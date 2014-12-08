package com.huawei.networkos.ops.java.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.huawei.networkos.ops.wizards.ChoseOpsTempletWizardPage;

import java.lang.reflect.InvocationTargetException;

/**
 * Non internal version for org.eclipse.jdt.internal.ui.wizards.JavaProjectWizard.
 * 
 * @author lobas_av
 * @coverage core.wizards.ui
 */
public class DesignerJavaProjectWizard extends DesignerNewElementWizard
    implements
      IExecutableExtension {
  private NewJavaProjectWizardPageOne fFirstPage;
  private NewJavaProjectWizardPageTwo fSecondPage;
  private ChoseOpsTempletWizardPage choseOpsTempletWizardPage;
  private NewProjectNameAndLocationWizardPage newProjectNameAndLocationWizardPage;
  private IConfigurationElement fConfigElement;

  ////////////////////////////////////////////////////////////////////////////
  //
  // Constructor
  //
  ////////////////////////////////////////////////////////////////////////////
  public DesignerJavaProjectWizard() {
 
    setWindowTitle(Messages.DesignerJavaProjectWizard_title);
    newProjectNameAndLocationWizardPage = createNewProjectNameAndLocationWizardPage();
    choseOpsTempletWizardPage = createchoseOpsTempletWizardPage();
    
  }

  ////////////////////////////////////////////////////////////////////////////
  //
  // Pages
  //
  ////////////////////////////////////////////////////////////////////////////
  @Override
  public void addPages() {
	  
	 
    {
      fFirstPage = new NewJavaProjectWizardPageOne();
      addPage(fFirstPage);
    }
    {
      fSecondPage = new NewJavaProjectWizardPageTwo(fFirstPage);
      addPage(fSecondPage);
    }
   // fFirstPage.init(getSelection(), getActivePart());
    addPage(newProjectNameAndLocationWizardPage);
    addPage(choseOpsTempletWizardPage);
    
  }
  
  

  ////////////////////////////////////////////////////////////////////////////
  //
  // DesignerNewElementWizard
  //
  ////////////////////////////////////////////////////////////////////////////
  
  protected ChoseOpsTempletWizardPage createchoseOpsTempletWizardPage(){
      return new ChoseOpsTempletWizardPage();
  }
  protected NewProjectNameAndLocationWizardPage createNewProjectNameAndLocationWizardPage(){
	  return new NewProjectNameAndLocationWizardPage("Setting project properties");
  }
  
  @Override
  protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
    fSecondPage.performFinish(monitor);
  }

  @Override
  public boolean performFinish() {
    boolean res = super.performFinish();
    if (res) {
      {
        org.eclipse.ui.IWorkingSet[] workingSets = fFirstPage.getWorkingSets();
        if (workingSets.length > 0) {
          IJavaProject newElement = getCreatedElement();
          org.eclipse.ui.PlatformUI.getWorkbench().getWorkingSetManager().addToWorkingSets(
              newElement,
              workingSets);
        }
      }
      BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
     // BasicNewResourceWizard.selectAndReveal(
       //   fSecondPage.getJavaProject().getProject(),
     //     DesignerPlugin.getActiveWorkbenchWindow());
    }
    return res;
  }

  private org.eclipse.ui.IWorkbenchPart getActivePart() {
	  
	 
    org.eclipse.ui.IWorkbenchPage activePage = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();
    if (activePage != null) {
      return activePage.getActivePart();
    }
    return null;
  }

  @Override
  protected void handleFinishException(Shell shell, InvocationTargetException e) {
    String title = Messages.DesignerJavaProjectWizard_errorTitle;
    String message = Messages.DesignerJavaProjectWizard_errorMessage;
    ExceptionHandler.perform(e, getShell(), title, message);
  }

  public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
    fConfigElement = cfig;
  }

  @Override
  public boolean performCancel() {
    fSecondPage.performCancel();
    return super.performCancel();
  }

  public IJavaProject getCreatedElement() {
    return fSecondPage.getJavaProject();
  }
  
  public String getProjectName(){
	  return fFirstPage.getProjectName();
  }
  
  public NewJavaProjectWizardPageOne getfFirstPage() {
		return fFirstPage;
	}

	public void setfFirstPage(NewJavaProjectWizardPageOne fFirstPage) {
		this.fFirstPage = fFirstPage;
	}

	public NewJavaProjectWizardPageTwo getfSecondPage() {
		return fSecondPage;
	}

	public void setfSecondPage(NewJavaProjectWizardPageTwo fSecondPage) {
		this.fSecondPage = fSecondPage;
	}

	public ChoseOpsTempletWizardPage getChoseOpsTempletWizardPage() {
		return choseOpsTempletWizardPage;
	}

	public void setChoseOpsTempletWizardPage(
			ChoseOpsTempletWizardPage choseOpsTempletWizardPage) {
		this.choseOpsTempletWizardPage = choseOpsTempletWizardPage;
	}

	public NewProjectNameAndLocationWizardPage getNewProjectNameAndLocationWizardPage() {
		return newProjectNameAndLocationWizardPage;
	}

	public void setNewProjectNameAndLocationWizardPage(
			NewProjectNameAndLocationWizardPage newProjectNameAndLocationWizardPage) {
		this.newProjectNameAndLocationWizardPage = newProjectNameAndLocationWizardPage;
	}
}