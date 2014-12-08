package com.huawei.network.opsdev.core.treeview.node;

import java.util.ArrayList;
import java.util.List;


public class XmlResponseNode implements ITreeNodeMap{
    public String[] name; 
    public List<XmlResponseNode> childNodes = new ArrayList<XmlResponseNode>(); 
    
    public String[] getName() {
        return name;

    }


    public void setName(String[] name) {
        this.name = name;
        
    }


    public  List<XmlResponseNode> getChild() {
        // TODO Auto-generated method stub
        return childNodes;
    }


    public void setChild( List<XmlResponseNode> childNodes) {
        
        this.childNodes = childNodes;
    }

}
