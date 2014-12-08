package com.huawei.networkos.ops.java.editor.actions;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
/**
 * 操作java编辑器的Action
 * 可以获得文本域、java编辑器
 * @author zWX199308
 *
 */
public abstract class JavaAction extends Action implements IEditorActionDelegate{
	
	protected JavaAction(){
		super();
	}
	
	protected JavaAction(String text,int style){
		super(text,style);
	}
	
	protected volatile IEditorPart targetEditor;
	
	public void setEditor(IEditorPart targetEditor) {
        this.targetEditor = targetEditor;
    }
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        setEditor(targetEditor);
    }
	
	public void selectionChanged(IAction action, ISelection selection) {
        action.setEnabled(true);
    }
	
	
	protected ITextEditor getTextEditor() {
        if (targetEditor instanceof ITextEditor) {
            return (ITextEditor) targetEditor;
        } else {
            throw new RuntimeException("Expecting text editor. Found:" + targetEditor.getClass().getName());
        }
    }
	
	protected JavaEditor getJavaEditor() {
        if (targetEditor instanceof JavaEditor) {
            return (JavaEditor) targetEditor;
        } else {
            throw new RuntimeException("Expecting PyEdit editor. Found:" + targetEditor.getClass().getName());
        }
    }

}
