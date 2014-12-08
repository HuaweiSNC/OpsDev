package com.huawei.networkos.ops.views;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;


public class OpsServerHandelWizard extends Wizard {
	private OpsServiceUrlHandle handle;
	private OpsService opsService;
	private OpsServerHandelPage handelPage;
	private IProject iproject;
	public OpsServerHandelWizard(IProject iproject,OpsService opsService) {
		setWindowTitle("Add OPS Device Handel");
		this.iproject = iproject;
		handelPage = new OpsServerHandelPage(iproject);
		this.opsService = opsService;
	}
	public void addPages() {
		addPage(handelPage);
	}

	public boolean performFinish() {
		handle = handelPage.getOpsServiceUrlHandle();
		opsService.getHandles().add(handle);
		return true;
	}
	public OpsService getOpsService(){
		return opsService;
	}
	public OpsServiceUrlHandle getOpsServiceUrlHandle(){
		return handle;
	}
}
