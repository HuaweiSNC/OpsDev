package com.huawei.network.opsdev.core.treeview.node;

import java.util.ArrayList;
import java.util.List;

/**
 * schemaFile的文件的节点实体类
 * @author qWX182698
 *
 */
public class SchemaFile implements ITreeNode {
   
      private String name;
      private List child = new ArrayList();
      private SchemaFiles parent; 
      private String fullPath;
      
      
      public String getFullPath() {
        return fullPath;
    }
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    public void setParent(SchemaFiles parent){
          this.parent = parent;
      }
     public SchemaFiles getParent(){
         return parent;
     }
      public String getName() {
          // TODO Auto-generated method stub
          return name;
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
   
  }