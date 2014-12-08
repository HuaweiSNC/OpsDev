package com.huawei.networkos.ops.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.huawei.tools.xml.config.OpsService;

/**
 * 增加Device的页面
 * 
 */
public class AddDevicePage extends WizardPage {
	private Text deviceNameText;
	private Text deviceIdText;
	private Text serverUrlText;
	private Text portText;
	private Text userNameText;
	private Label lblPort;
	private Label lblUsername;
	private Label lblPassword;
	private Label lblXmlversion;
	private Label lblProducttype;
	private Text passwordText;
	private Text xmlVersionText;
	private Text productTypeText;
	private OpsService opsService = new OpsService();
	private Label label;
	private Label label_1;
	private Label label_2;
	private Label label_3;
	private Label label_4;
	private Label lblName;
	private Text nameText;
	// http选择框
	private Label httpLabel;
	private Combo httpCombo;
	// 用于存储当前选择的工程
	private IProject iproject;

	private Label lblTrustStore;
	private Text trustStoreText;
	private Label lblStorePassword;
	private Text storePasswordText;
	private Group configssl;

	public IProject getIproject() {
		return iproject;
	}

	public void setIproject(IProject iproject) {
		this.iproject = iproject;
	}

	public OpsService getOpsService() {
		return opsService;
	}

	public void setOpsService(OpsService opsService) {
		this.opsService = opsService;
	}

	/**
	 * Create the wizard.
	 */
	public AddDevicePage() {
		super("Add New Device Page");
		setTitle("Add A New Device");
		setDescription("Add a new device in the selected project.");
	}

	// /**
	// * get current projec type
	// */
	// public String getProjectType() {
	// ProjectConfigXmlManager projectConfigXmlManager = new
	// ProjectConfigXmlManager(
	// getIproject().getLocation().toOSString() + File.separator
	// + OpsManagerProjectTool.PROJECT_CONFIG_FILENAME);
	// projectConfigXmlManager.parseFile();
	// return projectConfigXmlManager.getProjectType();
	// }

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout gl_container = new GridLayout(1, false);

		container.setLayout(gl_container);

		Group addevice = new Group(container, SWT.SHADOW_IN);
		addevice.setText("properties of the device");
		addevice.setLayout(new GridLayout(5, false));
		GridData gd_configdevice = new GridData(SWT.FILL, SWT.TOP, true, true,
				1, 1);
		gd_configdevice.widthHint = 567;
		gd_configdevice.heightHint = 155;

		addevice.setLayoutData(gd_configdevice);

		lblName = new Label(addevice, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblName.setText("*Name:");

		nameText = new Text(addevice, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				opsService.setName(nameText.getText());
				dialogChanged();
			}
		});
		new Label(addevice, SWT.NONE);

		httpLabel = new Label(addevice, SWT.NONE);
		httpLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		httpLabel.setText("*DeviceProtocol:");

		httpCombo = new Combo(addevice, SWT.READ_ONLY);
		httpCombo.setItems(new String[] { "http", "https" });
		httpCombo.select(0);
		httpCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		httpCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setProtocal(httpCombo.getText());
				dialogChanged();
			}
		});

		Label lblDevicename = new Label(addevice, SWT.NONE);
		lblDevicename.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDevicename.setText("*DeviceName:");

		deviceNameText = new Text(addevice, SWT.BORDER);
		deviceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		deviceNameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				opsService.setDevicename(deviceNameText.getText());
				dialogChanged();
			}
		});
		new Label(addevice, SWT.NONE);

		Label lblDeviceid = new Label(addevice, SWT.NONE);
		lblDeviceid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDeviceid.setText("*DeviceId:");

		deviceIdText = new Text(addevice, SWT.BORDER);
		deviceIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		deviceIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setDeviceId(deviceIdText.getText());
				dialogChanged();
			}
		});

		Label lblServier = new Label(addevice, SWT.NONE);
		lblServier.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblServier.setText("*ServerIP:");

		serverUrlText = new Text(addevice, SWT.BORDER);
		serverUrlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		serverUrlText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				opsService.setServer(serverUrlText.getText());
				dialogChanged();
			}
		});
		new Label(addevice, SWT.NONE);

		lblPort = new Label(addevice, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblPort.setText("*Port:");

		portText = new Text(addevice, SWT.BORDER);
		portText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		portText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setPort(portText.getText());
				dialogChanged();
			}
		});

		lblUsername = new Label(addevice, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblUsername.setText("UserName:");

		userNameText = new Text(addevice, SWT.BORDER);
		userNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		userNameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				opsService.setUserName(userNameText.getText());
				dialogChanged();
			}
		});
		new Label(addevice, SWT.NONE);

		lblPassword = new Label(addevice, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblPassword.setText("Password:");

		passwordText = new Text(addevice, SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		passwordText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setPasswd(passwordText.getText());
				dialogChanged();
			}
		});

		lblXmlversion = new Label(addevice, SWT.NONE);
		lblXmlversion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblXmlversion.setText("*Version:");

		xmlVersionText = new Text(addevice, SWT.BORDER);
		xmlVersionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		xmlVersionText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setXmlVersion(xmlVersionText.getText());
				dialogChanged();
			}
		});
		new Label(addevice, SWT.NONE);

		lblProducttype = new Label(addevice, SWT.NONE);
		lblProducttype.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblProducttype.setText("*ProductType:");

		productTypeText = new Text(addevice, SWT.BORDER);
		productTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		productTypeText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setProductType(productTypeText.getText());
				dialogChanged();
			}
		});

		configssl = new Group(container, SWT.SHADOW_IN);
		configssl.setText("Config SSL");
		configssl.setLayout(new GridLayout(5, false));
		GridData gd_configssl = new GridData(SWT.FILL, SWT.BOTTOM, true, false,
				1, 1);
		gd_configssl.heightHint = 48;
		gd_configssl.widthHint = 568;
		configssl.setLayoutData(gd_configssl);

		lblTrustStore = new Label(configssl, SWT.NONE);
		lblTrustStore.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblTrustStore.setText("TrustStore:");

		trustStoreText = new Text(configssl, SWT.BORDER);
		trustStoreText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		if (null != OpsService.trustStore && !"".equals(OpsService.trustStore)) {
			trustStoreText.setText(OpsService.trustStore);
		}
		trustStoreText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setTrustStore(trustStoreText.getText());
				dialogChanged();
			}
		});

		new Label(configssl, SWT.NONE);

		lblStorePassword = new Label(configssl, SWT.NONE);
		lblStorePassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblStorePassword.setText("StorePassword:");

		storePasswordText = new Text(configssl, SWT.BORDER);
		storePasswordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		if (null != OpsService.storePassword && !"".equals(OpsService.storePassword)) {
			storePasswordText.setText(OpsService.storePassword);
		}
		storePasswordText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				opsService.setStorePassword(storePasswordText.getText());
				dialogChanged();
			}
		});

		dialogChanged();
	}

	private void dialogChanged() {
		if ("http".equals(httpCombo.getText())) {
			configssl.setVisible(false);
//			return;
		}
		if ("https".equals(httpCombo.getText())) {
			configssl.setVisible(true);
//			return;
		}
		if (nameText.getText() == null || nameText.getText().equals("")) {
			updateStatus("The content of *Name can not be empty");
			return;
		}
		if (deviceNameText.getText() == null
				|| deviceNameText.getText().equals("")) {
			updateStatus("The content of *DeviceName can not be empty");
			return;
		}
		if (deviceIdText.getText() == null || deviceIdText.getText().equals("")
				|| !checkIsAllNum(deviceIdText.getText())) {
			updateStatus("The content of *DeviceId can not be empty");
			return;
		}
		if (serverUrlText.getText() == null
				|| serverUrlText.getText().equals("")) {
			updateStatus("The content of *ServerIP can not be empty");
			return;
		}
		if (portText.getText() == null || portText.getText().equals("")
				|| !checkIsAllNum(portText.getText())) {
			updateStatus("The content of *Port can not be empty");
			return;
		}
		if (xmlVersionText.getText() == null
				|| xmlVersionText.getText().equals("")) {
			updateStatus("The content of *Version can not be empty");
			return;
		}
		if (productTypeText.getText() == null
				|| productTypeText.getText().equals("")) {
			updateStatus("The content of *ProductType can not be empty");
			return;
		}
		if ("https".equals(httpCombo.getText())
				&& "".equals(trustStoreText.getText())) {
			updateStatus("The content of TrustStore can not be empty");
			return;
		}
		if ("https".equals(httpCombo.getText())
				&& "".equals(storePasswordText.getText())) {
			updateStatus("The content of StorePassword can not be empty");
			return;
		}
		
		updateStatus(null);
	}

	public boolean checkIsAllNum(String str) {
		for (char cha : str.toCharArray()) {
			if (cha < '0' || cha > '9') {
				return false;
			}
		}
		return true;
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}
