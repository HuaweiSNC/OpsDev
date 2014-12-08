package com.huawei.network.opsdev.core.treeview.node;

import java.util.ArrayList;
import java.util.List;

/**
 * restfulApi 
 * @author qWX182698
 *
 */
public class RestFulApi implements ITreeNode {
    
    private String name;
    private List child = new ArrayList();
    private SchemaFile parent;
    
    public String getName() {
        // TODO Auto-generated method stub
        return name;
    }
    
    public SchemaFile getParent(){
        return parent;
    }
    public void setParent(SchemaFile object){
        parent = object;
    }
    
    public void setName(String name) {
        // TODO Auto-generated method stub
        this.name = name;
 
    }
 

    public List getChild() {
        // TODO Auto-generated method stub
        return child;
    }
 
  
    public void setChild(List child) {
        // TODO Auto-generated method stub
        this.child = child;
 
    }
    public String toString(){
        return name;
    }
 
}