/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.huawei.networkos.ops.python.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.python.pydev.editor.PyEdit;
import org.python.pydev.editor.actions.PyAction;

import com.huawei.networkos.ops.python.ui.editors.AddPostBodyWizard;
/**
 * 在python编辑器中加入Add Post Body右键菜单的Action
 * @author zWX199308
 *
 */
public class AddPostBodyAction extends PyAction {

    public void run(IAction action) {
        PyEdit pyEdit = getPyEdit();
        Display display = Display.getDefault();
        AddPostBodyWizard addPostBodyWizard  = new AddPostBodyWizard(pyEdit);
        WizardDialog dialog = new WizardDialog(new Shell(display), addPostBodyWizard);
        dialog.setPageSize(400, 400);
        dialog.open();

    }

}
