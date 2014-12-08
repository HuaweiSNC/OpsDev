package com.huawei.network.opsdev.core.treeview.node;

import java.util.List;


public interface ITreeNodeMap {
    public String[] getName() ;
    public void setName(String[] name);
    public abstract  List<XmlResponseNode> getChild() ;
    public abstract void setChild(List<XmlResponseNode> child);
    
}
