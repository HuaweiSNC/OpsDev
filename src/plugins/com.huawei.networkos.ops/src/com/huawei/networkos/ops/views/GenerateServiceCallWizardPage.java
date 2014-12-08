package com.huawei.networkos.ops.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import com.huawei.network.opsdev.core.templet.ProjectTempletCreater;
import com.huawei.network.opsdev.core.templet.TempletAutoPythonNode;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
/**
 * 绑定数据源
 *
 */

public class GenerateServiceCallWizardPage extends WizardPage {
	
	private TempletAutoPythonNode templetAutoPythonNode;
	private String templetAutoVmFile;
	private IProject iProject;
	
	/**
	 * Create the wizard.
	 */
	public GenerateServiceCallWizardPage(IProject iProject, TempletAutoPythonNode templetAutoPythonNode, String templetVmFile) {
		super("GenerateServiceCallPage");
		setTitle("Generate Service Call Page");
		setDescription("the GenerateServiceCall templet");
		this.templetAutoPythonNode = templetAutoPythonNode;
		this.templetAutoVmFile = templetVmFile;
		this.iProject = iProject;

	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		String plugid = OpsManagerProjectTool.getProjectCreatePlugId(iProject);
		ProjectTempletCreater creater = new ProjectTempletCreater(plugid);
		final String priviewContent = creater.startPreview(templetAutoPythonNode, templetAutoVmFile).toString();
		
		Label lblPreview = new Label(container, SWT.NONE);
		lblPreview.setText("Preview:");
		StyledText styledText = new StyledText(container, SWT.BORDER|SWT.H_SCROLL);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		styledText.setText(priviewContent);
		
		Button btnCopy = new Button(container, SWT.NONE);
		GridData gd_btnCopy = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnCopy.widthHint = 91;
		btnCopy.setLayoutData(gd_btnCopy);
		btnCopy.setText("Copy");
		btnCopy.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				 Transfer[] dataTypes = new Transfer[] { TextTransfer.getInstance() };
			        Object[] data = new Object[] { priviewContent };

			        Clipboard clipboard = new Clipboard(Display.getCurrent());
			        try {
			            clipboard.setContents(data, dataTypes);
			            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Copying to clipboard information", "Copying success, press ctr+v to paste.");

			        } catch (SWTError e1) {
			            if (e1.code != DND.ERROR_CANNOT_SET_CLIPBOARD) {
			                throw e1;
			            }
			            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error copying to clipboard.", e1.getMessage());
			        } finally {
			            clipboard.dispose();
			        }
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
