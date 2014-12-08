package com.huawei.networkos.ops.views;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;

public class TestHandelWizard extends Wizard {
	private TestHandelPage handelPage;
	public TestHandelWizard(List<OpsService> opsServices, IProject iProject,OpsService currentOpsService,OpsServiceUrlHandle currentHandle) {
		setWindowTitle("Test Device Handle Page");
		handelPage = new TestHandelPage(opsServices, iProject,currentOpsService,currentHandle);
	}


	public void addPages() {
		addPage(handelPage);
	}


	public boolean performFinish() {
		handelPage.dispose();
		return true;
	}

}
