package com.huawei.network.opsdev.wizard;

import java.io.File;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;

/**
 *  
 * @author qWX182698
 *
 */
public class LoadFileDialog extends Dialog {
	private ProjectTempletManager manager;
	private java.util.List<String> files;
	private IProject iProject;

	private String schemaFilePath = "";
	private int count =0;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LoadFileDialog(Shell parentShell,ProjectTempletManager manager,java.util.List<String> files,IProject iProject) {
		super(parentShell);
		this.manager = manager;
		this.files= files;
		this.iProject = iProject;
	}

	
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	
	protected Control createDialogArea(Composite parent) {
	    
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(4, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		getShell().setToolTipText("Load Files");
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("Loading huawei schema Files , waitting...");
		
		final Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText("");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		ProjectConfigXmlManager configXmlManager = OpsManagerProjectTool.getConfigManager(iProject);
		final List<String> userOpsPath = configXmlManager.getUserSchemaFiles();
		 String filePath = configXmlManager.getSchemaRootPath();
		
		File opspathFile = new File(filePath); 
		this.schemaFilePath = opspathFile.getAbsolutePath();
		
		final ProgressBar progressBar = new ProgressBar(container, SWT.NONE);
		GridData gd_progressBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_progressBar.heightHint = 14;
		gd_progressBar.widthHint = 195;
		progressBar.setLayoutData(gd_progressBar);
		new Label(container, SWT.NONE);
		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("");
		getShell().layout();
		manager.clearUserManager();
		getShell().getDisplay().asyncExec(new Runnable(){
		  
		   public void run()
		   {
		    for(int i = 0; i < files.size(); i++)
		    {
		    	progressBar.setSelection(100*i/files.size());

		    	if(files.get(i).startsWith(schemaFilePath)){
		    		manager.addFile(files.get(i));
		    	}else{
		    		 manager.addUserFile(files.get(i));
		    	}
		    	if(count<files.size()-1){
		    		count++;
		    	}
		    	if(i==files.size()-1){
		    		close();
		    	}
		    }
		   }
		  });
		
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	
	protected void createButtonsForButtonBar(Composite parent) {

		
	}
	/**
	 * Return the initial size of the dialog.
	 */
	
	protected Point getInitialSize() {
		
		return new Point(540, 157);
	}

	
	
}
