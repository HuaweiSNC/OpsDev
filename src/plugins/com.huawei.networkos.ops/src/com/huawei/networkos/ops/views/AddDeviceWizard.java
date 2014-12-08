package com.huawei.networkos.ops.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;

import com.huawei.network.opsdev.core.treeview.node.DataFactory;
import com.huawei.tools.xml.config.ConfigXmlManager;
import com.huawei.tools.xml.config.OpsService;



public class AddDeviceWizard extends Wizard {
	private AddDevicePage addDevicePage;
	private IProject iproject;

	public AddDeviceWizard(IProject iproject) {
		setWindowTitle("Add New Device Page");
		addDevicePage = new AddDevicePage();
		this.iproject = iproject;
	}


	public void addPages() {
		addPage(addDevicePage);
		addDevicePage.setIproject(iproject);
	}


	public boolean performFinish() {
		OpsService opsService = addDevicePage.getOpsService();

		ConfigXmlManager configXmlManager = new ConfigXmlManager(iproject.getLocation().toOSString()+File.separator+".opsdevice");
		if(configXmlManager.addOpsServiceToXMl(opsService)){
			return true;
		}
		
		MessageDialog.openError(getShell(), "add opsservice error", "The name is already exist! please check it and enter it again!");
		return false;
	}

}
