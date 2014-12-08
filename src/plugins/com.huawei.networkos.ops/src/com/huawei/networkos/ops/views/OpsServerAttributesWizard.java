package com.huawei.networkos.ops.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;



import com.huawei.network.opsdev.core.templet.UrlPropertiesBean;
import com.huawei.tools.xml.config.ConfigXmlManager;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;
/**
 * 配置数据源时设置相关属性的页面
 *
 */

public class OpsServerAttributesWizard extends Wizard {

	private OpsServerAttributesWizardPage opsServerAttributesWizardPage;
	private IProject iproject;
	private List<UrlPropertiesBean> currentChosedBeands = new ArrayList<UrlPropertiesBean>();

	private OpsServiceUrlHandle handle;
	
	public OpsServerAttributesWizard(IProject iproject,OpsServiceUrlHandle handle) {
		setWindowTitle("Add OpsServerAttributesWizardPage");
		this.iproject = iproject;
		this.handle = handle;
		opsServerAttributesWizardPage = new OpsServerAttributesWizardPage(iproject,handle);
		
	}


	public void addPages() {
		addPage(opsServerAttributesWizardPage);
	}

	
	public boolean performFinish() {
		currentChosedBeands = opsServerAttributesWizardPage.getCurrentChosedBeands();
		handle.getPropertiesHandles().clear();
		for(UrlPropertiesBean bean : currentChosedBeands){
			handle.getPropertiesHandles().put(bean.getName(),bean.getType());
		}
		return true; 
	}
	
	
}
