package com.huawei.network.opsdev.core.schema;

import java.util.ArrayList;
import java.util.List;

import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.utils.PathTools;

import junit.framework.TestCase;

public class PythonTemlpetManagerTest extends TestCase{
    public void testPythonTemlpetManager(){
        List<String> schemaFiles = new ArrayList<String>();
        schemaFiles.add("D:\\xsd2rest\\resource\\aaa.xsd");
        schemaFiles.add("D:\\xsd2rest\\resource\\arp.xsd");
        ProjectTempletManager manager = new ProjectTempletManager(PathTools.PATH_OPS_CORE_PLUGIN_ID);
        manager.addFile(schemaFiles);
        manager.addFile(schemaFiles);
        System.out.println(manager.getApiUrls());
        System.out.println(manager.getApiUrls().get("aaa.xsd"));
       // manager.createTempletByUrl("/aaa/lam/loginFailedLimit","D:\\qx\\ops-svn\\project\\plugins\\com.huawei.networkos.opsdev.core\\src\\templet");
    }
    
}
