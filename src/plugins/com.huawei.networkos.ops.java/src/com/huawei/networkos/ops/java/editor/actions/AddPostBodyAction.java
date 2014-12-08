package com.huawei.networkos.ops.java.editor.actions;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.huawei.networkos.ops.java.ui.editors.AddPostBodyWizard;
/**
 * ��java�༭���м���Add Post Body�Ҽ��˵���Action
 * չʾ�����Wizard�Լ�Dialog
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
