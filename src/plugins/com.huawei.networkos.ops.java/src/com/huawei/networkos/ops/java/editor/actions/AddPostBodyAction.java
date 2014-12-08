package com.huawei.networkos.ops.java.editor.actions;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.huawei.networkos.ops.java.ui.editors.AddPostBodyWizard;
/**
 * 在java编辑器中加入Add Post Body右键菜单的Action
 * 展示的相关Wizard以及Dialog
 * @author zWX199308
 *
 */
public class AddPostBodyAction extends JavaAction{
	public void run(IAction action){
		JavaEditor javaEditor = getJavaEditor();
		Display display = Display.getDefault();
		AddPostBodyWizard addPostBodyWizard  = new AddPostBodyWizard(javaEditor);
		WizardDialog dialog = new WizardDialog(new Shell(display), addPostBodyWizard);
        dialog.setPageSize(400, 400);
        dialog.open();
	}

}
