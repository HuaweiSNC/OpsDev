/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.huawei.networkos.ops.python.ui.wizards.project;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.python.pydev.core.log.Log;
import org.python.pydev.plugin.PyStructureConfigHelpers;
import org.python.pydev.plugin.PydevPlugin;
import org.python.pydev.shared_core.callbacks.ICallback;
import org.python.pydev.ui.wizards.gettingstarted.AbstractNewProjectWizard;

import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.TempletVMNode;
import com.huawei.network.opsdev.core.utils.IOTools;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.networkos.ops.python.Messages;
import com.huawei.networkos.ops.wizards.ChoseOpsTempletWizardPage;
import com.huawei.networkos.ops.wizards.IWizardNewProjectNameAndLocationPage;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;
import com.huawei.tools.xml.config.ProjectTempletXmlManager;

/**
 * Python Project creation wizard
 * 
 * <ul>
 * <li>Asks users information about Python project
 * <li>Launches another thread to create Python project. A progress monitor is shown in UI thread
 * </ul>
 * 
 * TODO: Add a checkbox asking should a skeleton of a Python program generated
 * 
 * @author Mikko Ohtamaa
 * @author Fabio Zadrozny
 */
public class PythonProjectWizard extends AbstractNewProjectWizard implements IExecutableExtension {

    /**
     * The current selection.
     */
    protected IStructuredSelection selection;
    //ѡ��ģ��ҳ
    private ChoseOpsTempletWizardPage choseOpsTempletWizardPage;
    //����python����ҳ���ID
    public static final String WIZARD_ID = "org.python.pydev.ui.wizards.project.PythonProjectWizard";
    //�������̵�pluginID
    public static final String PYTHON_PLUGIN_ID = "com.huawei.networkos.ops.python";
    //����python����ҳ
    protected IWizardNewProjectNameAndLocationPage projectPage;
    //������������ҳ��
    protected NewProjectLocationWizardPage newProjectLocationWizardPage;

    Shell shell;

    /** Target project created by this wizard */
    IProject generatedProject;

    /** Exception throw by generator thread */
    Exception creationThreadException;

    private IProject createdProject;
    private IConfigurationElement fConfigElement;
    protected IWorkbench workbench;

    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
    	this.setWindowTitle(Messages.PythonProjectWizard_title);
        this.selection = currentSelection;
        this.workbench = workbench;
        initializeDefaultPageImageDescriptor();
        projectPage = createProjectPage();
        newProjectLocationWizardPage = new NewProjectLocationWizardPage("Setting project properties");
        choseOpsTempletWizardPage = createChoseOpsTempletWizardPage();
    }

    /**
     * Creates the project page.
     */
    protected IWizardNewProjectNameAndLocationPage createProjectPage() {
        return new NewProjectNameAndLocationWizardPage("Setting project properties");
    }
    
    /**
     * Chose templet page
     */
    protected ChoseOpsTempletWizardPage createChoseOpsTempletWizardPage(){
        return new ChoseOpsTempletWizardPage();
    }
    
    /**
     * Add wizard pages to the instance
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(projectPage);//�½�����ҳ
        addProjectReferencePage();//��������ҳ
        addPage(newProjectLocationWizardPage);//·����Ϣ
        addPage(choseOpsTempletWizardPage);//ѡ��ģ��ҳ
    }

    
    public IWizardPage getNextPage(IWizardPage page) {
        IWizardPage nextPage = super.getNextPage(page);
        if(nextPage == choseOpsTempletWizardPage)
        {
            choseOpsTempletWizardPage.resetOpsTempletList(PYTHON_PLUGIN_ID, "python");
        }
        return nextPage;
    }


    /**
     * Creates a new project resource with the entered name.
     * 
     * @return the created project resource, or <code>null</code> if the project was not created
     */
    protected IProject createNewProject(final Object... additionalArgsToConfigProject) {
        // get a project handle
        final IProject newProjectHandle = projectPage.getProjectHandle();

        // get a project descriptor
        IPath defaultPath = Platform.getLocation();
        IPath newPath = newProjectLocationWizardPage.getLocationPath();
        if (defaultPath.equals(newPath)) {
            newPath = null;
        } else {
            //The user entered the path and it's the same as it'd be if he chose the default path.
            IPath withName = defaultPath.append(newProjectHandle.getName());
            if (newPath.toFile().equals(withName.toFile())) {
                newPath = null;
            }
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
        description.setLocation(newPath);

        // update the referenced project if provided
        if (referencePage != null) {
            IProject[] refProjects = referencePage.getReferencedProjects();
            if (refProjects.length > 0) {
                description.setReferencedProjects(refProjects);
            }
        }

        final String projectNatureType = projectPage.getProjectType();
        final String projectType = projectPage.getProjectCreateType();
        
        final String projectInterpreter = projectPage.getProjectInterpreter();
        // define the operation to create a new project
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            protected void execute(IProgressMonitor monitor) throws CoreException {

                createAndConfigProject(newProjectHandle, description, projectNatureType, projectType, projectInterpreter, monitor,
                        additionalArgsToConfigProject);
            }
        };

        // run the operation to create a new project
        try {
            getContainer().run(true, true, op);
        } catch (InterruptedException e) {
            return null;
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof CoreException) {
                if (((CoreException) t).getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
                    MessageDialog.openError(getShell(), "Unable to create project",
                            "Another project with the same name (and different case) already exists.");
                } else {
                    ErrorDialog
                            .openError(getShell(), "Unable to create project", null, ((CoreException) t).getStatus());
                }
            } else {
                // Unexpected runtime exceptions and errors may still occur.
                Log.log(IStatus.ERROR, t.toString(), t);
                MessageDialog.openError(getShell(), "Unable to create project", t.getMessage());
            }
            return null;
        }

        return newProjectHandle;
    }

    /**
     * This method can be overridden to provide a custom creation of the project.
     * 
     * It should create the project, configure the folders in the pythonpath (source folders and external folders
     * if applicable), set the project type and project interpreter.
     */
    protected void createAndConfigProject(final IProject newProjectHandle, final IProjectDescription description,
           final String projectNatureType, final String projectType, final String projectInterpreter, IProgressMonitor monitor,
            Object... additionalArgsToConfigProject) throws CoreException {
        
        
        
        // �½�����ģ����
        ProjectTempletXmlManager templetXmlManager  = new ProjectTempletXmlManager(PYTHON_PLUGIN_ID, projectType);
        if(choseOpsTempletWizardPage.getSelectedTempletName() != null){
            
            // �����û�ѡ���ģ������
            templetXmlManager.setSelectedElement(choseOpsTempletWizardPage.getSelectedTempletName());
        }
        
        // ��ȡ��Ҫ���������е��ļ��У�MAP<�ļ�������,�Ƿ���ispackage>
        final Map<String, String> folders = templetXmlManager.getBuilderFolder();
        
        // ��ȡ���õ�ops2.0��Դ���ڵ�Ŀ¼
        final List<String> resourse= templetXmlManager.getBuilderResource();
        
        // ��ȡ��Ҫ��ģ���ļ�velocity�������ɵĹ���ģ��
        List<TempletVMNode> vm = templetXmlManager.getBuilderVm();
        
        // ��ȡ��ǰ��������Ҫ�������ļ�
        Map<String, String> files = templetXmlManager.getBuliderFile();
        
        ICallback<List<IContainer>, IProject> getSourceFolderHandlesCallback = new ICallback<List<IContainer>, IProject>() {

            public List<IContainer> call(IProject projectHandle) {
                final int sourceFolderConfigurationStyle = projectPage.getSourceFolderConfigurationStyle();
                List<IContainer> ret = new ArrayList<IContainer>();
                switch (sourceFolderConfigurationStyle) {

                    case IWizardNewProjectNameAndLocationPage.PYDEV_NEW_PROJECT_CREATE_PROJECT_AS_SRC_FOLDER:
                        //if the user hasn't selected to create a source folder, use the project itself for that.
                        ret = new ArrayList<IContainer>();
                        ret.add(projectHandle);
                        return ret;

                    case IWizardNewProjectNameAndLocationPage.PYDEV_NEW_PROJECT_NO_PYTHONPATH:
                        return new ArrayList<IContainer>();

                    default:
                        IContainer src = projectHandle.getFolder("src");
                        ret = new ArrayList<IContainer>();
                        ret.add(src);
                        if(folders!=null){
                            for(Entry<String,String> entry : folders.entrySet()){
                                if("true".equals(entry.getValue())){
                                    IContainer folder = projectHandle.getFolder(entry.getKey());
                                    ret.add(folder);
                                }
                            }
                        }
                        return ret;
                }
            }
        };
  
        //old pydev version 3.7
        //PyStructureConfigHelpers.createPydevProject(description, newProjectHandle, monitor, projectNatureType,
        //        projectInterpreter, getSourceFolderHandlesCallback, null);
        
        //new pydev version 3.8.2
        PyStructureConfigHelpers.createPydevProject(description, newProjectHandle, monitor, projectNatureType, projectInterpreter, getSourceFolderHandlesCallback, null, null);
  
        ProjectTempletManager templetManager = new ProjectTempletManager(PYTHON_PLUGIN_ID);
        templetManager.setTempletXmlManager(templetXmlManager);
        createTempletInProject(templetXmlManager, templetManager, vm, projectPage.getMainFunctionName() , newProjectHandle,projectType);
        createFileInProject(files, newProjectHandle);
        createFolder(folders,newProjectHandle);
        newProjectHandle.refreshLocal(0, monitor);
        //�������̵�ģ��xml�ļ�·��
        String templetXmlPath = PathTools.getTempletFilePath(PYTHON_PLUGIN_ID);
        //�ڵ�ǰ���̴洢�������̵�pluginID
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_CREATE_PLUGID, PYTHON_PLUGIN_ID);
        
        //�ڵ�ǰ���̴洢����ģ��·��
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_TEMPLET_XML_PATH, templetXmlPath);
      
        ProjectConfigXmlManager projectConfigXmlManager = new ProjectConfigXmlManager(newProjectHandle.getLocation().toOSString()+File.separator+OpsManagerProjectTool.PROJECT_CONFIG_FILENAME);
        String opspath = newProjectLocationWizardPage.getOpsCustomLocationFieldPath().toOSString();
        String schemaApiPath = opspath + File.separator+".." + File.separator+  "document";
        projectConfigXmlManager.initXmlConfig(opspath, newProjectLocationWizardPage.getSchemaPath(), schemaApiPath, projectType);
        projectConfigXmlManager.setProjectMonitorState("off");
        Boolean isnetworkMonitor = projectConfigXmlManager.isMonitorTrunOn();
        projectConfigXmlManager.configOPSPath(monitor, newProjectHandle);
        
        // �ڹ������洢schemafile�����
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.SCHEMAFILE_CONFIG, templetManager);
        
        // �ڹ������洢��ǰ���̵������ļ�
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_CONFIG, projectConfigXmlManager);
      
        // �ڹ������洢��ǰ���̵������ļ�
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_TEMPLET, templetXmlManager);
 
        // �ڹ������洢��ǰ���̵������ļ�
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_TYPE, projectType);
 
        // �ڵ�ǰ�������洢�������״̬
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_MONITOR_STATE, isnetworkMonitor);
        
 
    }

    public void createFileInProject(Map<String,String> files, IProject newProjectHandle){
        if(files!=null){
            String projectPath = newProjectHandle.getLocation().toFile().getAbsolutePath();
            for(Entry<String ,String> entry : files.entrySet()){
                IOTools.copyFile(PathTools.getPlugPath(PYTHON_PLUGIN_ID)+entry.getKey(),projectPath+File.separator+entry.getValue());
            }
        }
        
    }
    
    public void createFolder(Map<String,String> folders, IProject newProjectHandle){
        if(folders!=null){
            for(Entry<String,String> entry : folders.entrySet()){
                if("false".equals(entry.getValue())){
                    String projectPath = newProjectHandle.getLocation().toFile().getAbsolutePath();
                    File file = new File(projectPath+File.separator+entry.getKey());
                    file.mkdir();
                }
            }
        }
        
    }
    
    public void createTempletInProject(ProjectTempletXmlManager templetXmlManager,ProjectTempletManager manager, List<TempletVMNode> lstVm , String mainFunction,IProject newProjectHandle,String projectType){
        
        if(null == lstVm){
           return;
        }
   
        String projectPath = newProjectHandle.getLocation().toFile().getAbsolutePath();
        String vmfile = ""; 
        String outfile = "";
        for(TempletVMNode vmnode: lstVm){
            
            vmfile = PathTools.getPlugPath(PYTHON_PLUGIN_ID) + File.separator+vmnode.getVmFileName();
            outfile = projectPath + File.separator + vmnode.getOuturl();
            if(vmnode.getVmName().contains("apiResourceTemplet")){
                
                // ��ȡ�û�ѡ���ģ��ʾ��
                manager.addFile(newProjectLocationWizardPage.getOpsCustomLocationFieldPath().toOSString() + File.separator + vmnode.getExampleapifile());
                manager.createTempletByUrl(vmnode.getExampleapi(), outfile, vmfile,projectType,PYTHON_PLUGIN_ID);
                continue;
            }
            // ��ȡ�û���mainfunction����
            if(vmnode.getVmName().contains("mainfunction")){
                manager.createMainFunVm(mainFunction, outfile, vmfile, PYTHON_PLUGIN_ID);
                continue;
            }
            manager.createOtherVm(outfile, vmfile, null, PYTHON_PLUGIN_ID);
        }
    }
    
    
    /**
     * The user clicked Finish button
     * 
     * Launches another thread to create Python project. A progress monitor is shown in the UI thread.
     */
    public boolean performFinish() {
        createdProject = createNewProject();
        // Switch to default perspective (will ask before changing)
        BasicNewProjectResourceWizard.updatePerspective(fConfigElement);

        return true;
    }

    
    public IProject getCreatedProject() {
 
        return createdProject;
    }

    /**
     * Set Python logo to top bar0
     */
    protected void initializeDefaultPageImageDescriptor() {
        ImageDescriptor desc = PydevPlugin
                .imageDescriptorFromPlugin(PydevPlugin.getPluginID(), "icons/python_logo.png");
        setDefaultPageImageDescriptor(desc);
    }

    public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
            throws CoreException {
        this.fConfigElement = config;
    }
}
