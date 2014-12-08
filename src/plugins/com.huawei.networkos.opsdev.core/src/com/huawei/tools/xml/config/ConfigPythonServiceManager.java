package com.huawei.tools.xml.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.huawei.network.opsdev.core.inspectview.bean.ElementalHttpServer;
import com.huawei.network.opsdev.core.templet.ProjectTempletCreater;
import com.huawei.network.opsdev.core.templet.TempletVMNode;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;

public class ConfigPythonServiceManager {
    
    public static void syncXmlPython(IProject iProject,List<OpsService> opsServices){

        String projectPath = iProject.getLocation().toFile().getAbsolutePath();
        ProjectTempletXmlManager manager = OpsManagerProjectTool.getTempletConfigManager(iProject);
        //获得工程的pluginID
        String pluginId = OpsManagerProjectTool.getProjectCreatePlugId(iProject);
        TempletVMNode vmnode = manager.getOpsPointByName(OpsManagerProjectTool.HUAWEI_OPS_DEVICES);
//        String vmFile = PathTools.getPlugOpsDevCorePath() + File.separator
//                + vmnode.getVmFileName();
        String vmFile = PathTools.getPlugPath(pluginId) + File.separator+vmnode.getVmFileName();
        String filePath = projectPath + File.separator + vmnode.getOuturl();
        
        //IFile file = iProject.getFile(File.separator+"util"+File.separator+"OpsServiceConfig.py");
        ProjectTempletCreater templetCreate = new ProjectTempletCreater(vmFile, null, filePath, OpsManagerProjectTool.getProjectCreatePlugId(iProject));
        templetCreate.addAttribute("opsdevices", opsServices);
        templetCreate.start();
  
       /* InputStream inputStream = null;
        try {
            inputStream =  file.getContents();
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte [] cushion    = new byte[50];
        StringBuffer buf = new StringBuffer();
        try {
            while (inputStream.read(cushion)!=-1) {
                buf.append(new String(cushion));
                cushion = new byte[50];
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String str =  buf.substring(buf.indexOf("{"));
        JSONObject jsonObject = null;
        try{
        jsonObject = JSONObject.fromObject(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        for(OpsService opsService: opsServices){
            if(jsonObject.containsKey(opsService.getName())){
                jsonObject.remove(opsService.getName());
            }
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("server", opsService.getServer());
            jsonObject2.put("port", opsService.getPort());
            jsonObject2.put("username", opsService.getUserName());
            jsonObject2.put("devicename", opsService.getDevicename());
            jsonObject2.put("passwd",opsService.getPasswd());
            jsonObject2.put("id",opsService.getDeviceId());
            jsonObject2.put("version", opsService.getXmlVersion());
            jsonObject2.put("productType",opsService.getProductType());
            jsonObject.put(opsService.getName(), jsonObject2);
        }
        List<String> opsServiceNames =new ArrayList<String>();
        for(OpsService opsService: opsServices){
            opsServiceNames.add(opsService.getName());
        }
        List<String> removeKey = new ArrayList<String>();
        for(Object obj: jsonObject.keySet()){
            if(!opsServiceNames.contains(obj.toString())){
                removeKey.add(obj.toString());
            }
        }
        for(String str1: removeKey){
            jsonObject.remove(str1);
        }
        str = jsonObject.toString();
        StringBuffer buffer = new StringBuffer();
        buffer.append(buf.substring(0, buf.indexOf("{")));
        buffer.append(str);
        try {
            file.setContents(new ByteArrayInputStream(buffer.toString().getBytes()), true,true, null);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
    }
    
    
}
