package com.huawei.networkos.ops.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.internal.Workbench;

import com.huawei.network.opsdev.core.templet.ApiFileShower;
import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.UrlBean;
import com.huawei.network.opsdev.core.templet.UrlPropertiesBean;
import com.huawei.network.opsdev.core.treeview.node.DataFactory;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.TableItemControls;
import com.huawei.network.opsdev.wizard.LoadFileDialog;
import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;
/**
 * 测试数据源页面
 *
 */
public class TestHandelPage extends WizardPage {
	private Combo serviceCombo;
	private Combo operationCombo;
	private Button testButton;
	private ProjectTempletManager manager;
	private Table protertiesTable;

	private String response;
	private Composite composite_2;
	private List<OpsService> opsServices = new ArrayList<OpsService>();
	private IProject iProject;
	private List<TableItemControls> controls = new ArrayList<TableItemControls>();
	private List<UrlPropertiesBean> currentChosedBeands = new ArrayList<UrlPropertiesBean>();
	private Map<String, String> errorStringMap = new HashMap<String, String>();
	private TableColumn tblclmnAccess;
	private TableColumn tblclmnExample;
	private OpsService currentOpsService;
	private OpsServiceUrlHandle currentHandle;
	private String errorString;
	private TabFolder tabFolder;
	private TabItem treeItem;
	private TabItem rawItem;
	private StyledText respText;
	private Tree tree;
	private TreeColumn trclmnResponseKey;
	private TreeColumn trclmnResponseValue;
	private GridData gd_protertiesTable;

	/**
	 * Create the wizard.
	 */
	public TestHandelPage(List<OpsService> opsServices, IProject iProject,OpsService currentOpsService,OpsServiceUrlHandle currentHandle) {
		super("Test the device handle Page");
		setTitle("Test Device Handle Page");
		setDescription("The test device handle is correct ");
		this.opsServices = opsServices;
		this.iProject = iProject;
		this.currentOpsService = currentOpsService;
		this.currentHandle = currentHandle;
	}
	
	
	
	

	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		
		composite.setLayout(new GridLayout(3, false));
		setControl(composite);

		Label lblService = new Label(composite, SWT.NONE);
		lblService.setText("Service:");

		serviceCombo = new Combo(composite, SWT.READ_ONLY);
		GridData gd_serviceCombo_1 = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1);
		gd_serviceCombo_1.widthHint = 568;
		serviceCombo.setLayoutData(gd_serviceCombo_1);

		Label lblOperation = new Label(composite, SWT.NONE);
		lblOperation.setText("Operation:");

		operationCombo = new Combo(composite, SWT.READ_ONLY);
		operationCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));

		Button btnAu = new Button(composite, SWT.CHECK);
		GridData gd_btnAu = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2,
				1);
		gd_btnAu.widthHint = 566;
		btnAu.setLayoutData(gd_btnAu);
		btnAu.setText("Authentication required");
		new Label(composite, SWT.NONE);

		protertiesTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		gd_protertiesTable = new GridData(SWT.FILL, SWT.FILL, false, false, 3,
				1);
		gd_protertiesTable.heightHint = 49;
		protertiesTable.setLayoutData(gd_protertiesTable);
		protertiesTable.setHeaderVisible(true);
		protertiesTable.setLinesVisible(true);

		TableColumn tblclmnNewColumn_2 = new TableColumn(protertiesTable,
				SWT.NONE);

		tblclmnNewColumn_2.setWidth(105);
		tblclmnNewColumn_2.setText("Parameter");

		TableColumn tblclmnNewColumn_4 = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn_4.setWidth(115);
		tblclmnNewColumn_4.setText("Value");

		TableColumn tblclmnNewColumn_3 = new TableColumn(protertiesTable,
				SWT.NONE);
		tblclmnNewColumn_3.setWidth(83);
		tblclmnNewColumn_3.setText("Type");

		tblclmnAccess = new TableColumn(protertiesTable, SWT.NONE);
		tblclmnAccess.setWidth(100);
		tblclmnAccess.setText("Access");

		tblclmnExample = new TableColumn(protertiesTable, SWT.NONE);
		tblclmnExample.setWidth(100);
		tblclmnExample.setText("Example");

		testButton = new Button(composite, SWT.NONE);
		GridData gd_testButton_1 = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 3, 1);
		gd_testButton_1.widthHint = 72;
		testButton.setLayoutData(gd_testButton_1);
		testButton.setText("Test");
		testButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				test();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				3, 1));

		tabFolder = new TabFolder(composite_2, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		treeItem = new TabItem(tabFolder, SWT.NONE);
		treeItem.setText("Tree View");

		composite = new Composite(tabFolder, SWT.NONE);
		treeItem.setControl(composite);
		TreeColumnLayout tcl_composite = new TreeColumnLayout();
		composite.setLayout(tcl_composite);

		tree = new Tree(composite, SWT.BORDER);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		trclmnResponseKey = new TreeColumn(tree, SWT.NONE);
		tcl_composite.setColumnData(trclmnResponseKey, new ColumnPixelData(261,
				true, true));
		trclmnResponseKey.setText("Response Key");

		trclmnResponseValue = new TreeColumn(tree, SWT.NONE);
		tcl_composite.setColumnData(trclmnResponseValue, new ColumnPixelData(
				261, true, true));
		trclmnResponseValue.setText("Response Value");

		rawItem = new TabItem(tabFolder, SWT.NONE);
		rawItem.setText("Raw View");

		respText = new StyledText(tabFolder, SWT.BORDER | SWT.V_SCROLL);
		rawItem.setControl(respText);

		// responseText responseText = new responseText(composite_2, SWT.BORDER
		// | SWT.WRAP);
		// responseText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true, 1, 1));

		// responseTable = new Table(composite_2, SWT.BORDER |
		// SWT.FULL_SELECTION);
		// responseTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true, 1, 1));
		// responseTable.setHeaderVisible(true);
		// responseTable.setLinesVisible(true);
		//
		// TableColumn tblclmnNewColumn = new TableColumn(responseTable,
		// SWT.NONE);
		// tblclmnNewColumn.setWidth(203);
		// tblclmnNewColumn.setText("Response Name");
		//
		// TableColumn tblclmnNewColumn_1 = new TableColumn(responseTable,
		// SWT.NONE);
		// tblclmnNewColumn_1.setWidth(157);
		// tblclmnNewColumn_1.setText("Response Value");
		init();
	}

	public void init() {
		testButton.setEnabled(false);
		manager = (ProjectTempletManager) OpsManagerProjectTool.getTempletManager(iProject);
		
		LoadFileDialog dialog = new LoadFileDialog(Workbench.getInstance()
				.getDisplay().getActiveShell(), manager,
				ApiFileShower.getFileListFromFilePath(
						ProjectPathManager.getSchemaFolderPath(iProject),
						iProject), iProject);
		dialog.open();
		OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.SCHEMAFILE_CONFIG, manager) ;
		for (OpsService opsService : opsServices) {
			serviceCombo.add(opsService.getName());
		}
		operationCombo.removeAll();
		serviceCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				serviceCombChange();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		operationCombo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleComboChange();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		serviceCombo.select(opsServices.indexOf(currentOpsService));
		serviceCombChange();
		int x = 0;
		for(String str: operationCombo.getItems()){
			if(str.startsWith(currentHandle.getName())){
				break;
			}
			x++;
		}
		operationCombo.select(x);
		handleComboChange();
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

		if (!isCurrentChosedBeandsNotNull()) {
			updateStatus("The attribute value cant't be null");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
		if(message!=null){
			testButton.setEnabled(false);
		}else{
			testButton.setEnabled(true);
		}
	}

	private boolean isCurrentChosedBeandsNotNull() {
		for (UrlPropertiesBean bean : currentChosedBeands) {
			if (bean.getValue() == null || bean.getValue().equals("")) {
				return false;
			}
		}
		return true;
	}

	public String getUrl() {
		// self.full_url_body = "/arp/arpInterfaces/arpInterface"
		// def getUrlBody(self):
		// return self.full_url_body % ()
		// url_arg_value = attributes[key].replace("/", "%2F")
		// url_path+= "%s=%s&"%(key, url_arg_value)
		currentOpsService = opsServices.get(serviceCombo.getSelectionIndex());
		currentHandle = currentOpsService.getHandles().get(
				operationCombo.getSelectionIndex());
		StringBuffer url = new StringBuffer();
		url.append("http://" + currentOpsService.getServer() + ":"
				+ currentOpsService.getPort() + "/"
				+ currentOpsService.getDevicename() + "/"
				+ currentOpsService.getDeviceId());
		String fullUrl = currentHandle.getFulApiUrl(manager)+"?";
		for (UrlPropertiesBean propertiesBean : currentHandle.getChosedBean()
				.getKeys()) {
			fullUrl = fullUrl.replaceFirst(propertiesBean.getName() + "=%s",
					propertiesBean.getName() + "=" + propertiesBean.getValue());
			propertiesBean.getName();
		}

		url.append(fullUrl);
		for (UrlPropertiesBean propertiesBean : currentHandle.getChosedBean()
				.getBeans()) {
			url.append(propertiesBean.getName() + "="
					+ propertiesBean.getValue().replaceAll("/", "%2F") + "&");

		}
		if(url.toString().trim().endsWith("&") || url.toString().trim().endsWith("?")){
			return url.substring(0, url.length() - 1)+"/"+currentHandle.getContentType();
		}else{
			return url.toString()+"/"+currentHandle.getContentType();
		}
	}


	
	
	public void test() {
		tree.removeAll();
		response = "";
		//String urlStr = getUrl();
		errorString = "";
		//methodCombo.add("GET");
		// methodCombo.add("MODIFY");
		// methodCombo.add("DELETE");
		// methodCombo.add("CREATE");
		// methodCombo.add("GETALL");
		URL geturl = null;
		//URL posturl = null;
		
		try {
			
			geturl = new URL(getUrl());
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			errorString += "\n" + e.getMessage();
		}
		
		
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) geturl.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			errorString += "\n" + e.getMessage()
					 + "\n"
					+ e.toString();
		}
		if (conn != null) {
			if ("MODYFY".equals(currentHandle.getHandle())) {
				try {

					conn.setRequestMethod("PUT");
				} catch (ProtocolException e) {
					e.printStackTrace();
					errorString += "\n" + e.getMessage()
							
							+ "\n" + e.toString();
				}
			} else if ("DELETE".equals(currentHandle.getHandle())) {
				try {

					conn.setRequestMethod("DELETE");
				} catch (ProtocolException e) {
					e.printStackTrace();
					errorString += "\n" + e.getMessage()
							
							+ "\n" + e.toString();
				}
			} else if ("CREATE".equals(currentHandle.getHandle())) {
				try {

					conn.setRequestMethod("POST");
				} catch (ProtocolException e) {
					e.printStackTrace();
					errorString += "\n" + e.getMessage()
							
							+ "\n" + e.toString();
				}
			} else {
				try {

					conn.setRequestMethod("GET");
				} catch (ProtocolException e) {
					e.printStackTrace();
					errorString += "\n" + e.getMessage()
							
							+ "\n" + e.toString();
				}
			}
			conn.setConnectTimeout(5000);
			
			
			try {
				
				BufferedReader 	bufferedReader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				//System.out.println(bufferedReader);
				String  line;

				while ((line = bufferedReader.readLine()) != null) {
					response += line + "\n";
				}

			} catch (Exception e) {
				e.printStackTrace();
				errorString += "\n" + e.getMessage()
						 +"\n"
						+ e.toString();
			}

			if (errorString != null && errorString != "") {
				MessageDialog.openError(getShell(), "ERROR", errorString);
				return;

			}
			respText.setText(response);
			
			if(getUrl().trim().endsWith("xml")){
				TreeItem item = new TreeItem(tree, SWT.NULL);
				DataFactory.setXmlRootTreeItem(item, response);
			}else{
				DataFactory.setJsonRootTreeItem(tree, response);
			}
			
		
			
			
		}

	}
	
	public void serviceCombChange(){
		errorStringMap.clear();
		operationCombo.removeAll();
		currentChosedBeands.clear();
		OpsService opsService = opsServices.get(serviceCombo
				.getSelectionIndex());
		for (OpsServiceUrlHandle handle : opsService.getHandles()) {
			if (handle.getChosedBean() == null) {
				if (handle.getIsInnerSchemmer()) {
					handle.setChosedBean(new UrlBean(manager
							.getApiManager().getRestApiJsonByUrl(
									handle.getApiUrl()), handle
							.getPropertiesHandles()));
				} else {
					handle.setChosedBean(new UrlBean(manager
							.getUserApiManager().getRestApiJsonByUrl(
									handle.getApiUrl()), handle
							.getPropertiesHandles()));
				}
			}
			
			if (handle.getPropertiesHandles().keySet().size() != 0) {
				operationCombo.add(handle.getName());
						//+ handle.getPropertiesHandles().keySet()
							//	.toString());
				
			} else {
				operationCombo.add(handle.getName());
			}
			dialogChanged();
		}
	}
	
	public void handleComboChange(){
		errorStringMap.clear();
		for (TableItemControls itemControls : controls) {
			itemControls.dispose();
		}
		currentChosedBeands.clear();
		controls.clear();
		protertiesTable.removeAll();

		OpsServiceUrlHandle handle = opsServices
				.get(serviceCombo.getSelectionIndex()).getHandles()
				.get(operationCombo.getSelectionIndex());
		
		for (UrlPropertiesBean propertiesBean : handle.getChosedBean()
				.getAll()) {
			final UrlPropertiesBean pBeans = propertiesBean;
			TableItem item = new TableItem(protertiesTable, SWT.NULL);
			Label lable = new Label(protertiesTable, SWT.NULL);
			lable.setText(pBeans.getName());
			item.setText(0, pBeans.getName());

			currentChosedBeands.add(pBeans);
			if (pBeans.isEnumerate()) {
				final CCombo cCombo = new CCombo(protertiesTable,
						SWT.NULL);
				if (handle.getPropertiesHandles().containsKey(
						pBeans.getName())) {
					int x = 0;

					for (String str : pBeans.getEnumTexts()) {

						cCombo.add(str);
						if (!str.equals(handle.getPropertiesHandles()
								.get(pBeans.getName()))) {
							x++;
						}
					}
					pBeans.setValue(cCombo.getText());
					cCombo.select(x);
				} else {
					for (String str : pBeans.getEnumTexts()) {

						cCombo.add(str);

					}
				}
				final String errorMessage = pBeans.getName()
						+ "'s value cant't be null \n";

				cCombo.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent e) {
						pBeans.setValue(cCombo.getText());

						dialogChanged();
					}
				});
				cCombo.setEditable(false);
				TableEditor tedit3 = new TableEditor(protertiesTable);
				tedit3.grabHorizontal = true;
				tedit3.setEditor(cCombo, item, 1);

				controls.add(new TableItemControls(null, cCombo, null,
						new TableEditor[] { tedit3 }));
			} else if (pBeans.isBoolean()) {
				final CCombo cCombo = new CCombo(protertiesTable,
						SWT.NULL);
				cCombo.add("true");
				cCombo.add("false");
				if (handle.getPropertiesHandles().containsKey(
						pBeans.getName())) {

					if ("true".equals(handle.getPropertiesHandles()
							.get(pBeans.getName()))) {
						cCombo.select(0);
					} else {
						cCombo.select(1);
					}
					pBeans.setValue(cCombo.getText());
				}
				cCombo.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent e) {
						pBeans.setValue(cCombo.getText());

						dialogChanged();
					}
				});
				cCombo.setEditable(false);
				TableEditor tedit3 = new TableEditor(protertiesTable);
				tedit3.grabHorizontal = true;
				tedit3.setEditor(cCombo, item, 1);
				controls.add(new TableItemControls(null, cCombo, null,
						new TableEditor[] { tedit3 }));
			} else {
				final Text text = new Text(protertiesTable, SWT.NULL);
				TableEditor tedit3 = new TableEditor(protertiesTable);
				tedit3.grabHorizontal = true;
				tedit3.setEditor(text, item, 1);

				final String errorMessage = pBeans.getName()
						+ "'s value is not match the type ,please check it and enter it again\n";
				text.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent e) {
						if (currentChosedBeands.contains(pBeans)) {
							String str = pBeans.setValue(text.getText());
							if (str != null) {
								errorStringMap.put(pBeans.getName(),
										str);
							} else {
								errorStringMap.remove(pBeans.getName());
							}

							dialogChanged();
						} else {
							String str = pBeans.setValue(text.getText());
							if (str != null) {
								errorStringMap.put(pBeans.getName(),
										str);
							} else {
								errorStringMap.remove(pBeans.getName());
							}
							dialogChanged();
						}
					}

				});
				controls.add(new TableItemControls(text, null, null,
						new TableEditor[] { tedit3 }));
				item.setText(3, pBeans.getAccess());
				item.setText(2, pBeans.getType());
				item.setText(4, pBeans.getExample());
			}
		}
		dialogChanged();
	}
	
}
