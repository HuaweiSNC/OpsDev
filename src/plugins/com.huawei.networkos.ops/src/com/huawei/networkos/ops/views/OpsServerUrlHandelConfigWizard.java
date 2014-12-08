package com.huawei.networkos.ops.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import com.huawei.network.opsdev.core.utils.ConsoleFactory;
import com.huawei.tools.xml.config.ConfigXmlManager;
import com.huawei.tools.xml.config.OpsService;

public class OpsServerUrlHandelConfigWizard extends Wizard {
	OpsServerUrlHandelConfigPage configPage;
	private ConfigXmlManager configXmlManager;
	private OpsService opsService;
	public OpsServerUrlHandelConfigWizard(IProject iProject,OpsService opsService,ConfigXmlManager configXmlManager) {
		setWindowTitle("Attributes Page");
		configPage = new OpsServerUrlHandelConfigPage(iProject,opsService);
		this.configXmlManager = configXmlManager;
		this.opsService = opsService;
	}

	
	public void addPages() {
		addPage(configPage);
	}

	public boolean performFinish() {
		System.out.println(configPage.getOpsService().getHandles().get(0).getApiUrl());
		ConsoleFactory.printToConsole(configPage.getOpsService().getHandles().get(0).getApiUrl(),true);
		if(configXmlManager.modefyDevice(configPage.getOpsService())){
			return true;
		}
		return false;
	}

}
