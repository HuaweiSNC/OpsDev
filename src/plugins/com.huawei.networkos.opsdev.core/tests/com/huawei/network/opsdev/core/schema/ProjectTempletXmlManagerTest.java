package com.huawei.network.opsdev.core.schema;

import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.tools.xml.config.ProjectTempletXmlManager;

import junit.framework.TestCase;

public class ProjectTempletXmlManagerTest extends TestCase{
    public void testXmlManager(){
        ProjectTempletXmlManager managerTest = new ProjectTempletXmlManager(PathTools.PATH_OPS_CORE_PLUGIN_ID, "D:\\qx\\ops-svn\\project\\plugins\\com.huawei.networkos.opsdev.core\\projecttemplet.xml");
        System.out.println(managerTest.getBuilderFolder());
        System.out.println(managerTest.getBuilderResource());
        System.out.println(managerTest.getBuilderVm());
    }
}
