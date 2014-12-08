package com.huawei.network.opsdev.core.event;

import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class PageSelectChangeEvent extends EventObject{
    private static IProject currentIProject = null;
    private boolean state = true;
    public PageSelectChangeEvent(Object source) {
        super(source);
        IEditorInput editorInput =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
        if (editorInput instanceof FileEditorInput) {
            IFile file = (IFile) ((FileEditorInput) editorInput).getAdapter(IFile.class);
            IProject iProject =  file.getProject();
            if(iProject!=currentIProject){
                state = false;
                currentIProject = iProject;
            }else{
                state = true;
            }
            
        }else{
            state = true;
        }
        
       
    }
    public static IProject getCurrentIProject() {
        return currentIProject;
    }
    public static void setCurrentIProject(IProject currentIProject) {
        PageSelectChangeEvent.currentIProject = currentIProject;
    }
    public boolean getState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }

    


}
