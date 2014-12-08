package com.huawei.networkos.ops.python;
import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "com.huawei.networkos.ops.python.messages"; 
  public static String PythonProjectWizard_errorMessage;
  public static String PythonProjectWizard_errorTitle;
  public static String PythonProjectWizard_title;
  public static String DesignerNewElementWizard_errorMessage;
  public static String DesignerNewElementWizard_errorSeeLog;
  public static String DesignerNewElementWizard_errorTitle;
  public static String DesignerNewProjectCreationWizardPage_message;
  public static String DesignerNewProjectCreationWizardPage_removeErrorMessage;
  public static String DesignerNewProjectCreationWizardPage_removeErrorTitle;
  public static String DesignerNewProjectCreationWizardPage_removeProgress;
  public static String DesignerNewProjectCreationWizardPage_title;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
