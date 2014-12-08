package com.huawei.networkos.ops.python.ui.editors;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.python.pydev.core.docutils.PySelection;
import org.python.pydev.editor.PyEdit;

import com.huawei.network.opsdev.core.utils.ConsoleFactory;
/**
 * Add Post BodyµÄ¸¸Ò³Ãæ
 * @author zWX199308
 *
 */
public class AddPostBodyWizard extends Wizard {
    private IProject iProject;
    private PyEdit pyEdit;
    private AddPostBodyPage addPostBodyPage;
	private AddPostBodyPage2 addPostBodyPage2;
    
    public AddPostBodyWizard(PyEdit pyEdit) {
        setWindowTitle("PostBody Page");
        this.iProject = pyEdit.getProject();
        this.pyEdit = pyEdit;
        addPostBodyPage = new AddPostBodyPage(iProject);
        addPostBodyPage2 = new AddPostBodyPage2();
    }

    public void addPages() {
        addPage(addPostBodyPage);
        addPage(addPostBodyPage2);
    }
    
    
    
    public boolean performFinish() {
        StringBuffer buf = new StringBuffer();
        PySelection pySelection = pyEdit.createPySelection();
        int x = pySelection.getCursorLine();
        System.out.println(x);
        ConsoleFactory.printToConsole(x + "",true);
        for(String str : addPostBodyPage2.getCreatedBody()){
            pySelection.addLine("    "+str, x++);
        }
        return true;
        
    }

}
