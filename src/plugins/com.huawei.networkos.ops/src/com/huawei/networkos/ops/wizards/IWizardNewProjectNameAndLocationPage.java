package com.huawei.networkos.ops.wizards;
/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * The first page in the New Project wizard must implement this interface.
 */
public interface IWizardNewProjectNameAndLocationPage extends IWizardPage {

    public static final String PYDEV_NEW_PROJECT_CREATE_PREFERENCES = "PYDEV_NEW_PROJECT_CREATE_PREFERENCES";

    final int PYDEV_NEW_PROJECT_CREATE_SRC_FOLDER = 0; //also the default
    final int PYDEV_NEW_PROJECT_CREATE_PROJECT_AS_SRC_FOLDER = 1;
    final int PYDEV_NEW_PROJECT_NO_PYTHONPATH = 2;

    /**
     * Returns a flag indicating whether the default python src folder
     * should be created.
     */
    public int getSourceFolderConfigurationStyle();

    /**
     * @return a string as specified in the constants in IPythonNature
     * @see IPythonNature#PYTHON_VERSION_XXX 
     * @see IPythonNature#JYTHON_VERSION_XXX
     * @see IPythonNature#IRONPYTHON_VERSION_XXX
     */
    public String getProjectType();
    
    public String getProjectCreateType();

    /**
     * Returns a handle to the new project.
     */
    public IProject getProjectHandle();

    public String getProjectName();

    /**
     * Gets the location path for the new project.
     */
    public IPath getLocationPath();

    /**
     * @return "Default" to mean that the default interpreter should be used or the complete path to an interpreter
     * configured.
     * 
     * Note that this changes from the python nature, where only the path is returned (because at this point, we
     * want to give the user a visual indication that it's the Default interpreter if that's the one selected)
     */
    public String getProjectInterpreter();

    public Path getOpsCustomLocationFieldPath();
    public String getMainFunctionName();

    public String getSchemaPath();
}
