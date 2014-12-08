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
 * Add Post Body�ĸ�ҳ��
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
         * ���iProject
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
    	//��õ�ǰ��ѡ��ı༭��
    	ITextSelection selection = (ITextSelection) javaEditor.getEditorSite().getSelectionProvider().getSelection();
    	//��ù�������е���һ��
    	int startLine = selection.getStartLine()-1;
    	//�����ѡ�༭�����ڵ��ļ�
    	//javaEditor.getEditorInput()��Returns the input for this editor
    	IDocument doc = javaEditor.getDocumentProvider().getDocument(javaEditor.getEditorInput());
    	//����ļ��ķָ���
    	String endLineDelim = getDefaultLineDelimiter(doc);
    	//��Ҫ���������
//    	String contents = addPostBodyPage2.getPreviewContext();
    	//��������
    	for(String contents : addPostBodyPage2.getCreatedBody()){
    		insertContents(doc,endLineDelim,"    " + contents,startLine++);
        }
    	
        return true;
    }
    
    /*
	 * ����ļ��ķֽ��
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
	 * ��ǰλ�ò�������
	 * doc����ǰ�ļ�
	 * endLineDelim��������־�ķֽ��
	 * contents����Ҫ���������
	 * afterLine����ǰλ��
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
