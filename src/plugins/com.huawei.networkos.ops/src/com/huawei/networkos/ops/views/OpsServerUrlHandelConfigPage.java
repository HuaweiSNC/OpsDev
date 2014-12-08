package com.huawei.networkos.ops.views;

import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TableColumn;


import com.huawei.tools.xml.config.OpsService ;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
/**
 * 配置数据源页面
 *
 */
public class OpsServerUrlHandelConfigPage extends WizardPage {
	private Table operationTable;
	private Table parameterTable;
	private Text urlText;
	private Composite attributescontainer;
	private CLabel urloperationLabel;
	private Button yesRadioButton;
	private Button noRadioButton;
	private CLabel basicurlLabel;
	private CLabel operationLabel;
	private Button operationaddButton;
	private Button operationdeleteButton;
	private CLabel parameterLabel;
	private OpsService opsService;
	private Button btnConfig;
	private TableColumn tblclmnValue;
	private IProject iProject;
	private OpsServerUrlHandelConfigPage configPage = this;
	
	/**
	 * Create the wizard.
	 */
	public OpsServerUrlHandelConfigPage(IProject iProject,OpsService opsService) {
		super("Attributes Page");
		setTitle("Configure http service");
		setDescription("Specify the operation names and URLs, any operation parameters, and the service name.");
		this.iProject = iProject;
		this.opsService = opsService;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		attributescontainer = new Composite(parent, SWT.NULL);

		setControl(attributescontainer);
		attributescontainer.setLayout(new GridLayout(6, false));
		
		urloperationLabel = new CLabel(attributescontainer, SWT.NONE);
		GridData gd_urloperationLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 4, 1);
		gd_urloperationLabel.widthHint = 401;
		urloperationLabel.setLayoutData(gd_urloperationLabel);
		urloperationLabel.setText("Do you want to use a base URL as a prefix for all operation URLs?");
		
		yesRadioButton = new Button(attributescontainer, SWT.RADIO | SWT.RIGHT);
		yesRadioButton.setText("Yes");
		yesRadioButton.addSelectionListener(new SelectionListener() {
			
			
			public void widgetSelected(SelectionEvent e) {
				if(yesRadioButton.getSelection()){
					noRadioButton.setSelection(false);
					urlText.setText("http://"+opsService.getServer()+":"+opsService.getPort());
					basicurlLabel.setVisible(true);
					urlText.setVisible(true);
				}
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		noRadioButton = new Button(attributescontainer, SWT.RADIO);
		noRadioButton.setText("No");
		noRadioButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if(noRadioButton.getSelection()){
					yesRadioButton.setSelection(false);
					basicurlLabel.setVisible(false);
					urlText.setVisible(false);
				}
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		basicurlLabel = new CLabel(attributescontainer, SWT.NONE);
		basicurlLabel.setText("Basic URL:");
		
		urlText = new Text(attributescontainer, SWT.BORDER);
		urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		operationLabel = new CLabel(attributescontainer, SWT.NONE);
		operationLabel.setText("Operation:");
		new Label(attributescontainer, SWT.NONE);
		new Label(attributescontainer, SWT.NONE);
		new Label(attributescontainer, SWT.NONE);
		
		operationaddButton = new Button(attributescontainer, SWT.NONE);
		GridData gd_operationaddButton = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_operationaddButton.widthHint = 78;
		operationaddButton.setLayoutData(gd_operationaddButton);
		operationaddButton.addSelectionListener(new SelectionListener() {
			
			
			public void widgetSelected(SelectionEvent e) {
				Display display = Display.getDefault();
				OpsServerHandelWizard opsServerHandelWizard = new OpsServerHandelWizard( iProject,opsService);
                WizardDialog dialog = new WizardDialog(new Shell(display), opsServerHandelWizard);
                dialog.setPageSize(400,400);
                dialog.open();
                setOperationTable();
				
			}
			
		
			public void widgetDefaultSelected(SelectionEvent e) {

				
			}
		});
		operationaddButton.setText("Add");
		
		operationdeleteButton = new Button(attributescontainer, SWT.NONE);
		GridData gd_operationdeleteButton = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_operationdeleteButton.widthHint = 78;
		operationdeleteButton.setLayoutData(gd_operationdeleteButton);
		operationdeleteButton.setText("Delete");
		operationdeleteButton.addSelectionListener(new SelectionListener() {
			

			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openConfirm(configPage.getShell(),"Delete Warning","Are you sure to delete this device!")){
					deleteDevice();
				}
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {

				
			}
		});
		gd_operationdeleteButton.widthHint = 78;
		operationTable = new Table(attributescontainer, SWT.BORDER | SWT.FULL_SELECTION);
		operationTable.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				OpsServiceUrlHandle handle = getOpsServiceUrlHandle();
				if(handle==null){
					return;
				}
				Display display = Display.getDefault();
				OpsServerAttributesWizard addOpsServerAttributesWizard = new OpsServerAttributesWizard( iProject,handle);
                WizardDialog dialog = new WizardDialog(new Shell(display), addOpsServerAttributesWizard);
                dialog.setPageSize(400,400);
                dialog.open();
                setParameterTable();
			}
		});
		GridData gd_operationTable = new GridData(SWT.FILL, SWT.FILL, true, true, 6, 2);
		gd_operationTable.heightHint = 37;
		operationTable.setLayoutData(gd_operationTable);
		operationTable.setHeaderVisible(true);
		operationTable.setLinesVisible(true);
		operationTable.addSelectionListener(new SelectionListener() {
			

			public void widgetSelected(SelectionEvent e) {
				setParameterTable();
			}


			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
				TableColumn tblclmnNewColumn = new TableColumn(operationTable, SWT.NONE);
				tblclmnNewColumn.setWidth(71);
				tblclmnNewColumn.setText("Name");
				
				TableColumn tblclmnNewColumn_1 = new TableColumn(operationTable, SWT.NONE);
				tblclmnNewColumn_1.setWidth(85);
				tblclmnNewColumn_1.setText("Method");
				
				
				
				TableColumn tblclmnNewColumn_3 = new TableColumn(operationTable, SWT.NONE);
				tblclmnNewColumn_3.setWidth(387);
				tblclmnNewColumn_3.setText("URL");
		
		parameterLabel = new CLabel(attributescontainer, SWT.NONE);
		parameterLabel.setText("Parameter:");
		new Label(attributescontainer, SWT.NONE);
		new Label(attributescontainer, SWT.NONE);
		new Label(attributescontainer, SWT.NONE);
		
		btnConfig = new Button(attributescontainer, SWT.NONE);
		btnConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnConfig.setText("Config");
		btnConfig.addSelectionListener(new SelectionListener() {
			
			
			public void widgetSelected(SelectionEvent e) {
				OpsServiceUrlHandle handle = getOpsServiceUrlHandle();
				if(handle==null){
					showMessage("You must chose an handle!");
					return;
				}
				Display display = Display.getDefault();
				OpsServerAttributesWizard addOpsServerAttributesWizard = new OpsServerAttributesWizard( iProject,handle);
                WizardDialog dialog = new WizardDialog(new Shell(display), addOpsServerAttributesWizard);
                dialog.setPageSize(400,400);
                dialog.open();
                setParameterTable();
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}
		});
		new Label(attributescontainer, SWT.NONE);
		
		parameterTable = new Table(attributescontainer, SWT.BORDER | SWT.FULL_SELECTION);
		parameterTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		parameterTable.setHeaderVisible(true);
		parameterTable.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn_4 = new TableColumn(parameterTable, SWT.NONE);
		tblclmnNewColumn_4.setWidth(264);
		tblclmnNewColumn_4.setText("Name");
		
		tblclmnValue = new TableColumn(parameterTable, SWT.NONE);
		tblclmnValue.setWidth(295);
		tblclmnValue.setText("Type");
		setOperationTable();
	}
	
	public OpsServiceUrlHandle getOpsServiceUrlHandle(){
		if(opsService!=null){
			int x  =  operationTable.getSelectionIndex();
			if(x!=-1){
				return opsService.getHandles().get(x);
			}
		}
		return null;
	}
	
	public void setOperationTable(){
		operationTable.removeAll();
		for(OpsServiceUrlHandle handel:opsService.getHandles()){
			TableItem item  = new TableItem(operationTable, SWT.NULL);
			item.setText(new String[]{handel.getName(),handel.getHandle(),handel.getApiUrl()});
 
			
		}
	}
	
	public void setParameterTable(){
		parameterTable.removeAll();
		OpsServiceUrlHandle handle = getOpsServiceUrlHandle();
		if(handle.getPropertiesHandles()!=null){
			for(Entry<String, String> entry:handle.getPropertiesHandles().entrySet()){
				TableItem item  = new TableItem(parameterTable, SWT.NULL);
				item.setText(new String[]{entry.getKey(),entry.getValue()});
			}
		}
	}
	
	public void deleteDevice(){
		if(operationTable.getSelectionIndex()==-1){
			MessageDialog.openWarning(getShell(), "Delete Error", "You must chose an device handle to delete it");
			return;
		}
		opsService.getHandles().remove(operationTable.getSelectionIndex());
		setOperationTable();
	}
	public void showMessage(String message){
		MessageDialog.openInformation(getShell(), "Message", message);
	}
	public OpsService getOpsService(){
		return opsService;
	}
	
}
