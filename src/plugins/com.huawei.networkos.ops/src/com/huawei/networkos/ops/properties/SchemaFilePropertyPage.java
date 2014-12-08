package com.huawei.networkos.ops.properties;
import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import org.eclipse.ui.dialogs.PropertyPage;

import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;

import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.networkos.ops.wizards.ChoseOpsInstanceRestfulapiWizard;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;
/**
 * 配置用户外置的schema文件的路径的配置页面
 * @author qWX182698
 * @Modify zWX199308
 */
public class SchemaFilePropertyPage extends PropertyPage {
	private IProgressMonitor monitor = new NullProgressMonitor();
	private IProject iProject;
	private Composite composite_2;
	private ProjectConfigXmlManager projectConfigXmlManager;
	private List list;
	
	private Map<String,ProjectTempletManager> mapProjectPythonTempletManager =  ChoseOpsInstanceRestfulapiWizard.MAP_MANAGER;
	/**
	 * Constructor for SamplePropertyPage.
	 */
	public SchemaFilePropertyPage() {
		super();
	}

	private void addFirstSection(Composite parent) {
	}

	private void addSeparator(Composite parent) {
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		iProject = PathTools.getCurrentProject();
		composite_2 = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout();
		gl_composite_2.numColumns = 9;
		composite_2.setLayout(gl_composite_2);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite_2.setLayoutData(data);
		
		addFirstSection(composite_2);
		addSeparator(composite_2);

		Label lblUsersSchemaFile = new Label(composite_2, SWT.NONE);
		lblUsersSchemaFile.setText("User's schema file");
		
		OpsManagerProjectTool.reLoadProject(iProject, null, "java");
		
		//得到当前的工程
		projectConfigXmlManager = (ProjectConfigXmlManager) OpsManagerProjectTool.getConfigManager(iProject);
		list = new List(composite_2, SWT.BORDER);


		changeList();

		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, true, true, 9, 1);
		gd_list.widthHint = 350;
		gd_list.heightHint = 211;
		list.setLayoutData(gd_list);

		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
	 
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("add schema file ");
		btnNewButton_1.addSelectionListener(new SelectionListener() {

			
			public void widgetSelected(SelectionEvent e) {
				//点击"Add"按钮,出现选择框
				handleLocationBrowseButtonPressed();
			}

			
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		Button btnNewButton = new Button(composite_2, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
 
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("delete schema file");
		btnNewButton.addSelectionListener(new SelectionListener() {

			
			public void widgetSelected(SelectionEvent e) {
				deleteSchemaFiles();

			}

			
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		return composite_2;
	}

	protected void performDefaults() {
		super.performDefaults();
		// Populate the owner text field with the default value

	}

	public boolean performOk() {
		// store the value in the owner text field

		projectConfigXmlManager.configOPSPath(null, iProject);
		return true;
	}
	/**
	 * 删除外置的schema文件的路径 并在当前项目的配置文件中进行同步操作
	 */
	private void deleteSchemaFiles() {
		String[] fileNames = list.getSelection();
		if (fileNames != null && fileNames.length != 0) {
			if (MessageDialog.openConfirm(this.getShell(), "Delete schemafile",
					"Are you sure delete this schema files?")) {

					
					
					if(mapProjectPythonTempletManager.containsKey(iProject.getName())){
						Map<String,java.util.List<String>> map = mapProjectPythonTempletManager.get(iProject.getName()).getUserApiManager().getMapFileNameUrl();
						
						for(String str : map.keySet()){
							if(str.equals(fileNames[0])){
								map.remove(str);
								
							}
						}
						Map<String,java.util.List<String>> map2 = mapProjectPythonTempletManager.get(iProject.getName()).getUrlsMap();
						for(String str : map2.keySet()){
							if(str.equals(fileNames[0])){
								map2.remove(str);
							}
						}
		
					}
					
					if(projectConfigXmlManager.removeSchemaFiles(fileNames[0])){
						projectConfigXmlManager.configOPSPath(null, iProject);
						changeList();
						MessageDialog.open(WARNING, getShell(), "Message",
								"Delete Files Success", SWT.NULL);
					}else{
						MessageDialog.open(WARNING, getShell(), "Message",
								"Delete Files False", SWT.NULL);
					}

				try {
					iProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		} else {
			MessageDialog.open(INFORMATION, getShell(), "Warning",
					"You did not chose any files", SWT.NULL);
		}
	}
	/**
	 * 给browser添加一个事件 如果我们按了该按钮就会出现一个文件选择框, 该框里面只出现*.xsd文件并且用户选择的文件必须是schema根文件
	 */
	private void handleLocationBrowseButtonPressed() {
		FileDialog dialog = new FileDialog(this.getShell());
		dialog.setText("Seletc schema files!!");

		//设置文件名过滤器
		dialog.setFilterExtensions(new String[] { "*.xsd" });
		String addedFiles = dialog.open();
		String[] existFiles = list.getItems();
		boolean isExist = false;
		if (addedFiles != null) {

			if (true) {

				File strFile = new File(addedFiles);

				String strFileName = strFile.getName();
				strFileName = strFileName
						.substring(0, strFileName.indexOf("."));
				//工程配置文件管理器添加选中的文件
				projectConfigXmlManager.addUserSchemaFiles(addedFiles);
				
				String plugid  = OpsManagerProjectTool.getProjectCreatePlugId(iProject);
				try{
					if(mapProjectPythonTempletManager.containsKey(iProject.getName())){
						
						mapProjectPythonTempletManager.get(iProject.getName()).addUserFile(addedFiles);
					}else{
						mapProjectPythonTempletManager.put(iProject.getName(), new ProjectTempletManager(plugid));
						mapProjectPythonTempletManager.get(iProject.getName()).addUserFile(addedFiles);
					}
					projectConfigXmlManager.configOPSPath(null, iProject);
					
				}catch(Exception exception){
					MessageDialog.openError(getShell(), "Schema file error", "You schema file is error and it can't be parsed!!");
					return;
				}

				changeList();
				MessageDialog.open(WARNING, getShell(), "Message",
						"Add Files Success", SWT.NULL);

			}
		} else {
			MessageDialog.open(INFORMATION, getShell(), "Message",
					"You did not chose any files", SWT.NULL);
		}
	}
	
	/**
	 * 更改界面中的list显示的schema路径的信息
	 */
	public void changeList(){
		list.removeAll();
		java.util.List<String> userSchemaFiles = projectConfigXmlManager.getUserSchemaFiles();
		for(String str : userSchemaFiles){
			list.add(str);
		}
	}

}