package com.huawei.networkos.ops.wizards;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.SWTResourceManager;
import com.huawei.tools.xml.config.ProjectTempletXmlManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;



public class ChoseOpsTempletWizardPage extends WizardPage {
    
    private ProjectTempletXmlManager projectTempletXmlManager = null;
    private String selectedTempletName;
    private List guideList;
    private StyledText guidestyledText;
   // private IWizardNewProjectNameAndLocationPage projectPage;
    private Map<String, String> templetNameDescMap = new LinkedHashMap<String, String>();
    
	/**
	 * Create the wizard.
	 */
	public ChoseOpsTempletWizardPage() {
		super("wizardPage");
		setTitle("Guide Page ");
		setDescription("Select a sample project.");
	}

 
	
	/***
	 * Reset ops templet list
	 */
	public void resetOpsTempletList(String plugid , String projectType)
	{
 
	    templetNameDescMap.clear();
	   // final String projectType = projectPage.getProjectCreateType();
	    if(null == projectTempletXmlManager)
	    {
	        projectTempletXmlManager = new ProjectTempletXmlManager(plugid, projectType);
	    }
	   
	    templetNameDescMap.putAll(projectTempletXmlManager.getTempletList());
	    guideList.removeAll();
	    for(String str : templetNameDescMap.keySet()){
            guideList.add(str);
        } 
	    
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite guide) {
		Composite guidecontainer = new Composite(guide, SWT.NULL);

		setControl(guidecontainer);
	
		//final Map<String, String> templetMap = projectTempletXmlManagermanager.getTempletList();
		guidecontainer.setLayout(new GridLayout(2, false));
		
		CLabel sampleNewLabel = new CLabel(guidecontainer, SWT.NONE);
		sampleNewLabel.setText("select templt for sample project:");
		
		CLabel descriptionNewLabel = new CLabel(guidecontainer, SWT.NONE);
		descriptionNewLabel.setText("description:");
	    
		guideList = new List(guidecontainer, SWT.BORDER);
		GridData gd_guideList = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_guideList.heightHint = 269;
		gd_guideList.widthHint = 237;
		guideList.setLayoutData(gd_guideList);

		guideList.addSelectionListener(new SelectionListener() {
            
            
            public void widgetSelected(SelectionEvent e) {
                
                selectedTempletName = guideList.getSelection()[0];
                guidestyledText.setText(templetNameDescMap.get(selectedTempletName));
                selectedTempletName =guideList.getSelection()[0]; 
                dialogChanged();
            }
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                
            }
        });
		guidestyledText = new StyledText(guidecontainer, SWT.BORDER | SWT.FULL_SELECTION | SWT.READ_ONLY | SWT.WRAP);
		GridData gd_guidestyledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_guidestyledText.widthHint = 320;
		gd_guidestyledText.heightHint = 268;
		guidestyledText.setLayoutData(gd_guidestyledText);
		guidecontainer.setTabList(new Control[]{guidestyledText, guideList});
		dialogChanged();
	}
	
	private void dialogChanged() {
        
        if(guidestyledText.getText()==null || guidestyledText.equals("")){
            updateStatus("You must chose a templet");
            return;
        }
        
        
        updateStatus(null);
    }
    
	public String getSelectedTempletName(){
	    return selectedTempletName;
	}
	
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
}
