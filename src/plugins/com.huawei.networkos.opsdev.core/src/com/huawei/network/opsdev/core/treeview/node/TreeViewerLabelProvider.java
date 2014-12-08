package com.huawei.network.opsdev.core.treeview.node;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class TreeViewerLabelProvider implements ILabelProvider {
    public TreeViewerLabelProvider() {

    }

    public Image getImage(Object element)
    {
        if (element instanceof SchemaFiles) {

            return null;
        }

        if (element instanceof SchemaFile) {

            return null;
        }

        if (element instanceof RestFulApi) {

            return null;
        }
        if (element instanceof DeviceNode){
            return ((DeviceNode) element).getImage();
        }
        if (element instanceof OpsHandleNode){
            return ((OpsHandleNode) element).getImage();
        }
        return null;

    }

    public String getText(Object element)
    {
        if (element instanceof SchemaFiles) {

            return ((SchemaFiles) element).getName();
        }

        if (element instanceof SchemaFile) {

            return ((SchemaFile) element).getName();
        }

        if (element instanceof RestFulApi) {

            return ((RestFulApi) element).getName();
        }
        if (element instanceof DeviceNode){
            return ((DeviceNode) element).getName();
        }
        if (element instanceof OpsHandleNode){
            return ((OpsHandleNode) element).getName();
        }

        return "";
    }
   
    public void addListener(ILabelProviderListener listener)
    {

    }

    public void dispose()
    {

    }

    public boolean isLabelProperty(Object element, String property)
    {
        return false;
    }

    public void removeListener(ILabelProviderListener listener)
    {

    }

}