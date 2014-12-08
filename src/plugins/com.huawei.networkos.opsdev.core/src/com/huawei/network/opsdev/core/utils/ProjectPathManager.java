package com.huawei.network.opsdev.core.utils;

import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;

/**
 * 项目路径管理
 * @author qWX182698
 *
 */
public class ProjectPathManager {

    /**
     * 获取当前的项目
     * @return
     */
    public static IProject getCurrentProject(){
        ISelectionService selectionService =   
                Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();  
      
        ISelection selection = selectionService.getSelection();  
        IProject project = null;
        if(selection instanceof IStructuredSelection) {  
            Object element = ((IStructuredSelection)selection).getFirstElement();  
  
            if (element instanceof IResource) {  
                project= ((IResource)element).getProject();  
            }else if (element instanceof IAdaptable) {
                IAdaptable adaptable = (IAdaptable) element;
                Object projectObject =  adaptable.getAdapter(IResource.class);

                if (projectObject instanceof IProject)
                {
                    project = (IProject) projectObject;
                }  else if (projectObject instanceof IFolder)
                {
                    project = ((IFolder) projectObject).getProject();
                } else {
                    project = null;
                }
              
            }else if (element instanceof IProject) {
                project = (IProject) element;
            }

        } 
        return project;
    }

    
    /**
     * 获取当前的项目
     * @return
     */
    public static IPath getSelectFolder(){
        
        ISelectionService selectionService =   
                Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();  
      
        IPath path = null;
        ISelection selection = selectionService.getSelection();  
        IProject project = null;
        if(selection instanceof IStructuredSelection) {  
            
            Object element = ((IStructuredSelection)selection).getFirstElement();  
            if (element instanceof IResource) {  
                project= ((IResource)element).getProject();  
            }else if (element instanceof IAdaptable) {
                IAdaptable adaptable = (IAdaptable) element;
                Object projectObject =  adaptable.getAdapter(IResource.class);
                if (projectObject instanceof IProject)
                {
                    project = (IProject) projectObject;
                }  else if (projectObject instanceof IFolder)
                {
                    path = ((IFolder) projectObject).getLocation();
                    return path;
                }  
            }else if (element instanceof IProject) {
                project = (IProject) element;
            }
            
        } 
        
        if (null != project)
        {
            path = project.getLocation();
        }
        return path;
    }

    /**
     * 获取选择的项目或者资源的路径
     * @return
     */
    public static IPath getSelectionPath() {
    
        IProject project =  getCurrentProject();
        if (null != project)
        {
            return project.getLocation();
        }
        
        return null;
    }

    /**
     * 获取包括schame文件的文件夹的路径
     * @param iProject 具体哪一个项目
     * @return
     */
    public static String getSchemaFolderPath(IProject iProject) {
        if (iProject != null) {

            ProjectConfigXmlManager configXmlManager = OpsManagerProjectTool.getConfigManager(iProject);
            return configXmlManager.getSchemaRootPath();
        }
        return null;
    }

}
