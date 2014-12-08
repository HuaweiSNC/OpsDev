package com.huawei.networkos.ops.views;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.huawei.network.opsdev.core.treeview.node.DataFactory;
import com.huawei.network.opsdev.core.treeview.node.DeviceNode;
import com.huawei.network.opsdev.core.treeview.node.OpsHandleNode;
import com.huawei.network.opsdev.core.treeview.node.TreeViewerContentProvider;
import com.huawei.network.opsdev.core.treeview.node.TreeViewerLabelProvider;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.networkos.ops.Activator;
import com.huawei.networkos.ops.views.event.DeviceViewListener;
import com.huawei.tools.xml.config.ConfigPythonServiceManager;
import com.huawei.tools.xml.config.ConfigXmlManager;
import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;

/**
 * DeviceView视图，管理数据源
 * @modify zWX199308
 *
 */

public class DeviceView extends ViewPart implements Observer{
	public DeviceView() {
	}
	public static final String ID = "com.huawei.networkos.ops.views.DeviceView";
	
	private IProject currentIproject;
    private TreeViewer viewer;
    private Tree tree;
    private List<OpsService> opsServices = new ArrayList<OpsService>();
    private ConfigXmlManager configXmlManager;
    private ConfigPythonServiceManager configPythonServiceManager = new ConfigPythonServiceManager();
    private DeviceView deviceView = this;
    private Composite parent ;
    
    private  CCombo comboProject = null;
    /*
     * The content provider class is responsible for
     * providing objects to the view. It can wrap
     * existing objects in adapters or simply return
     * objects as-is. These objects may be sensitive
     * to the current input of the view, or ignore
     * it and always show the same content 
     * (like Task List, for example).
     */
     
    public void getDevice(){
    	
        configXmlManager = new ConfigXmlManager(currentIproject.getLocation().toOSString() + File.separator + ".opsdevice");
        opsServices.addAll(configXmlManager.getDevices());
    }
    
    public IProject getProject(String projectName){
        
    	IProject[]  iProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        for(IProject iProject: iProjects){
            
               if(iProject.getName().equals(projectName))
               {
                   return iProject;
               }
           
        }

        return null;
    }
    
    public void resetProjectList(final Collection<Object> collection)
    {
    	
    	Runnable runnable = new Runnable() {

			public void run()
			{
				try
				{
					comboProject.removeAll();
				    for(Object iProjectObject: collection){
				    	if (iProjectObject instanceof IProject)
				    	{
				    		IProject iProject = (IProject) iProjectObject;
				    		File file = new File(iProject.getLocation().toOSString()+File.separator+".opsdev");
				        	if(file.exists()){
				        		comboProject.add(iProject.getName());
				        	}
				    	}
			         }
				} catch (Exception e)
				{
				   e.printStackTrace();
				}
		 
			}
		};
		Activator.getDefault().getWorkbench().getDisplay().syncExec(runnable);
    }
 

    /**
     * This is a callback that will allow us
     * to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {
    	//首先reload所有已经存在的工程
    	IProject[] iProjectsAll = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    	for (int i = 0; i < iProjectsAll.length; i++)
    	{
    		IProject iproject = iProjectsAll[i];
    		File file = new File(iproject.getLocation().toOSString()+File.separator+".opsdev");
    		if (file.exists()) {
				ProjectConfigXmlManager projectConfigXmlManager = new ProjectConfigXmlManager(iproject.getLocation().toOSString() + File.separator + OpsManagerProjectTool.PROJECT_CONFIG_FILENAME);
	            projectConfigXmlManager.parseFile();
	            String projectTypeTmp = projectConfigXmlManager.getProjectType();
	            OpsManagerProjectTool.reLoadProject(iproject, null, projectTypeTmp);
			}
    	}
    	
    	
    	
    	this.parent = parent;
    	
        parent.setLayout(new GridLayout(2, false));
        
        Label lblNewLabel = new Label(parent, SWT.NONE);
        lblNewLabel.setText("Choose project:");
        
        comboProject = new CCombo(parent, SWT.BORDER);
        GridData gd_comboProject = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
        gd_comboProject.widthHint = 95;
        comboProject.setLayoutData(gd_comboProject);
        comboProject.setEditable(false);
        IProject[]  iProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        
        // 增加工程列表信息
        if (null != iProjects) {
        	for(IProject iProject: iProjects){
            	File file = new File(iProject.getLocation().toOSString()+File.separator+".opsdev");
            	if(file.exists()){
            		comboProject.add(iProject.getName());
            	}
            }
		}
        
        comboProject.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				
			    String selectProjectName = comboProject.getText();
			    if(selectProjectName != null && selectProjectName.length() > 0)
			    {
					currentIproject = getProject(selectProjectName);
					if(currentIproject != null)
					{
						changeTreeView();
					}
			    }
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
			
		});
        
//        getDevice();
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        tree = viewer.getTree();
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gd_table.heightHint = 80;
        tree.setLayoutData(gd_table);
        viewer.setContentProvider(new TreeViewerContentProvider());
        viewer.setLabelProvider(new TreeViewerLabelProvider());

//        viewer.setInput(getViewSite());
        
                // Create the help context id for the viewer's control
        PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "tyrty.viewer");
        
        
        
        final Menu menu = new Menu (parent.getShell(), SWT.POP_UP);
        final MenuItem addMenuItem = new MenuItem (menu, SWT.PUSH);
        addMenuItem.setText("Add");
        addMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				Display display = Display.getDefault();
                AddDeviceWizard addDeviceWizard = new AddDeviceWizard(currentIproject);
                WizardDialog dialog = new WizardDialog(new Shell(display), addDeviceWizard);
//                dialog.setPageSize(400,180);
                dialog.open();
                changeTreeView();
				
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        final MenuItem modyfyMenuItem = new MenuItem (menu, SWT.PUSH);
        modyfyMenuItem.setText("Modify");
        modyfyMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
            	modyfyDevice();
            	changeTreeView();
				
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        final MenuItem deleteMenuItem = new MenuItem (menu, SWT.PUSH);
        deleteMenuItem.setText("Delete");
        deleteMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				delete();
				changeTreeView();
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        final MenuItem configMenuItem = new MenuItem (menu, SWT.PUSH);
        configMenuItem.setText("ConfigDevice");
        configMenuItem.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
            	OpsService opsService = getSelectedOpsService();
            	if(opsService==null){
            		showMessage("You must chose a device!");
            		return;
            	}
            	Display display = Display.getDefault();
            	
				OpsServerUrlHandelConfigWizard configWizard = new OpsServerUrlHandelConfigWizard( currentIproject,opsService,configXmlManager);
                WizardDialog dialog = new WizardDialog(new Shell(display), configWizard);
                dialog.setPageSize(400,400);
                dialog.open();
                changeTreeView();
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        tree.setMenu(menu);
        final Menu handleMenu = new Menu (parent.getShell(), SWT.POP_UP);
        final MenuItem testHandleMenuItem = new MenuItem(handleMenu,SWT.PUSH);
        final MenuItem generateMenuItem = new MenuItem(handleMenu, SWT.PUSH);
        final MenuItem deleteHandleMenuItem  = new MenuItem(handleMenu,SWT.PUSH); 
        generateMenuItem.setText("Generate Service Call");
        generateMenuItem.addSelectionListener(new SelectionListener() {
			

			public void widgetSelected(SelectionEvent e) {
				Display display = Display.getDefault();
				GenerateServuceCallWizard testHandelWizard = new GenerateServuceCallWizard(getSelectedOpsService(),getSelectedOpsServiceUrlHandle(), currentIproject);
	            WizardDialog dialog = new WizardDialog(new Shell(display), testHandelWizard);
	            dialog.setPageSize(600,500);
	            dialog.open();
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        deleteHandleMenuItem.setText("Delete");
        deleteHandleMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				delete();
				changeTreeView();
			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        testHandleMenuItem.setText("Test");
        testHandleMenuItem.addSelectionListener(new SelectionListener() {
			

			public void widgetSelected(SelectionEvent e) {
				Display display = Display.getDefault();
				
				TestHandelWizard testHandelWizard = new TestHandelWizard(opsServices, currentIproject,getSelectedOpsService(),getSelectedOpsServiceUrlHandle());
	            WizardDialog dialog = new WizardDialog(new Shell(display), testHandelWizard);
	            dialog.setPageSize(600,400);
	            dialog.open();

			}
			

			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
        final Display display = parent.getDisplay();
        handleMenu.addMenuListener(new MenuListener() {
		
			public void menuShown(MenuEvent e) {
				 Point p = display.getCursorLocation();
				 p = tree.toControl(p);
				 TreeItem item = tree.getItem(p);
				 if (item == null) {
					 handleMenu.setVisible(false);
		                
		                return;
		         }
				 tree.setSelection(item);
				 TreeSelection selection = (TreeSelection)viewer.getSelection();
				 Object object = selection.getFirstElement();
				 if(object instanceof DeviceNode){
					 handleMenu.setVisible(false);
					 tree.setMenu(menu);
					 menu.setVisible(true);
				 }
				 
				
			}
			
			public void menuHidden(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        menu.addMenuListener(new MenuListener() {
			
			public void menuShown(MenuEvent e) {
				 Point p = display.getCursorLocation();
				 p = tree.toControl(p);
				 TreeItem item = tree.getItem(p);
				 if (item == null) {
		                menu.setVisible(false);
		                
		                return;
		         }
				 tree.setSelection(item);
				 TreeSelection selection = (TreeSelection)viewer.getSelection();
				 Object object = selection.getFirstElement();
				 if(!(object instanceof DeviceNode)){
					 menu.setVisible(false);
					 tree.setMenu(handleMenu);
					 handleMenu.setVisible(true);
				 }
				 
			}
			

			public void menuHidden(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        DeviceViewListener listener = new DeviceViewListener(this);
		OpsManagerProjectTool.addListener(listener);
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(
            viewer.getControl().getShell(),
            "Device",
            message);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }
    public void changeTreeView(){
    	 opsServices.clear();
    	 getDevice();
         viewer.setInput(DataFactory.getDeviceList(opsServices));
         configPythonServiceManager.syncXmlPython(currentIproject, opsServices);
    }
    
    public OpsService getSelectedOpsService(){
    	TreeSelection selection = (TreeSelection) viewer.getSelection();
    	if(selection.getFirstElement() instanceof DeviceNode){
    		
    		return((DeviceNode)selection.getFirstElement()).getOpsService();

    		

    	}
    	if(selection.getFirstElement() instanceof OpsHandleNode){
 	    	OpsHandleNode handleNode = (OpsHandleNode)selection.getFirstElement();
	    	OpsService opsService = (handleNode).getParent();
	    	return opsService;
	    		
    	}
    	return null;
    }
    public OpsServiceUrlHandle getSelectedOpsServiceUrlHandle(){
    	TreeSelection selection = (TreeSelection) viewer.getSelection();
    	if(selection.getFirstElement() instanceof DeviceNode){
    		
    		return null;

    		

    	}
    	if(selection.getFirstElement() instanceof OpsHandleNode){
 	    	OpsHandleNode handleNode = (OpsHandleNode)selection.getFirstElement();

	    	return handleNode.getOpsServiceUrlHandle();
    	}
    	return null;
    }
    
    public void delete(){
    
    	TreeSelection selection = (TreeSelection) viewer.getSelection();
    	if(selection.getFirstElement() instanceof DeviceNode){
    		if(MessageDialog.openConfirm(viewer.getControl().getShell(), "Delete", "Are you sure you will delete this Device")){
    			if(configXmlManager.delectDevice(((DeviceNode)selection.getFirstElement()).getOpsService())){
    				showMessage("delete device success!!");
    				return;
    			}
    			showMessage("delete device false");
    			return;
    		}

    	}
    	if(selection.getFirstElement() instanceof OpsHandleNode){
    		if(MessageDialog.openConfirm(viewer.getControl().getShell(), "Delete", "Are you sure you will delete this Device")){
	    		OpsHandleNode handleNode = (OpsHandleNode)selection.getFirstElement();
	    		OpsService opsService = (handleNode).getParent();
	    		opsService.getHandles().remove(handleNode.getOpsServiceUrlHandle());
	    		if(configXmlManager.modefyDevice(opsService)){
	    			showMessage("delete urlHandle success!!");
	    			return;
	    		}
	    		showMessage("delete urlHandle false!!");
    		}
    	}
    }
    
    public void modyfyDevice(){
    	OpsService opsService =null;
    	TreeSelection selection = (TreeSelection) viewer.getSelection();
    	if(selection.getFirstElement() instanceof DeviceNode){
    	    opsService = ((DeviceNode)selection.getFirstElement()).getOpsService();
    	}
    	if(selection.getFirstElement() instanceof OpsHandleNode){
    		OpsHandleNode handleNode = (OpsHandleNode)selection.getFirstElement();
    		opsService = (handleNode).getParent();
    	}
    	if(opsService!=null){
    		 Display display = Display.getDefault();
             ConfigDeviceWizard configDeviceWizard = new ConfigDeviceWizard(currentIproject,opsService);
             WizardDialog dialog = new WizardDialog(new Shell(display), configDeviceWizard);
//             dialog.setPageSize(400,300);
             dialog.open();
             changeTreeView();
    	}else{
    		showMessage("You must chose an device");
    	}
    	
    	
    }

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
