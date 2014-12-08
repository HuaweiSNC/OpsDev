package com.huawei.network.opsdev.core.utils;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.ui.internal.Workbench;

import com.huawei.network.opsdev.core.event.IPageSelectListening;
import com.huawei.network.opsdev.core.event.OpsEventListener;
import com.huawei.network.opsdev.core.event.PageSelectChangeEvent;
import com.huawei.network.opsdev.core.inspectview.bean.ElementalHttpServer;
import com.huawei.network.opsdev.core.templet.ApiFileShower;
import com.huawei.network.opsdev.core.templet.ProjectTempletCreater;
import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.TempletVMNode;
import com.huawei.network.opsdev.wizard.LoadFileDialog;
import com.huawei.tools.xml.config.ConfigXmlManager;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;
import com.huawei.tools.xml.config.ProjectTempletXmlManager;

public class OpsManagerProjectTool {

    public final static String SCHEMAFILE_CONFIG = "loadschemafile";
    public final static String PROJECT_CONFIG = "projectconfigxml";
    public final static String PROJECT_TEMPLET = "projecttempletxml";
    public final static String PROJECT_TYPE = "projectcreatetype";
    public final static String PROJECT_MONITOR_STATE = "projectmonitorstate";
    public final static String PROJECT_MONITOR_PORT = "projectmonitorport";
    public final static String PROJECT_CONFIG_FILENAME = ".opsdev";
    
    public final static String PROJECT_CREATE_PLUGID = "projectcreateplugid";
    public final static String PROJECT_TEMPLET_XML_PATH = "projecttepletxmlpath";

    public final static String DEVICEMANAGER_CALL_HANDLE_POINT = "handeldefcode";
    public final static String DEVICEMANAGER_HANDLE_POINT = "handlecode";
    public final static String HUAWEI_OPS_SCHEMA_VM_CALL = "apiResource";
    public final static String HUAWEI_OPS_PROJECT_REST_CALL = "opsrestcall";
    public final static String HUAWEI_OPS_DEVICES = "opsDevices";
    
    public enum OPSEVENT{
        PROJECT_DELETE,
        PROJECT_ADD,
        PROJECT_REFRUSH
    }
     
    //创建工程的pluginID
    public static String packageName = "";
    
    public static String getPackageName() {
        return packageName;
    }
    
    private static Collection listeners = new HashSet();

    public static void addListener(OpsEventListener listener) {

        if (listeners == null) {

            listeners = new HashSet();
        }
        listeners.add(listener);
    }

    public static void removeListener(OpsEventListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }
    
    public static void notifyEvent(OPSEVENT event, Map<String, Object> mapEventValue)
    {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            OpsEventListener listener = (OpsEventListener) iter.next();
            listener.notifyEvent(event, mapEventValue );
        }
    } 
    
    public static Object getManagerFromProject(IProject iProject, String configName) {
        Object object = null;
        try {
            object = iProject.getSessionProperty(new QualifiedName(
                    configName, configName));
        } catch (CoreException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        return object;
    }

    public static void saveManagerInToProject(IProject iProject, String configName, Object manager) {
        try {
            iProject.setSessionProperty(new QualifiedName(configName,
                    configName), manager);
        } catch (CoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static ProjectTempletManager getTempletManager(IProject iProject) {

        ProjectTempletManager manager = (ProjectTempletManager) getManagerFromProject(iProject, SCHEMAFILE_CONFIG);
        if (null == manager)
        {
            manager = new ProjectTempletManager(PathTools.PATH_OPS_CORE_PLUGIN_ID);
            // 在工程区存储schemafile存放类
            saveManagerInToProject(iProject, SCHEMAFILE_CONFIG, manager);
        }
        return manager;
    }

    public static ProjectConfigXmlManager getConfigManager(IProject iProject) {

        ProjectConfigXmlManager manager = (ProjectConfigXmlManager) getManagerFromProject(iProject, PROJECT_CONFIG);
        if (null == manager)
        {
            manager = new ProjectConfigXmlManager(iProject.getLocation().toOSString() + File.separator
                    + PROJECT_CONFIG_FILENAME);
         
            String projectType = getProjectCreateType(iProject);
            String opsCore = PathTools.getPlugOpsViewPath();
            String opspath = opsCore+File.separator+"OPS2.0"+File.separator+"ops";
            String schemaPath = opsCore+File.separator+"OPS2.0"+File.separator+"schema";
            //g(String opspath, String schemaPath, String opsapiDocpath , String type)
            String schemaApiPath = opspath + File.separator+".." + File.separator+  "document";
            manager.initXmlConfig(opspath, schemaPath, schemaApiPath,  projectType);
            manager.configOPSPath(null, iProject);
            //projectConfigXmlManager.configOPSPath(projectPage.getOpsCustomLocationFieldPath().toOSString(), monitor, iProject);
            saveManagerInToProject(iProject, PROJECT_CONFIG, manager);
        }
        return manager;
    }

    public static ProjectTempletXmlManager getTempletConfigManager(IProject iProject) {

        ProjectTempletXmlManager manager = (ProjectTempletXmlManager) getManagerFromProject(iProject, PROJECT_TEMPLET);
        //获得当前工程的类型
        String projectType = getConfigManager(iProject).getProjectType();
//        Object projectTypeObj = getManagerFromProject(iProject, PROJECT_TYPE);
        if (null == manager)
        {
            
           String plugid = getProjectCreatePlugId(iProject);
           if (plugid == null || plugid.length() == 0)
           {
               plugid = PathTools.PATH_OPS_CORE_PLUGIN_ID;
           }
            manager = new ProjectTempletXmlManager(plugid, projectType);
            saveManagerInToProject(iProject, PROJECT_TEMPLET, manager);
        }
        return manager;
    }

    /****
     * 获取指定工程与指定vm名称的vm文件名
     * @param iProject 工程名
     * @param vmName vm名称
     * @return 返回vm文件路径
     */

    public static String getVMTempletFileName(IProject iProject, String vmName) {

        ProjectTempletXmlManager manager = getTempletConfigManager(iProject);
        return manager.getOpsPointVMFileByName(vmName);
    }

    /****
     * 获取指定工程与指定vm名称的vm文件名
     * @param iProject 工程名
     * @param vmName vm名称
     * @return 返回vm文件路径
     */

    public static String getOutputFileName(IProject iProject, String vmName) {

        ProjectTempletXmlManager manager = getTempletConfigManager(iProject);
        return manager.getOpsOutputByName(vmName);
    }
    
    public static void resetNetMonitorFile(IProject iProject)
    { 
        
        Boolean isRun = ElementalHttpServer.getIsRun();
        Integer port = ElementalHttpServer.getMonitorPort();
        Boolean projectMonitor = (Boolean) getManagerFromProject(iProject, OpsManagerProjectTool.PROJECT_MONITOR_STATE);
        Integer monitorPort = (Integer) getManagerFromProject(iProject, OpsManagerProjectTool.PROJECT_MONITOR_PORT);
         
        if (projectMonitor != isRun || port != monitorPort)
        {
            Boolean monitorStatus = (projectMonitor && isRun);
            
            String plugid = OpsManagerProjectTool.getProjectCreatePlugId(iProject);
            String projectPath = iProject.getLocation().toFile().getAbsolutePath();
            ProjectTempletXmlManager manager = getTempletConfigManager(iProject);
            TempletVMNode vmnode = manager.getOpsPointByName(HUAWEI_OPS_PROJECT_REST_CALL);
            String vmFile = PathTools.getPlugPath(plugid) + File.separator
                    + vmnode.getVmFileName();
            String filePath = projectPath + File.separator + vmnode.getOuturl();
            // 重新生成REST CALL调用模板
            ProjectTempletCreater templetCreate = new ProjectTempletCreater(vmFile, null, filePath, OpsManagerProjectTool.getProjectCreatePlugId(iProject) );
            templetCreate.addAttribute("monitorenable", monitorStatus);
            templetCreate.addAttribute("monitorip", "127.0.0.1");
            
            templetCreate.addAttribute("monitorport", port);
            templetCreate.start();
            
            // 重新保存当前工程的定义
            ProjectConfigXmlManager config = getConfigManager(iProject);
            config.setProjectMonitorState(monitorStatus ? "on" : "off");
            saveManagerInToProject(iProject, PROJECT_MONITOR_STATE, projectMonitor);
            saveManagerInToProject(iProject, PROJECT_MONITOR_PORT, port);
            
            //初始化truststore和storepassword
            ConfigXmlManager configXmlManager = new ConfigXmlManager(iProject.getLocation().toOSString()+File.separator+".opsdevice");
            configXmlManager.praseFile();
        }
    }
    
    /****
     * 获得工程创建的插件ID
     * @param iProject 工程名
     * @return 创建工程选的插件ID
     */
    public static String getProjectCreatePlugId(IProject iProject) {
        return (String)getManagerFromProject(iProject, PROJECT_CREATE_PLUGID);
    }
    
    
    /****
     * 获得工程创建的模板路径
     * @param iProject 工程名
     * @return 创建工程选的模板的XML路径
     */
    public static String getProjectTempletXmlPath(IProject iProject) {
        return (String)getManagerFromProject(iProject, PROJECT_TEMPLET_XML_PATH);
    }
    
    /****
     * 获得工程类型
     * @param iProject 工程名
     * @return 创建工程选的类型
     */
    public static String getProjectCreateType(IProject iProject) {
        return (String)getManagerFromProject(iProject, PROJECT_TYPE);
    }
    
    public static void resetMainSchemaPath(IProject iProject, String newSchema)
    {
        ProjectTempletManager templet = getTempletManager(iProject);
        templet.clearMain();
 
        LoadFileDialog dialog = new LoadFileDialog(Workbench.getInstance()
                .getDisplay().getActiveShell(), templet,
                ApiFileShower.getFileListFromFilePath(newSchema,
                        iProject), iProject);
        dialog.open();
        
    }
    
    public static void reLoadProject(IProject iProject,IProgressMonitor monitor, String projectType){
        
        ProjectConfigXmlManager manager = (ProjectConfigXmlManager) getManagerFromProject(iProject, PROJECT_CONFIG);
        if (manager != null)
        {
            return ;
        }
        
        //新建工程模板类
        String projectTypeTmp = projectType;
        String projectPlugId = PathTools.PATH_OPS_CORE_PLUGIN_ID;
        try {
            iProject.refreshLocal(0, monitor);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        String opsDevProjectFile = iProject.getLocation().toOSString() + File.separator + OpsManagerProjectTool.PROJECT_CONFIG_FILENAME;
        File opsDevProject = new File(opsDevProjectFile);
        
         ProjectConfigXmlManager projectConfigXmlManager = null;
        Boolean isnetworkMonitor = false;
        if(opsDevProject.exists()){
            projectConfigXmlManager = new ProjectConfigXmlManager(iProject.getLocation().toOSString() + File.separator + OpsManagerProjectTool.PROJECT_CONFIG_FILENAME);
            projectConfigXmlManager.parseFile();
            isnetworkMonitor = projectConfigXmlManager.isMonitorTrunOn();
            projectTypeTmp = projectConfigXmlManager.getProjectType();
            projectPlugId = projectConfigXmlManager.getPlugID();
        }else{
            
            projectConfigXmlManager = new ProjectConfigXmlManager(iProject.getLocation().toOSString() + File.separator + OpsManagerProjectTool.PROJECT_CONFIG_FILENAME);
            String plugindir = PathTools.getPlugOpsViewPath();
            projectConfigXmlManager.setProjectType(projectTypeTmp);
            String opspath = plugindir+File.separator+"OPS2.0"+File.separator+"ops"; //
            String schemaApiPath = opspath + File.separator+".." + File.separator+  "document";
            projectConfigXmlManager.initXmlConfig(opspath, plugindir+File.separator+"OPS2.0"+File.separator+"schema" , schemaApiPath, projectTypeTmp);
            projectConfigXmlManager.setProjectMonitorState("off");
            isnetworkMonitor = projectConfigXmlManager.isMonitorTrunOn();
            projectConfigXmlManager.configOPSPath( monitor, iProject);
        }
        
        ProjectTempletXmlManager templetXmlManager  = new ProjectTempletXmlManager(projectPlugId, projectTypeTmp);
        ProjectTempletManager templetManager = new ProjectTempletManager(projectPlugId);
        
        // 在工程区存储schemafile存放类
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.SCHEMAFILE_CONFIG, templetManager);
        
        // 在工程区存储当前工程的配置文件
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.PROJECT_CONFIG, projectConfigXmlManager);
      
        // 在工程区存储当前工程的配置文件
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.PROJECT_TEMPLET, templetXmlManager);
 
        // 在工程区存储当前工程的配置文件
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.PROJECT_TYPE, projectTypeTmp);
 
        // 在当前工程区存储网络监视状态
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.PROJECT_MONITOR_STATE, isnetworkMonitor);
        
        // 在当前工程区存储创建工程的插件ID
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.PROJECT_CREATE_PLUGID, projectPlugId);
        
    }
        

}
