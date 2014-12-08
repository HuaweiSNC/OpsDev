package com.huawei.network.opsdev.core.treeview.node;

import java.io.File;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;

public class OpsHandleNode implements ITreeNode{
    private OpsService opsService;
    private OpsServiceUrlHandle handle;
    public OpsHandleNode(OpsServiceUrlHandle handle){
        this.handle = handle;
    }

    public OpsServiceUrlHandle getOpsServiceUrlHandle(){
        return handle;
    }
    
    public String getName() {
        return handle.getName();
    }

    public void setName(String name) {
        // TODO Auto-generated method stub
    }
    
    public void setParent(OpsService opsService){
        this.opsService = opsService;
    }
    
    public OpsService getParent(){
        return opsService;
    }

    public List getChild() {

        return null;
    }

    public void setChild(List child) {
        
    }
    public Image getImage(){

        Image image = new Image(null,PathTools.getPlugOpsDevCorePath()+File.separator+"icons"+File.separator+"device_handle.png");
        return image;
  }

}
