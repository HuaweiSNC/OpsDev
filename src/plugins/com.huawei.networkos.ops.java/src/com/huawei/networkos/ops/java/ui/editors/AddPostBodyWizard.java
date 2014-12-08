package com.huawei.networkos.ops.java.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditorExtension3.InsertMode;
/**
 * Add Post Body的父页面
 * @author zWX199308
 *
 */
public class AddPostBodyWizard extends Wizard {
    private IProject iProject;
    private JavaEditor javaEditor;
    private AddPostBodyPage addPostBodyPage;
    private AddPostBodyPage2 addPostBodyPage2;
    
    public AddPostBodyWizard(JavaEditor javaEditor) {
        setWindowTitle("PostBody Page");
        this.javaEditor = javaEditor;
        /*
         * 获得iProject
         */
        IEditorInput editorInput = javaEditor.getEditorInput();
        IFile file = (IFile) ((FileEditorInput) editorInput).getAdapter(IFile.class);
        this.iProject = file.getProject();
        addPostBodyPage = new AddPostBodyPage(iProject);
        addPostBodyPage2 = new AddPostBodyPage2();
    }

    public void addPages() {
        addPage(addPostBodyPage);
        addPage(addPostBodyPage2);
    }
    
    
    public boolean performFinish() {
    	//获得当前所选择的编辑器
    	ITextSelection selection = (ITextSelection) javaEditor.getEditorSite().getSelectionProvider().getSelection();
    	//获得光标所在行的上一行
    	int startLine = selection.getStartLine()-1;
    	//获得所选编辑器所在的文件
    	//javaEditor.getEditorInput()：Returns the input for this editor
    	IDocument doc = javaEditor.getDocumentProvider().getDocument(javaEditor.getEditorInput());
    	//获得文件的分隔符
    	String endLineDelim = getDefaultLineDelimiter(doc);
    	//需要插入的内容
//    	String contents = addPostBodyPage2.getPreviewContext();
    	//插入内容
    	for(String contents : addPostBodyPage2.getCreatedBody()){
    		insertContents(doc,endLineDelim,"    " + contents,startLine++);
        }
    	
        return true;
    }
    
    /*
	 * 获得文件的分界符
	 */
    public String getDefaultLineDelimiter(IDocument document) {
    	
		if (document instanceof IDocumentExtension4)
			return ((IDocumentExtension4)document).getDefaultLineDelimiter();
		
		String lineDelimiter= null;
		try {
			lineDelimiter= document.getLineDelimiter(0);
		} catch (BadLocationException x) {
			x.printStackTrace();
		}

		if (lineDelimiter != null)
			return lineDelimiter;

		String sysLineDelimiter= System.getProperty("line.separator");
		String[] delimiters= document.getLegalLineDelimiters();
		Assert.isTrue(delimiters.length > 0);
		for (int i= 0; i < delimiters.length; i++) {
			if (delimiters[i].equals(sysLineDelimiter)) {
				lineDelimiter= sysLineDelimiter;
				break;
			}
		}

		if (lineDelimiter == null)
			lineDelimiter= delimiters[0];

		return lineDelimiter;
	}
    
    
    /*
	 * 向当前位置插入内容
	 * doc：当前文件
	 * endLineDelim：结束标志的分界符
	 * contents：需要插入的内容
	 * afterLine：当前位置
	 */
    public void insertContents(IDocument doc, String endLineDelim, String contents, int startLine) {
    	
        try {
            int offset = -1;
            if (doc.getNumberOfLines() > startLine) {
                offset = doc.getLineInformation(startLine + 1).getOffset();

            } else {
                offset = doc.getLineInformation(startLine).getOffset();
            }

            if (doc.getNumberOfLines() - 1 == startLine) {
                contents = endLineDelim + contents;

            }

            if (!contents.endsWith(endLineDelim)) {
                contents += endLineDelim;
            }

            if (offset >= 0) {
                doc.replace(offset, 0, contents);
            }
        } catch (BadLocationException e) {
        	e.printStackTrace();
        }
    }

}
