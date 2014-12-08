package com.huawei.network.opsdev.core.treeview.node;

import java.util.ArrayList;
import java.util.List;
/**
 * schamefile的父节点的实体类
 * @author qWX182698
 *
 */
public class SchemaFiles implements ITreeNode{
       
      public String name;
      public List child = new ArrayList();
       
   
      public List getChild() {
         return child;
      }
   
      public void setChild(List child) {
          this.child = child;
      }
   
    
      public String getName() {
          // TODO Auto-generated method stub
          return name;
      }
   
     
      public void setName(String name) {
          // TODO Auto-generated method stub
          this.name = name;
           
      }
      public String toString(){
          return name;
      }
}