package com.huawei.network.opsdev.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;
 

/**
 * 路径工具
 * @author qWX182698
 * @modify xielequan
 *
 */
public class PathTools {
    
    public final static String PATH_OPS_CORE_PLUGIN_ID = "com.huawei.networkos.ops.core";
    
    public final static String PATH_OPS_VIEW_PLUGIN_ID = "com.huawei.networkos.ops";
    
    
    /***
     * 获取插件当前的工作安装目录下模版目录
     * @return 插件安装的com.huawei.networkos.ops.core全路径
     */
    public static String getTempletFilePath(String plugid)
    {
        String projectTempletFile = "";
        if(null != plugid && !plugid.isEmpty())
        {
            projectTempletFile = getPlugPath(plugid);
        } else {
            projectTempletFile = getPlugOpsDevCorePath();
        }
        return projectTempletFile + File.separator + "projecttemplet.xml" ;
    }
    
    /**
     * 根据所给的plugId获得该plug的地址
     * @param plugId
     * @return 
     */
    
    private static String pluginOpsDevCorePath = "";
    private static String pluginOpsViewPath = "";
    
    public static String getPlugVersion(){
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(getPlugPath(PATH_OPS_CORE_PLUGIN_ID)+File.separator+"META-INF"+File.separator+"MANIFEST.MF");
            properties.load(fileInputStream);
            return  properties.get("Bundle-Version").toString();
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return "";
    }
    
    public static String getEclipsePath()
    {
        return Platform.getInstallLocation().getURL().getPath();
    }
    
    /*
     * 获取当前插件的路径信息
     * @return 当前插件的路径信息
     */
    public static String getPlugPath(String plugId){
 
        String eclipsePath = Platform.getInstallLocation().getURL().getPath();
        String pluginPath = Platform.getBundle(plugId).getLocation();
        pluginPath = pluginPath.substring(pluginPath.lastIndexOf("file")+5);
        if(!pluginPath.contains(":")){
            return eclipsePath + pluginPath;
        }
        return pluginPath;
    }

    /**
     * 获取当前在 pydevexplore里面选择的项目的对象
     * @return 当前工程
     */
    public static IProject getCurrentProject(){  
        
        IProject project = null;  

        IWorkbenchWindow iworkbench = Workbench.getInstance().getActiveWorkbenchWindow();
        
        //1.根据当前编辑器获取工程  
        //第一种是通过活动的也就是你正打开的编辑器对象为基础，这个时候你打开的是那个IProject里面的文件，获取的就是那个IProject。
//        if (null != iworkbench.getActivePage())
//        {
//            IEditorPart part = iworkbench.getActivePage().getActiveEditor();
//            if(part != null){  
//                Object object = part.getEditorInput().getAdapter(IFile.class);  
//                if(object != null){  
//                    project = ((IFile)object).getProject();  
//                }  
//            } 
//        }
        
        if(project != null){  
            return project;
        }
        
        // 获取选中的节点然后获取工程
        //第二种是通过ISelectionService服务获取当前选中的对象
        if (null != iworkbench.getSelectionService())
        {
            ISelectionService selectionService = iworkbench.getSelectionService();  
            ISelection selection = selectionService.getSelection();  
            if(selection instanceof IStructuredSelection) {  
                Object element = ((IStructuredSelection)selection).getFirstElement();  
      
                if (element instanceof IResource) {  
                    project= ((IResource)element).getProject();  
                }else if (element instanceof IAdaptable) {
                    IAdaptable adaptable = (IAdaptable) element;
                  project = (IProject) adaptable.getAdapter(IResource.class);
//                  project = ((IFolder) adaptable.getAdapter(IResource.class)).getProject();
              }else if (element instanceof IProject) {
                  project = (IProject) element;
              }
            }   
        }
        
        return project;  
    }  
   
    
    /***
     * 获取插件当前的工作安装目录
     * @return 插件安装的com.huawei.networkos.ops.core全路径
     */
    public static String getPlugOpsDevCorePath()
    {
        if(pluginOpsDevCorePath.isEmpty())
        {
            pluginOpsDevCorePath = getPlugPath(PATH_OPS_CORE_PLUGIN_ID);
        }
        return pluginOpsDevCorePath;
    }
    
        /***
     * 获取插件当前的工作安装目录
     * @return 插件安装的com.huawei.networkos.ops.core全路径
     */
    public static String getPlugOpsViewPath()
    {
        if(pluginOpsViewPath.isEmpty())
        {
            pluginOpsViewPath = getPlugPath(PATH_OPS_VIEW_PLUGIN_ID);
        }
        return pluginOpsViewPath;
    }
    /***
     * 获取插件当前的工作安装目录下模版目录
     * @return 插件模板的全路径
     */
    public static String getPlugOpsTempletPath(String pluginID)
    {
        return getPlugPath(pluginID) + File.separator + "templet" ;
    }
    
    /***
     * 获取模板的相对路径
     * @return 插件安装的com.huawei.networkos.ops.core全路径
     */
    public static String getRelativeFileTempletPath(String pluginID , String fullpath)
    {
        String templetDir = getPlugOpsTempletPath(pluginID);
        File ftempletFile = new File(fullpath);
        File ftempletDir = new File( templetDir );
        String filepath = ftempletFile.getAbsolutePath();
        String fileTempletDir = ftempletDir.getAbsolutePath() + File.separator;
        return filepath.replace(fileTempletDir, "");
        
    }
    
    
}
