package com.huawei.networkos.ops.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.huawei.tools.xml.config.OpsService;
import org.eclipse.swt.widgets.Button;

public class DeviceDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DeviceDialog(Shell parent, int style,OpsService opsService ,String dialogText) {
		super(parent, style);
		setText(dialogText);
		if(opsService==null){
			this.opsService = new OpsService();
		}else{
			this.opsService = opsService;
		}
		
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public OpsService open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return opsService;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 367);
		shell.setText(getText());
		shell.setLayout(new GridLayout(4, false));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label server = new Label(shell, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 41;
		server.setLayoutData(gd_lblNewLabel);
		server.setText("server:");
		
		serverText = new Text(shell, SWT.BORDER);
		serverText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label port = new Label(shell, SWT.NONE);
		GridData gd_port = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_port.widthHint = 35;
		port.setLayoutData(gd_port);
		port.setText("port:");
		
		portText = new Text(shell, SWT.BORDER);
		portText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label userName = new Label(shell, SWT.NONE);
		userName.setText("userName:");
		
		userNameTest = new Text(shell, SWT.BORDER);
		userNameTest.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label deviceName = new Label(shell, SWT.NONE);
		deviceName.setText("deviceName:");
		
		deviceNameTest = new Text(shell, SWT.BORDER);
		deviceNameTest.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label passwd = new Label(shell, SWT.NONE);
		passwd.setText("passwd:");
		
		passwdText = new Text(shell, SWT.BORDER);
		passwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label xxx = new Label(shell, SWT.NONE);
		xxx.setText("");
		new Label(shell, SWT.NONE);
		
		Label deviceId = new Label(shell, SWT.NONE);
		deviceId.setText("deviceId:");
		
		deviceIdText = new Text(shell, SWT.BORDER);
		deviceIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label xmlVersion = new Label(shell, SWT.NONE);
		xmlVersion.setText("xmlVersion:");
		
		xmlVersionText = new Text(shell, SWT.BORDER);
		xmlVersionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label productType = new Label(shell, SWT.NONE);
		productType.setText("productTypeText:");
		
		productTypeText = new Text(shell, SWT.BORDER);
		productTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button ok = new Button(shell, SWT.NONE);
		ok.setText("    Ok   ");
		ok.addSelectionListener(new SelectionListener() {
			
			
			public void widgetSelected(SelectionEvent e) {
				setOpsService();
				shell.close();
			}
			
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
				
			}
		});
		
		Button cancle = new Button(shell, SWT.NONE);
		cancle.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		cancle.setText("Cancle");
		new Label(shell, SWT.NONE);
		cancle.addSelectionListener(new SelectionListener() {
			
			
			public void widgetSelected(SelectionEvent e) {
				opsService = null;
				shell.close();
			}
			
			
			public void widgetDefaultSelected(SelectionEvent e) {
				opsService = null;
				
			}
		});
		
		if(!opsService.equals(new OpsService())){
			deviceNameTest.setText(opsService.getDevicename());
			deviceIdText.setText(opsService.getDeviceId());
			portText.setText(opsService.getPort());
			userNameTest.setText(opsService.getUserName());
			passwdText.setText(opsService.getPasswd());
			serverText.setText(opsService.getServer());
			xmlVersionText.setText(opsService.getXmlVersion());
			productTypeText.setText(opsService.getProductType());
		}
	}
	
	private Text serverText;
	private Text portText;
	private Text userNameTest;
	private Text deviceNameTest;
	private Text passwdText;
	private Text deviceIdText;
	private Text xmlVersionText;
	private Text productTypeText;
	private OpsService opsService;

	public void setOpsService(){
		opsService = new OpsService();
		opsService.setDevicename(deviceNameTest.getText());
		opsService.setDeviceId(deviceIdText.getText());
		opsService.setPasswd(passwdText.getText());
		opsService.setPort(portText.getText());
		opsService.setProductType(productTypeText.getText());
		opsService.setServer(serverText.getText());
		opsService.setUserName(userNameTest.getText());
		opsService.setXmlVersion(xmlVersionText.getText());
	}
	
	
	
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	
	
	
	
	

}
