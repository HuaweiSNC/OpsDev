package com.huawei.networkos.ops.views;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


public class AboutOpsdevWorkbench implements IWorkbenchWindowActionDelegate{
    private ISelection selection;
    
    public void run(IAction action) {
        // TODO Auto-generated method stub
        Display display =  Display.getDefault();
        AboutOpsDevPage aboutOpsDevPage = new AboutOpsDevPage(new Shell(display));
        aboutOpsDevPage.open();
    }


    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
        
    }


    public void dispose() {
        // TODO Auto-generated method stub
        
    }

    public void init(IWorkbenchWindow window) {
        // TODO Auto-generated method stub
        
    }

}
