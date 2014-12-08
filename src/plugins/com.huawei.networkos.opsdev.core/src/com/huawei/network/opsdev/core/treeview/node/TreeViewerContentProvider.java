package com.huawei.network.opsdev.core.treeview.node;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 重写了ITreeContentProvider
 * @author qWX182698
 *
 */
public class TreeViewerContentProvider implements ITreeContentProvider{
       
      public TreeViewerContentProvider()
      {
           
      }
       
       
      public Object [] getChildren(Object parentElement)
      {
           
          if (parentElement instanceof SchemaFiles){
               
          //  System.out.println(((University) parentElement).getName());
              List childList = ((SchemaFiles) parentElement).getChild();
              return childList.toArray();
          }
           
          if (parentElement instanceof SchemaFile){
               
          //    System.out.println(((SchemaFile) parentElement).getName());
              List childList = ((SchemaFile) parentElement).getChild();
              return childList.toArray();
          }
          
          if(parentElement instanceof DeviceNode){
              return ((DeviceNode)parentElement).getChild().toArray();
          }
          if(parentElement instanceof XmlResponseNode){
              return ((XmlResponseNode)parentElement).getChild().toArray();
          }
          
          return new Object[0];
      }
       
      public Object getParent(Object element)
      {
          return null;
      }
       
      public boolean hasChildren(Object element)
      {
          if (element instanceof SchemaFiles){
   
              List childList = ((SchemaFiles) element).getChild();
               
              if (childList.size() > 0)
                  return true;
              else
                  return false;
          }
           
          if (element instanceof SchemaFile){
   
              List childList = ((SchemaFile) element).getChild();
               
              if (childList.size() > 0)
                  return true;
              else
                  return false;
         }
         if(element instanceof DeviceNode){
             List childList = ((DeviceNode) element).getChild();
             
             if (childList.size() > 0)
                 return true;
             else
                 return false;
         }
          return false;
      }
       
      public Object[] getElements(Object inputElement)
      {
          if(inputElement instanceof List)
          {
              List list =(List)inputElement;
              return list.toArray();     
          }
          else
              return new Object[]{inputElement};
      }
       
      public void dispose(){}
       
      public void inputChanged(Viewer viewer,Object oldInput,Object newInput){
           
      }
       
  }