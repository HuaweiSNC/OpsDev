package com.huawei.networkos.ops.views;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import com.huawei.tools.xml.config.ConfigXmlManager;
import com.huawei.tools.xml.config.OpsService;



public class ConfigDeviceWizard extends Wizard {
	private ConfigDevicePage configDevicepage;
	private IProject iproject;
	public ConfigDeviceWizard(IProject iproject,OpsService opsService) {
		setWindowTitle("Modify Device Page");
		this.iproject = iproject;
		configDevicepage = new ConfigDevicePage(opsService);
		
	}


	public void addPages() {
		addPage(configDevicepage);
		configDevicepage.setIproject(iproject);
	}


	public boolean performFinish() {
		OpsService opsService = configDevicepage.getOpsService();
		ConfigXmlManager configXmlManager = new ConfigXmlManager(iproject.getLocation().toOSString()+File.separator+".opsdevice");
		if(configXmlManager.modefyDevice(opsService)){
			return true;
		}
		MessageDialog.openError(getShell(), "modyfy opsservice error", "we did not modyfy opsservice success!");
		return false;
	}

}
