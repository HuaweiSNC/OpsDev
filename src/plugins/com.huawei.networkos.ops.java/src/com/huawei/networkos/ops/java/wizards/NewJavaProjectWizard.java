package com.huawei.networkos.ops.java.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.TempletVMNode;
import com.huawei.network.opsdev.core.utils.IOTools;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.networkos.ops.java.Activator;
import com.huawei.networkos.ops.wizards.ChoseOpsTempletWizardPage;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;
import com.huawei.tools.xml.config.ProjectTempletXmlManager;

/**
 * Wizard that create the new project
 * �½�����ҳ�����:java����ҳ�桢����ģ��ѡ��ҳ�桢����·������ҳ��
 * add jar
 * create packages
 * create and config the project
 * @author zWX199308
 *
 */
public class NewJavaProjectWizard extends DesignerJavaProjectWizard {
	  private NewJavaProjectWizardPageOne fFirstPage;
	  private NewJavaProjectWizardPageTwo fSecondPage;
	  private NewProjectNameAndLocationWizardPage newProjectNameAndLocationWizardPage;
	  private ChoseOpsTempletWizardPage choseOpsTempletWizardPage;
	  private String projectType = "java";
	  public static final String JAVA_PLUGIN_ID = Activator.PLUGIN_ID;
	
    public NewJavaProjectWizard() {
    	fFirstPage = getfFirstPage();
    	fSecondPage = getfSecondPage();
    	newProjectNameAndLocationWizardPage = getNewProjectNameAndLocationWizardPage();
    	choseOpsTempletWizardPage = getChoseOpsTempletWizardPage();
    }
    
    //���ѡ�񹤳�ģ��ҳ����ȡѡ��Ĺ���ģ��
    public IWizardPage getNextPage(IWizardPage page){
    	IWizardPage nextPage = super.getNextPage(page);
    	if(nextPage == choseOpsTempletWizardPage){
    	    
    		choseOpsTempletWizardPage.resetOpsTempletList(JAVA_PLUGIN_ID, "java");
    		return choseOpsTempletWizardPage;
    	}
    	return nextPage;
    }
    
    public boolean canFinish(){
    	if(getfFirstPage().isPageComplete()){
    		return true;
    	}
    	return super.canFinish();
    }
    
    @Override
    public boolean performFinish() {
    	boolean result = super.performFinish();
        if (result) {
            createNewJavaProject();
        }
        return result;
    }
    
    //����java����
    private void createNewJavaProject(){
    	final IJavaProject javaProject = getCreatedElement();
    	final IProject newProjectHandle = javaProject.getProject();
    	final String projectType = "java";
    	  //1������������
    	  createPackages(getProjectName());
    	  //2�������������ļ������ù���
    	  WorkspaceModifyOperation op = new WorkspaceModifyOperation(){
    		  protected void execute(IProgressMonitor monitor) throws CoreException{
    			  createAndConfigProject( javaProject, newProjectHandle,projectType,monitor);
    		  }
    	  };
    	  // run the operation to create a new project
    	  try {
              getContainer().run(true, true, op);
          } catch (InterruptedException e) {
        	  e.printStackTrace();
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
                  MessageDialog.openError(getShell(), "Unable to create project", t.getMessage());
              }
          }
    	
    }
    
    //��������jar��
    private void addRequiredLibraries(final IJavaProject javaProject) throws Exception {

    	String librootpath = javaProject.getProject().getLocation().toOSString();
    	String path =  librootpath + File.separator + "lib";
    	String projectName = javaProject.getProject().getName();
    	File jarFile = new File(path);
    	
    	File[] jarFileList = jarFile.listFiles();
    	for(int i=0;i<jarFileList.length;i++){
    		String jarPath = jarFileList[i].getPath();
    		jarPath = jarPath.replace(librootpath, "/" + projectName);
    		jarPath = jarPath.replace("\\", "/");
		    IPath ijarPath = new Path(jarPath);
		    IPath srcPath = null;
		    
		    
		    IClasspathEntry newEntry = JavaCore.newLibraryEntry(ijarPath, srcPath, null);
		    IClasspathEntry[] entries = javaProject.getRawClasspath();
		    entries = (IClasspathEntry[]) ArrayUtils.add(entries, newEntry);
		    javaProject.setRawClasspath(entries, null);
    	}
    	 
    	//javaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);

    }
    
    //��������Ĺ������������������ֱ���service��util��main
    private void createPackages(String projectName){
    	final IJavaProject javaProject = getCreatedElement();
    	try {
    		String path = File.separator + projectName + File.separator + "src";
			IPackageFragmentRoot packageFragmentRoot = javaProject.findPackageFragmentRoot(new Path(path));
			packageFragmentRoot.createPackageFragment("service", true, null);
			packageFragmentRoot.createPackageFragment("util", true, null);
			packageFragmentRoot.createPackageFragment("main", true, null);
			packageFragmentRoot.createPackageFragment("trap", true, null);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void createAndConfigProject(final IJavaProject javaProject, IProject newProjectHandle,final String projectType,IProgressMonitor monitor){
    	
    	
    	//�½�����ģ����
    	ProjectTempletXmlManager templetXmlManager  = new ProjectTempletXmlManager(JAVA_PLUGIN_ID, projectType);
    	//�ж�ģ�������Ƿ�ѡ��
    	if(getChoseOpsTempletWizardPage().getSelectedTempletName() != null){
    		// �����û�ѡ���ģ������
            templetXmlManager.setSelectedElement(getChoseOpsTempletWizardPage().getSelectedTempletName());
    	}
    	
    	//��ȡ��Ҫ���������е��ļ��У�MAP<�ļ�������,�Ƿ���ispackage>
    	final Map<String, String> folders = templetXmlManager.getBuilderFolder();
    	//��ȡ���õ�ops2.0��Դ���ڵ�Ŀ¼
    	//final List<String> resourse= templetXmlManager.getBuilderResource();
    	// ��ȡ��Ҫ��ģ���ļ�velocity�������ɵĹ���ģ��
        List<TempletVMNode> vm = templetXmlManager.getBuilderVm();
        // ��ȡ��ǰ��������Ҫ�������ļ�
        Map<String, String> files = templetXmlManager.getBuliderFile();
        
        ProjectTempletManager templetManager = new ProjectTempletManager(JAVA_PLUGIN_ID);
        //�ڹ����д���ģ��
        createTempletInProject(templetXmlManager, templetManager, vm,"mainFunction",newProjectHandle,projectType);
        createFolder(folders,newProjectHandle);
        createFileInProject(files, newProjectHandle);
        
        
	  	  try {
	  		  //3�����jar��
	    		  addRequiredLibraries(javaProject);
	  	  }catch (Exception e) {
	  		  e.printStackTrace();
	  	  }
	  	  
        try {
			newProjectHandle.refreshLocal(0, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
        
        ProjectConfigXmlManager projectConfigXmlManager = new ProjectConfigXmlManager(newProjectHandle.getLocation().toOSString() + File.separator + OpsManagerProjectTool.PROJECT_CONFIG_FILENAME);
        projectConfigXmlManager.setProjectType(projectType);
        projectConfigXmlManager.getProjectMonitorState();
        

        String opspath = newProjectNameAndLocationWizardPage.getOpsCustomLocationFieldPath().toOSString();
        String schemaApiPath = opspath + File.separator+".." + File.separator+  "document";
        projectConfigXmlManager.initXmlConfig(opspath, newProjectNameAndLocationWizardPage.getSchemaPath() , schemaApiPath, projectType);
        projectConfigXmlManager.setProjectMonitorState("off");
        Boolean isnetworkMonitor = projectConfigXmlManager.isMonitorTrunOn();
        
        //Boolean isnetworkMonitor = projectConfigXmlManager.isMonitorTrunOn();
        
        //�������̵�ģ��xml�ļ�·��
        String templetXmlPath = PathTools.getTempletFilePath(JAVA_PLUGIN_ID);
        
        //�ڵ�ǰ���̴洢�������̵�pluginID
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_CREATE_PLUGID, JAVA_PLUGIN_ID);
        
        //�ڵ�ǰ���̴洢����ģ��·��
        OpsManagerProjectTool.saveManagerInToProject(newProjectHandle, OpsManagerProjectTool.PROJECT_TEMPLET_XML_PATH, templetXmlPath);
        
        projectConfigXmlManager.configOPSPath( monitor, newProjectHandle);
        
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
    
    
    private void createFileInProject(Map<String,String> files, IProject newProjectHandle){
        if(files!=null){
            String projectPath = newProjectHandle.getLocation().toFile().getAbsolutePath();
            for(Entry<String ,String> entry : files.entrySet()){
                IOTools.copyFile(PathTools.getPlugPath(JAVA_PLUGIN_ID) + entry.getKey(),projectPath + File.separator  + entry.getValue());
            }
        }
    }
    
    private void createFolder(Map<String,String> folders, IProject newProjectHandle){
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
    
    private void createTempletInProject(ProjectTempletXmlManager templetXmlManager,ProjectTempletManager manager, List<TempletVMNode> lstVm , String mainFunction,IProject newProjectHandle,String projectType){
    	if(null == lstVm){
            return;
         }
         String projectPath = newProjectHandle.getLocation().toFile().getAbsolutePath();
         String vmfile = ""; 
         String outfile = "";
         for(TempletVMNode vmnode: lstVm){
             
             vmfile = PathTools.getPlugPath(JAVA_PLUGIN_ID) + File.separator+vmnode.getVmFileName();
             outfile = projectPath + File.separator + vmnode.getOuturl();
             
             String packageName = vmnode.getOuturl().substring(4, 8);
             OpsManagerProjectTool.packageName = packageName;

             if(vmnode.getVmName().contains("apiResourceTemplet")){
                 
                 // ��ȡ�û�ѡ���ģ��ʾ��
                 manager.addFile((new Path(PathTools.getPlugOpsViewPath()+File.separator+"OPS2.0"+File.separator+"schema")).toOSString() + File.separator + vmnode.getExampleapifile());
                 manager.createTempletByUrl(vmnode.getExampleapi(), outfile, vmfile,projectType, JAVA_PLUGIN_ID);
                 continue;
             }
             // ��ȡ�û���mainfunction����
             if(vmnode.getVmName().contains("mainfunction")){
                 manager.createMainFunVm(mainFunction, outfile, vmfile, JAVA_PLUGIN_ID);
                 continue;
             }
             manager.createOtherVm(outfile, vmfile, null, JAVA_PLUGIN_ID);
         }
    }
}