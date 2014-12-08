package com.huawei.networkos.ops.views;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
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
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.wizard.LoadFileDialog;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;
/**
 * 添加Device的方法、内容类型
 *
 */
public class OpsServerHandelPage extends WizardPage {
	private Text nameText;
	private TreeViewer treeViewer;
	private ProjectTempletManager manager;
	private IProject iProject;
    private boolean isChosed = false;
    private UrlBean currentUrlBean;
    private boolean isUseInnerSchema;
    private Combo contentTypeText;
    private Combo methodCombo;
    private OpsServiceUrlHandle opsServiceUrlHandle = new OpsServiceUrlHandle();
    
    
	/**
	 * Create the wizard.
	 */
	public OpsServerHandelPage(IProject project) {
		super("Ops Url Handle Page");
		setTitle("Ops url handle page");
		setDescription("Add new URL handle");
        this.iProject = project;
	}

	
	
	
	public OpsServiceUrlHandle getOpsServiceUrlHandle() {
		return opsServiceUrlHandle;
	}




	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(4, false));
		
		CLabel lblNewLabel = new CLabel(container, SWT.NONE);
		lblNewLabel.setText("Name:");
		init();
		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if(nameText.getText()!=null&&!nameText.getText().equals("")){
					opsServiceUrlHandle.setName(nameText.getText());
					dialogChanged();
				}
			}
		});
		CLabel lblNewLabel_1 = new CLabel(container, SWT.NONE);
		lblNewLabel_1.setText("Method:");
		
		methodCombo = new Combo(container, SWT.READ_ONLY);

		methodCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		methodCombo.add("GET");
		methodCombo.add("MODIFY");
		methodCombo.add("DELETE");
		methodCombo.add("CREATE");
		methodCombo.add("GETALL");
		
		methodCombo.addModifyListener(new ModifyListener() {
			
			
			public void modifyText(ModifyEvent e) {
				opsServiceUrlHandle.setHandle(methodCombo.getItem(methodCombo.getSelectionIndex()));
				dialogChanged();
			}
		});
		CLabel lblNewLabel_2 = new CLabel(container, SWT.NONE);
		lblNewLabel_2.setText("Content Type:");
		
		contentTypeText = new Combo(container, SWT.READ_ONLY);
		contentTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		contentTypeText.add("json");
		contentTypeText.add("xml");
		contentTypeText.addModifyListener(new ModifyListener() {
			
			
			public void modifyText(ModifyEvent e) {
				if(contentTypeText.getText()!=null&&!contentTypeText.getText().equals(""))
				opsServiceUrlHandle.setContentType(contentTypeText.getText());
				dialogChanged();
			}
		});
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Label lblChoseUrl = new Label(container, SWT.NONE);
		lblChoseUrl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		lblChoseUrl.setText("Chose An Url:");
		FilteredTree filteredTree = new FilteredTree(container, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter(), true);

		filteredTree.getFilterControl().setText("");
		GridData gd_filteredTree = new GridData(SWT.FILL, SWT.FILL, true, true,
				8, 1);
		gd_filteredTree.heightHint = 138;
		gd_filteredTree.widthHint = 300;
        Map<String, List<String>> filesUrl = manager.getApiUrls();
		filteredTree.setLayoutData(gd_filteredTree);
		treeViewer = filteredTree.getViewer();
		treeViewer.setContentProvider(new TreeViewerContentProvider());
		treeViewer.setLabelProvider(new TreeViewerLabelProvider());
        treeViewer.setInput(DataFactory.getList(iProject, filesUrl));
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				 ISelection iSelection = treeViewer.getSelection();
	                
	                if (iSelection != null) {
	                    Object selectItem = ((TreeSelection) iSelection)
	                            .getFirstElement();
	                    if (selectItem instanceof RestFulApi) {
	                        isChosed = true;
	                       
	                      

	                        RestFulApi api = (RestFulApi) selectItem;
	                        changeIsUseInnerOps(api.getParent().getParent());
	                        if (isUseInnerSchema) {
	                            currentUrlBean = new UrlBean(manager.getApiManager().getRestApiJsonByUrl(api.getName()));
	                            opsServiceUrlHandle.setIsInnerSchemmer(true);
	                        } else {
	                            currentUrlBean = new UrlBean(manager.getUserApiManager().getRestApiJsonByUrl(api.getName()));
	                            opsServiceUrlHandle.setIsInnerSchemmer(false);
	                        }
	                        opsServiceUrlHandle.setApiUrl(api.getName());
	                        opsServiceUrlHandle.setBean(currentUrlBean);
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
	}
	
	
	
	private void dialogChanged() {
        if (!isChosed) {
            updateStatus("You must chosed an urlApi!");
            return;
        } 
        if(nameText.getText()==null||nameText.getText().equals("")){
        	updateStatus("Name can not be null");
        	return;
        }
        if(methodCombo.getSelectionIndex()==-1){
        	updateStatus("You must chose a method");
        	return;
        }
        if(contentTypeText.getText()==null||contentTypeText.getText().equals("")){
        	updateStatus("Content type cant be null"); 
        	return;
        }
        updateStatus(null);
    }
	
	 public void changeIsUseInnerOps(SchemaFiles files) {
	        if ("InnerSchemaFiles".equals(files.toString())) {
	            isUseInnerSchema = true;
	        } else {
	            isUseInnerSchema = false;
	        }
	    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message==null);
    }
    
    private void init() {
    	manager = (ProjectTempletManager) OpsManagerProjectTool.getTempletManager(iProject);
        List<String> files = ApiFileShower.getFileListFromFilePath(
                ProjectPathManager.getSchemaFolderPath(iProject), iProject);
        LoadFileDialog dialog = new LoadFileDialog(getShell(), manager, files, iProject);
        dialog.open();
	 }
}
