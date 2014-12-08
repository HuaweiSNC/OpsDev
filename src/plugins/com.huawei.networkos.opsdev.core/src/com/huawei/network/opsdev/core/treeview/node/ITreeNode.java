package com.huawei.network.opsdev.core.treeview.node;

import java.util.List;

public interface ITreeNode {
   
       public String getName() ;
       public void setName(String name);
       public abstract List getChild() ;
       public abstract void setChild(List child);
       
}
