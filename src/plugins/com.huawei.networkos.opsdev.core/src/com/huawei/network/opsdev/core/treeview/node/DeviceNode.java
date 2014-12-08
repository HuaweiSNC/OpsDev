package com.huawei.network.opsdev.core.treeview.node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;

import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.tools.xml.config.OpsService;

public class DeviceNode implements ITreeNode {
    private List<OpsHandleNode> handleNodes= new ArrayList<OpsHandleNode>();
    private OpsService opsService;
    public DeviceNode(OpsService opsService){
        this.opsService = opsService;
    }
    
    public String getName() {
        return opsService.getName();
    }

    public OpsService getOpsService(){
        return opsService;
    }
    public void setName(String name) {

        
    }

   
    public List getChild() {
        return handleNodes;
    }

   
    public void setChild(List child) {
        handleNodes = child;
        
    }
    public void setImage(){
        
    }
    public Image getImage(){
//        Image image = new Image(new Device, filename)
        //TODO
        Image image = new Image(null,PathTools.getPlugOpsDevCorePath()+File.separator+"icons"+File.separator+"device_node.png");
        return image;
    }
    
}
