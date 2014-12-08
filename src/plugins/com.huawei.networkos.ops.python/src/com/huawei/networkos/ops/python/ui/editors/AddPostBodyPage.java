package com.huawei.networkos.ops.python.ui.editors;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import com.huawei.network.opsdev.core.templet.ApiFileShower;
import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.UrlBean;
import com.huawei.network.opsdev.core.treeview.node.DataFactory;
import com.huawei.network.opsdev.core.treeview.node.RestFulApi;
import com.huawei.network.opsdev.core.treeview.node.SchemaFile;
import com.huawei.network.opsdev.core.treeview.node.SchemaFiles;
import com.huawei.network.opsdev.core.treeview.node.TreeViewerContentProvider;
import com.huawei.network.opsdev.core.treeview.node.TreeViewerLabelProvider;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.network.opsdev.wizard.LoadFileDialog;
//import com.huawei.networkos.ops.wizards.LoadFileDialog;

import org.eclipse.swt.layout.GridLayout;

import org.eclipse.swt.widgets.Label;
/**
 * Add Post Body的第一个页面
 * 在inner或user的树列表中选择一个schema
 * @author zWX199308
 *
 */


public class AddPostBodyPage extends WizardPage {
    private TreeViewer treeViewer;
    private ProjectTempletManager manager;


    private UrlBean currentUrlBean;
    private boolean isUseInnerSchema;
    private boolean isChosed = false;
    private IProject iProject;
    private boolean canToNextPage =false;

    public boolean getIsUseInnerSchema() {
        return isUseInnerSchema;
    }

    /**
     * Create the wizard.
     */
    public AddPostBodyPage(IProject project) {
        super("wizardPage");
        setTitle("Create Post Body");
        setDescription("Please Choose an Api");
        setPageComplete(false);
        
        this.iProject = project;
    }

    /**
     * Create contents of the wizard.
     * @param parent
     */
    public void createControl(Composite parent) {
        
        final Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout(8, false));
        init();
        FilteredTree filteredTree = new FilteredTree(container, SWT.MULTI
                | SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter(), true);

        filteredTree.getFilterControl().setText("");
        GridData gd_filteredTree = new GridData(SWT.FILL, SWT.FILL, true,
                true, 8, 1);
        gd_filteredTree.heightHint = 138;
        gd_filteredTree.widthHint = 300;
        filteredTree.setLayoutData(gd_filteredTree);
        treeViewer = filteredTree.getViewer();
        treeViewer.setContentProvider(new TreeViewerContentProvider());
        treeViewer.setLabelProvider(new TreeViewerLabelProvider());
        Map<String, List<String>> filesUrl = manager.getApiUrls();
        treeViewer.setInput(DataFactory.getList(iProject, filesUrl));
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                ISelection iSelection = treeViewer.getSelection();
                
               
                dialogChanged();
                if (iSelection != null) {
                    Object selectItem = ((TreeSelection) iSelection)
                            .getFirstElement();
                    if (selectItem instanceof RestFulApi) {
                        isChosed = true;
                       
                      

                        RestFulApi api = (RestFulApi) selectItem;
                        changeIsUseInnerOps(api.getParent().getParent());
                        if (isUseInnerSchema) {
                            currentUrlBean = new UrlBean(manager.getApiManager().getRestApiJsonByUrl(api.getName()));
                        } else {
                            currentUrlBean = new UrlBean(manager.getUserApiManager().getRestApiJsonByUrl(api.getName()));
                        }
                        IWizardPage page = getNextPage();
                        if(page instanceof AddPostBodyPage2){
                            ((AddPostBodyPage2)page).createItem(currentUrlBean);
                        }
                       
                    } else {
                        isChosed = false;
                    }
                    if (selectItem instanceof SchemaFile) {
                        SchemaFile file = (SchemaFile) selectItem;

                        changeIsUseInnerOps(file.getParent());
                        isChosed = false;

                    }
                }
                dialogChanged();
            }
        });

        setControl(container);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        Label label_1 = new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
    }

    


    private void init() {
        Object object = null;

        try {
            if (iProject.getSessionProperties().containsKey(new QualifiedName("loadschemafile", "loadschemafile"))) {
                object = iProject.getSessionProperty(new QualifiedName("loadschemafile", "loadschemafile"));
            }
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (object != null) {
            manager = (ProjectTempletManager) object;
        } else {
            manager = new ProjectTempletManager("com.huawei.networkos.ops.core");
            try {
                iProject.setSessionProperty(new QualifiedName("loadschemafile", "loadschemafile"), manager);
            } catch (CoreException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        List<String> files = ApiFileShower.getFileListFromFilePath(
                ProjectPathManager.getSchemaFolderPath(iProject), iProject);
        LoadFileDialog dialog = new LoadFileDialog(getShell(), manager, files, iProject);
        dialog.open();

    }



    /**
     * 改变页面的日志 只有我们选择了url 才能点击完成按钮, 不然将显示相应的需要改正的错误,并且不能进入到下一步
     */
    private void dialogChanged() {
        if (!isChosed) {
            updateStatus("You must chosed an urlApi!");
            canToNextPage=false;
            getContainer().updateButtons();
            return;
        }
   
       
        updateStatus(null);
        getContainer().updateButtons();
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        if(message==null){
            setMessage("");
            canToNextPage=true;
        }

    }

    public void changeIsUseInnerOps(SchemaFiles files) {
        if ("InnerSchemaFiles".equals(files.toString())) {
            isUseInnerSchema = true;
        } else {
            isUseInnerSchema = false;
        }
    }
    
    public boolean canFlipToNextPage() {
        return canToNextPage;
    }


}
