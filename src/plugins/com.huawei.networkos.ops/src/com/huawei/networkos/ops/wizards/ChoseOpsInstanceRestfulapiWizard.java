package com.huawei.networkos.ops.wizards;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.custom.CLabel;

import com.huawei.network.opsdev.core.templet.ApiFileShower;
import com.huawei.network.opsdev.core.templet.ApiJsonXmlPreviewManager;
import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.treeview.node.DataFactory;
import com.huawei.network.opsdev.core.treeview.node.RestFulApi;
import com.huawei.network.opsdev.core.treeview.node.SchemaFile;
import com.huawei.network.opsdev.core.treeview.node.SchemaFiles;
import com.huawei.network.opsdev.core.treeview.node.TreeViewerContentProvider;
import com.huawei.network.opsdev.core.treeview.node.TreeViewerLabelProvider;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.wizard.LoadFileDialog;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.internal.Workbench;

/**
 * 生成restfulapi 的创建页面
 * 
 * @author qWX182698 
 * 
 */
public class ChoseOpsInstanceRestfulapiWizard extends WizardPage {
	public static Map<String, ProjectTempletManager> MAP_MANAGER = new HashMap<String, ProjectTempletManager>();

	private IProject iProject;
	private List<String> files;
	private Map<String, List<String>> filesUrl;
	private StyledText previewText;
	private Button xmlButton;
	private Button jsonButton;
	private ProjectTempletManager manager;
	private String choseUrl;
	private List<String> chosedUrl = new ArrayList<String>();
	private String currentSchemaFileName;
	private String currentSchemaFile;
	private IPath iPath;
	private TreeViewer treeViewer;
	private String url;
	private boolean isUseInnerSchema;

	/**
	 * Create the wizard.
	 */

	public void performHelp() {

		if (!currentSchemaFileName.isEmpty()) {

			// String cmdStr =
			// "D:\\ops-demo\\tool\\eclipse-SDK-3.7\\eclipse-SDK-3.7.1-win32\\eclipse\\epl-v10.html";
			ProjectConfigXmlManager configXml = (ProjectConfigXmlManager) OpsManagerProjectTool.getConfigManager(iProject);
			
			
			if (null == configXml.getOpsapiDocpath() || !configXml.getOpsapiDocpath().isEmpty())
			{
				String cmdStr =  configXml.getOpsapiDocpath()	+ File.separator+ currentSchemaFileName.replace(".xsd","" ).replace("_action", "")
						+ "_specification.html";
				BareBonesBrowserLaunch.openURL(cmdStr);
			}
		}
	}

	public ProjectTempletManager getPythonTempletManager() {
		return manager;
	}

	public List<String> getChosedUrls() {
		return chosedUrl;
	}
	
	public String getCurrentSchemaFile() {
		return currentSchemaFileName;
	}

	/**
	 * 该页面的构造函数,设置页面的一些基本显示信息
	 */
	public ChoseOpsInstanceRestfulapiWizard() {
		super("wizardPage");
		setTitle("Create Restful Api");
		setDescription("This wizard according to the selected schema files, generate Restful API classes.");

	}

	public boolean getIsUseInnerSchema() {
		return isUseInnerSchema;
	}

	public IPath getIPath() {
		return iPath;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		if (ProjectPathManager.getCurrentProject() == null) {
			MessageDialog.openError(getShell(), "Error",
					"You Must Chose A Project");
			return;
		}

		iPath = ProjectPathManager.getSelectFolder();
		iProject = ProjectPathManager.getCurrentProject();
		if (ProjectPathManager.getSchemaFolderPath(iProject) == null) {
			MessageDialog.openError(getShell(), "Error",
					"The project you chosed do not have schema resource");
			return;
		}

		files = ApiFileShower.getFileListFromFilePath(
				ProjectPathManager.getSchemaFolderPath(iProject), iProject);
		Object object = null;
		try {
			//..
			object = iProject.getSessionProperty(new QualifiedName(
					"loadschemafile", "loadschemafile"));
		} catch (CoreException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (object != null) {
			MAP_MANAGER.put(iProject.getName(), (ProjectTempletManager) object);
		}
		if (!MAP_MANAGER.containsKey(iProject.getName())) {
			
			String plugid = OpsManagerProjectTool.getProjectCreatePlugId(iProject);
			manager = new ProjectTempletManager(plugid);
			MAP_MANAGER.put(iProject.getName(), manager);
		} else {
			manager = MAP_MANAGER.get(iProject.getName());
		}
		try {
			iProject.setSessionProperty(new QualifiedName("loadschemafile",
					"loadschemafile"), manager);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		LoadFileDialog dialog = new LoadFileDialog(getShell(), manager, files,
				iProject);
		dialog.open();
		filesUrl = manager.getApiUrls();
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(7, false));
		// previewText = new StyledText(container, SWT.BORDER);
		// previewText.setBounds(10, 212, 489, 182);
		CLabel treeLable = new CLabel(container, SWT.NONE);
		treeLable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1));
		treeLable.setText("Select Restful API:");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		FilteredTree filteredTree = new FilteredTree(container, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter(), true);

		filteredTree.getFilterControl().setText("");
		GridData gd_filteredTree = new GridData(SWT.FILL, SWT.FILL, true, true,
				7, 1);
		gd_filteredTree.heightHint = 156;
		gd_filteredTree.widthHint = 300;
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

						changePreview((RestFulApi) selectItem);
						changeIsUseInnerOps(((RestFulApi) selectItem)
								.getParent().getParent());
						currentSchemaFile =  ((RestFulApi) selectItem).getParent().getFullPath();
						currentSchemaFileName =  ((RestFulApi) selectItem)
								.getParent().getName();
						chosedUrl.clear();
					} else {
						choseUrl = "";
						currentSchemaFileName ="";
						currentSchemaFile = "";
					}
					if (selectItem instanceof SchemaFile) {
						SchemaFile file = (SchemaFile) selectItem;
						changePreview(file);
						changeIsUseInnerOps(file.getParent());
						currentSchemaFile = file.getFullPath();
						currentSchemaFileName =  file.getName();
						chosedUrl.clear();
						for (Object api : file.getChild()) {
							chosedUrl.add(api.toString());
						}
					} else {
						chosedUrl.clear();
					}
				} else {
					choseUrl = "";
					currentSchemaFileName ="";
					currentSchemaFile ="";
				}
				dialogChanged();
			}
		});
		CLabel previewLabel = new CLabel(container, SWT.NONE);
		previewLabel.setText("Preview:");
		new Label(container, SWT.NONE);
		xmlButton = new Button(container, SWT.RADIO);
		GridData gd_xmlButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_xmlButton.widthHint = 63;
		xmlButton.setLayoutData(gd_xmlButton);
		xmlButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				ISelection iSelection = treeViewer.getSelection();

				if (iSelection != null) {
					Object selectItem = ((TreeSelection) iSelection)
							.getFirstElement();
					if (selectItem instanceof RestFulApi) {

						changePreview((RestFulApi) selectItem);
						dialogChanged();
					}
				}
			}
		});
		xmlButton.setText("XML");
		jsonButton = new Button(container, SWT.RADIO);

		jsonButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				ISelection iSelection = treeViewer.getSelection();

				if (iSelection != null) {
					Object selectItem = ((TreeSelection) iSelection)
							.getFirstElement();
					if (selectItem instanceof RestFulApi) {

						changePreview((RestFulApi) selectItem);
						dialogChanged();
					}
				}
			}
		});
		jsonButton.setText("Json");
		jsonButton.setSelection(true);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		ScrolledComposite scrolledComposite = new ScrolledComposite(container,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true,
				true, 7, 1);
		gd_scrolledComposite.heightHint = 111;
		gd_scrolledComposite.widthHint = 302;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		previewText = new StyledText(scrolledComposite, SWT.BORDER
				| SWT.V_SCROLL);
		previewText.setEditable(false);

		scrolledComposite.setContent(previewText);
		scrolledComposite.setMinSize(previewText.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		dialogChanged();

	}

	/**
	 * 改变xml,或者json显示的内容
	 * 
	 * @param api
	 */
	private void changePreview(RestFulApi api) {
		xmlButton.setEnabled(true);
		jsonButton.setEnabled(true);
		String fileName = api.getName();
		

		if (xmlButton.getSelection()) {
			ApiJsonXmlPreviewManager apiJsonXmlPreviewManager = new ApiJsonXmlPreviewManager();
			previewText.setText(fileName+"\n"+apiJsonXmlPreviewManager.showXml(
					fileName,
					manager,
					"InnerSchemaFiles".equals(api.getParent().getParent()
							.getName())));
			choseUrl = fileName;
		} else if (jsonButton.getSelection()) {
			ApiJsonXmlPreviewManager apiJsonXmlPreviewManager = new ApiJsonXmlPreviewManager();
			previewText.setText(fileName+"\n"+apiJsonXmlPreviewManager.showJson(
					fileName,
					manager,
					"InnerSchemaFiles".equals(api.getParent().getParent()
							.getName())));
			choseUrl = fileName;
		}

		dialogChanged();
	}

	private void changePreview(SchemaFile file) {
		xmlButton.setEnabled(false);
		jsonButton.setEnabled(false);
		StringBuffer buffer = new StringBuffer();
		for (Object str : file.getChild()) {
			buffer.append(str.toString() + "\n");
		}
		previewText.setText(buffer.toString());
	}

	/**
	 * 获取我们当前选择的url路径
	 * 
	 * @return
	 */
	public String getChoseUrl() {
		return choseUrl;
	}

	/**
	 * 改变页面的日志 只有我们选择了url 才能点击完成按钮, 不然将显示相应的需要改正的错误,并且不能进入到下一步
	 */
	private void dialogChanged() {

		if ((getChoseUrl() == null || getChoseUrl().equals(""))
				&& chosedUrl.isEmpty()) {
			updateStatus("You must chose an api Or a schemaFile");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * 获取当前选择的项目
	 * 
	 * @return
	 */
	public IProject getIProject() {
		return iProject;
	}

	public void changeIsUseInnerOps(SchemaFiles files) {
		if ("InnerSchemaFiles".equals(files.toString())) {
			isUseInnerSchema = true;
		} else {
			isUseInnerSchema = false;
		}
	}

	public boolean ifOverride() {
		return MessageDialog
				.openQuestion(getShell(), "Do you want to overwride this file",
						"this file is already exist , please chose if you want to overwride it? ");
	}
}
