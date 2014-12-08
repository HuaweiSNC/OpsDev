package com.huawei.networkos.ops.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;

import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;


/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class SampleNewWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private ChoseOpsInstanceRestfulapiWizard choseOpsInstanceRestfulapiWizard;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public SampleNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {

		choseOpsInstanceRestfulapiWizard = new ChoseOpsInstanceRestfulapiWizard();
		addPage(choseOpsInstanceRestfulapiWizard);
		OpsManagerProjectTool.reLoadProject(ProjectPathManager.getCurrentProject(), null, "python");
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		if(choseOpsInstanceRestfulapiWizard.getChoseUrl()==null||choseOpsInstanceRestfulapiWizard.equals("")){
			MessageDialog.openError(getShell(), "Error", "You Must Chose An RestFulApi");
			return false;
		}
		
		// 获取当前信息工程
		IProject iProject  = choseOpsInstanceRestfulapiWizard.getIProject();
		// 获取当前选择的url
		String url = choseOpsInstanceRestfulapiWizard.getChoseUrl();
		// 找到所有选择的url
		List<String> urls = choseOpsInstanceRestfulapiWizard.getChosedUrls();
		final Map<String,Boolean> mapUrlExist = new HashMap<String,Boolean>();
		if(!url.equals("")){
    		final String path = getAbsolutPath(getNewApiFilePath(url).toOSString(), choseOpsInstanceRestfulapiWizard.getIProject().getLocation().toOSString());
    		IFile file = iProject.getFile(path);
    		if(file.exists()){
    			if(MessageDialog.openQuestion(getShell(), "Question", "The file "+file.getName()+" is already exist! Do you want to override it?")){
    				mapUrlExist.put(url, true);
    			}else{
    			mapUrlExist.put(url, false);
    			}
    		}else{
    			mapUrlExist.put(url, true);
    		}
		}else{
			int count = 0;
			boolean isOverrideAll = false;
			for(String str: urls){
				final String path = getAbsolutPath(getNewApiFilePath(str).toOSString(), choseOpsInstanceRestfulapiWizard.getIProject().getLocation().toOSString());
	    		IFile file = iProject.getFile(path);
	    		
	    		
	    		
	    		if(file.exists()&&isOverrideAll==false){
	    			if(count == 0){
	    				
			    		if(MessageDialog.openQuestion(getShell(), "Question", "Do you want to override all the files ?")){
			    			
			    			isOverrideAll = true;
			    		}
			    		mapUrlExist.put(str, true);
			    		count++;
			    		continue;
		    		}
	    			if(MessageDialog.openQuestion(getShell(), "Question", "The file "+file.getName()+" is already exist! if you want to override it?")){
	    				mapUrlExist.put(str, true);
	    				continue;
	    			}else{
	    				mapUrlExist.put(str, false);
	    				continue;
	    			}
	    		}
	    		mapUrlExist.put(str, true);
	    		
	    		
			}
		}
        
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					
					doFinish( monitor,mapUrlExist);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(
		IProgressMonitor monitor,Map<String , Boolean> mapUrlExist)
		throws CoreException {

		// create a sample file
		final String url = choseOpsInstanceRestfulapiWizard.getChoseUrl();
		final List<String> urls = choseOpsInstanceRestfulapiWizard.getChosedUrls();
        monitor.beginTask("Creating " + url, 2);
        final IProject iProject = choseOpsInstanceRestfulapiWizard.getIProject();

        int count = 0;
        String openPath = "";
        // 刷新内部数据
        choseOpsInstanceRestfulapiWizard.getPythonTempletManager().getApiManager().refurbishAllRestApi();
        choseOpsInstanceRestfulapiWizard.getPythonTempletManager().getUserApiManager().refurbishAllRestApi();
        // 判断是否是使用内部的schema，如果是外部的schema，需要使用的内容不一样
        boolean isUseInnerSchema = choseOpsInstanceRestfulapiWizard.getIsUseInnerSchema();
        boolean isCreateRestFulApi = false;
        StringWriter outputWriter = null;
        for(Entry<String,Boolean> urlEntry : mapUrlExist.entrySet()){
            final String path = getAbsolutPath(getNewApiFilePath(urlEntry.getKey()).toOSString(), iProject.getLocation().toOSString());
            IFile file  = iProject.getFile(path);
            count++;

            isCreateRestFulApi = false;
            if(file.exists() && urlEntry.getValue()){
            	file.delete(true, monitor);
            	isCreateRestFulApi = true;
            }else if(!file.exists()){
            	isCreateRestFulApi = true;
            }
            if(isCreateRestFulApi){
            	if(isUseInnerSchema)
            	{
            		// 内部的数据API
            		outputWriter = createRestFulApi(urlEntry.getKey(),choseOpsInstanceRestfulapiWizard.getPythonTempletManager());
            	} else {
            		// 外部增加的API
            		outputWriter = createUserRestFulApi(urlEntry.getKey(),choseOpsInstanceRestfulapiWizard.getPythonTempletManager());
            	}
            	if(outputWriter != null)
            	{
            		file.create(new ByteArrayInputStream(outputWriter.toString().getBytes()), false, monitor);    	
            	}
            }
    	
            if(count==mapUrlExist.size()){
            	openPath = path;
            }
        }
        
        // 打开最后一个内容
        final String openEditorPath = openPath;
        if(openEditorPath != null && openEditorPath.length() > 0)
        {
            monitor.worked(1);
    		monitor.setTaskName("Opening file for editing...");
    		getShell().getDisplay().asyncExec(new Runnable() {
    			public void run() {
    				IWorkbenchPage page =
    					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    				try {
    					IDE.openEditor(page,iProject.getFile(openEditorPath) , true);
    				} catch (PartInitException e) {
    				}
    			}
    		});
    		
        }
        
        monitor.worked(1);
	}
	
	/**
	 * We will initialize file contents with a sample text.
	 */

//	private InputStream openContentStream() {
//		String contents =
//			"This is the initial file contents for *.mpe file that should be word-sorted in the Preview page of the multi-page editor";
//		return new ByteArrayInputStream(contents.getBytes());
//	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "com.huawei.networkos.ops.wizard", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
	public IPath getNewApiFilePath(String url){
		String name = url.substring(1, url.length());
		name = new Character(name.charAt(0))
		.toString().toUpperCase()+name.substring(1, name.indexOf("/"))
				+ new Character(name.charAt(name.lastIndexOf("/") + 1))
						.toString().toUpperCase()
				+ name.substring(name.lastIndexOf("/") + 2, name.length());
		IProject iProject = choseOpsInstanceRestfulapiWizard.getIProject();
		if("java".equals(OpsManagerProjectTool.getProjectCreateType(iProject))){
			return choseOpsInstanceRestfulapiWizard.getIPath().append(File.separator+name+".java");
		}else if("python".equals(OpsManagerProjectTool.getProjectCreateType(iProject))){
			return choseOpsInstanceRestfulapiWizard.getIPath().append(File.separator+name+".py");
		}
			return choseOpsInstanceRestfulapiWizard.getIPath().append(File.separator+name+".py");
	}
	public String getAbsolutPath(String path1,String path2 ){
		return path1.replace(path2, "");
	}
	 
	/**
	 * 获取创建的restfulapi的
	 * @param 需要生成的restfulapi的url
	 * @param 生成restfulapi文件的manager对象
	 * @return 生成的restfulapi的stringwriter流
	 */
	private StringWriter createRestFulApi(String url,ProjectTempletManager manager){

		IProject iProject = choseOpsInstanceRestfulapiWizard.getIProject();
		if(null == iProject){
			MessageDialog.openError(getShell(), "Error", "You Must Chose A Project");
			return null;
		}
		String vmFile = OpsManagerProjectTool.getVMTempletFileName(iProject, OpsManagerProjectTool.HUAWEI_OPS_SCHEMA_VM_CALL);
		return manager.createTempletByUrl(url , vmFile, true,OpsManagerProjectTool.getProjectCreateType(iProject));
	}
	
	private StringWriter createUserRestFulApi(String url,ProjectTempletManager manager){

		IProject iProject = choseOpsInstanceRestfulapiWizard.getIProject();
        if(null == iProject){
            MessageDialog.openError(getShell(), "Error", "You Must Chose A Project");
            return null;
        }
        
        String vmFile = OpsManagerProjectTool.getVMTempletFileName(iProject, OpsManagerProjectTool.HUAWEI_OPS_SCHEMA_VM_CALL);
        return manager.createTempletByUserUrl(url , vmFile, true,OpsManagerProjectTool.getProjectCreateType(iProject));
	}
	
}