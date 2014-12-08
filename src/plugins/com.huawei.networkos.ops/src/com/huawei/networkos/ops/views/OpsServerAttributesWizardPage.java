package com.huawei.networkos.ops.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.huawei.network.opsdev.core.templet.ApiFileShower;
import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.UrlBean;
import com.huawei.network.opsdev.core.templet.UrlPropertiesBean;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.TableItemControls;
import com.huawei.network.opsdev.wizard.LoadFileDialog;
 
import com.huawei.tools.xml.config.OpsServiceUrlHandle;

public class OpsServerAttributesWizardPage extends WizardPage {
	private Table table;
	private OpsServiceUrlHandle handle;
	private List<TableItemControls> controls = new ArrayList<TableItemControls>();
	private IProject iProject;
	private ProjectTempletManager manager;
	private UrlBean bean;
	private List<UrlPropertiesBean> currentChosedBeands = new ArrayList<UrlPropertiesBean>();
	private Map<String ,String> errorStringMap = new HashMap<String, String>();
	
	
	
	public List<UrlPropertiesBean> getCurrentChosedBeands() {
		return currentChosedBeands;
	}

	public void setCurrentChosedBeands(List<UrlPropertiesBean> currentChosedBeands) {
		this.currentChosedBeands = currentChosedBeands;
	}

	public OpsServiceUrlHandle getHandle() {
		return handle;
	}

	/**
	 * Create the wizard.
	 */
	public OpsServerAttributesWizardPage(IProject iProject,OpsServiceUrlHandle handle) {
		super("Add Handle Attribute Page");
		setTitle("Add attribute page");
		setDescription("Add attribute to URL handle");
		this.iProject = iProject;
		this.handle = handle;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		CLabel lblNewLabel = new CLabel(container, SWT.NONE);
		lblNewLabel.setText("Set Body Properties Values");
		new Label(container, SWT.NONE);

		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(25);
		tblclmnNewColumn.setText("");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(86);
		tblclmnNewColumn_1.setText("Name");

		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(80);
		tblclmnNewColumn_3.setText("Type");
		
		TableColumn tblclmnAccess = new TableColumn(table, SWT.NONE);
		tblclmnAccess.setWidth(81);
		tblclmnAccess.setText("Access");

		TableColumn tblclmnNewColumn_4 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_4.setWidth(84);
		tblclmnNewColumn_4.setText("Example");
		Object object = null;
		try {
			object = iProject.getSessionProperty(new QualifiedName(
					"loadschemafile", "loadschemafile"));
		} catch (CoreException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		manager = (ProjectTempletManager) OpsManagerProjectTool.getTempletManager(iProject);
		
		LoadFileDialog dialog = new LoadFileDialog(getShell(), manager,ApiFileShower.getFileListFromFilePath(
                ProjectPathManager.getSchemaFolderPath(iProject), iProject) , iProject);
		dialog.open();
		 
		if (handle.getIsInnerSchemmer()) {
			bean = new UrlBean(manager.getApiManager().getRestApiJsonByUrl(
					handle.getApiUrl()));
		} else {
			bean = new UrlBean(manager.getUserApiManager().getRestApiJsonByUrl(
					handle.getApiUrl()));
		}
		for (UrlPropertiesBean propertiesBean : bean.getAll()) {
			final UrlPropertiesBean pBeans = propertiesBean;
			TableItem item = new TableItem(table, SWT.NULL);
			Label lable = new Label(table, SWT.NULL);
			lable.setText(pBeans.getName());
			item.setText(1, pBeans.getName());

			final Button button1 = new Button(table, SWT.CHECK);
			if(handle.getPropertiesHandles().containsKey(pBeans.getName())){
				button1.setSelection(true);
				currentChosedBeands.add(pBeans);
			}
			button1.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					if (button1.getSelection()) {
						if (currentChosedBeands.contains(pBeans)) {
							currentChosedBeands.remove(pBeans);
						}
						currentChosedBeands.add(pBeans);
					} else {
						if (currentChosedBeands.contains(pBeans)) {
							currentChosedBeands.remove(pBeans);
						}
					}

					dialogChanged();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					if (button1.getSelection()) {
						if (currentChosedBeands.contains(pBeans)) {
							currentChosedBeands.remove(pBeans);
						}
						currentChosedBeands.add(pBeans);
					} else {
						if (currentChosedBeands.contains(pBeans)) {
							currentChosedBeands.remove(pBeans);
						}
					}

					dialogChanged();

				}
			});
			TableEditor tedit2 = new TableEditor(table);
			tedit2.grabHorizontal = true;
			tedit2.setEditor(button1, item, 0);
			item.setText(4, pBeans.getExample());
//			if (pBeans.isEnumerate()) {
//				final CCombo cCombo = new CCombo(table, SWT.NULL);
//				if(handle.getPropertiesHandles().containsKey(pBeans.getName())){
//					int x = 0 ;
//				
//					for (String str : pBeans.getEnumTexts()) {
//						
//						
//						cCombo.add(str);
//						if(!str.equals(handle.getPropertiesHandles().get(pBeans.getName()))){
//							x++;
//						}
//					}
//					pBeans.setValue(cCombo.getText());
//					cCombo.select(x);
//				}else{
//					for (String str : pBeans.getEnumTexts()) {
//	
//						
//						cCombo.add(str);
//						
//					}
//				}
//				final String errorMessage = pBeans.getName()
//						+ "'s value cant't be null \n";
//
//				cCombo.addModifyListener(new ModifyListener() {
//
//					public void modifyText(ModifyEvent e) {
//						pBeans.setValue(cCombo.getText());
//
//						dialogChanged();
//					}
//				});
//				cCombo.setEditable(false);
//				TableEditor tedit3 = new TableEditor(table);
//				tedit3.grabHorizontal = true;
//				tedit3.setEditor(cCombo, item, 2);
//
//				controls.add(new TableItemControls(null, cCombo, button1,
//						new TableEditor[] { tedit2, tedit3 }));
//			} else if (pBeans.isBoolean()) {
//				final CCombo cCombo = new CCombo(table, SWT.NULL);
//				cCombo.add("true");
//				cCombo.add("false");
//				if(handle.getPropertiesHandles().containsKey(pBeans.getName())){
//					
//					if("true".equals(handle.getPropertiesHandles().get(pBeans.getName()))){
//						cCombo.select(0);
//					}else{
//						cCombo.select(1);
//					}
//					pBeans.setValue(cCombo.getText());
//				}
//				cCombo.addModifyListener(new ModifyListener() {
//
//					public void modifyText(ModifyEvent e) {
//						pBeans.setValue(cCombo.getText());
//
//						dialogChanged();
//					}
//				});
//				cCombo.setEditable(false);
//				TableEditor tedit3 = new TableEditor(table);
//				tedit3.grabHorizontal = true;
//				tedit3.setEditor(cCombo, item, 2);
//				controls.add(new TableItemControls(null, cCombo, button1,
//						new TableEditor[] { tedit2, tedit3 }));
//			} else {
//				final Text text = new Text(table, SWT.NULL);
//				TableEditor tedit3 = new TableEditor(table);
//				tedit3.grabHorizontal = true;
//				tedit3.setEditor(text, item, 2);
//				if(handle.getPropertiesHandles().containsKey(pBeans.getName())){
//					text.setText(handle.getPropertiesHandles().get(pBeans.getName()));
//					pBeans.setValue(text.getText());
//				}
//				final String errorMessage = pBeans.getName()
//						+ "'s value is not match the type ,please check it and enter it again\n";
//				text.addModifyListener(new ModifyListener() {
//
//					public void modifyText(ModifyEvent e) {
//						if (currentChosedBeands.contains(pBeans)) {
//							String str = pBeans.setValue(text.getText());
//							if (str!=null) {
//								errorStringMap.put(pBeans.getName(), str);
//							} else {
//								errorStringMap.remove(pBeans.getName());
//							}
//
//							dialogChanged();
//						} else {
//							String str = pBeans.setValue(text.getText());
//							if (str!=null) {
//								errorStringMap.put(pBeans.getName(), str);
//							} else {
//								errorStringMap.remove(pBeans.getName());
//							}
//							dialogChanged();
//						}
//					}
//
//				});
			controls.add(new TableItemControls(null, null, button1,
					new TableEditor[] { tedit2 }));
			item.setText(3,pBeans.getAccess());
			item.setText(2, pBeans.getType());
			item.setText(4,pBeans.getExample());

		}

	}

	private void dialogChanged() {

		if (errorStringMap.size() != 0) {
			StringBuffer error = new StringBuffer();
			for (String str : errorStringMap.values()) {
				error.append(str);
			}
			updateStatus(error.toString());
			return;
		}
//		if (currentChosedBeands.size() == 0) {
//			updateStatus("You should chose at least one attribute!");
//			return;
//		}
//		if (!isCurrentChosedBeandsNotNull()) {
//			updateStatus("The attribute value you chosed cant't be null");
//			return;
//		}
		updateStatus(null);
	}

//	private boolean isCurrentChosedBeandsNotNull() {
//		for (UrlPropertiesBean bean : currentChosedBeands) {
//			if (bean.getValue() == null || bean.getValue().equals("")) {
//				return false;
//			}
//		}
//		return true;
//	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}


}
