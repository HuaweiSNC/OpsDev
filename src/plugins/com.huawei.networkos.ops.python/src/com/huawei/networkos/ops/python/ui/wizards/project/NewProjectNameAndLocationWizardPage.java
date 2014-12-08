package com.huawei.networkos.ops.python.ui.wizards.project;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.python.pydev.plugin.PyStructureConfigHelpers;
import org.python.pydev.plugin.preferences.PydevPrefs;
import org.python.pydev.ui.wizards.gettingstarted.AbstractNewProjectPage;
import org.python.pydev.utils.ICallback;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.networkos.ops.wizards.IWizardNewProjectNameAndLocationPage;

/**
 * First page for the new project creation wizard. This page
 * collects the name and location of the new project.
 * 
 * NOTE: COPIED FROM org.eclipse.ui.internal.ide.dialogs.WizardNewProjectNameAndLocationPage 
 * Changed to add the details for the python project type 
 */


public class NewProjectNameAndLocationWizardPage extends AbstractNewProjectPage implements
        IWizardNewProjectNameAndLocationPage {

    // Whether to use default or custom project location
    private boolean useDefaults = true;
    
    private boolean useOpsDefaults = true;
    
    private boolean useSchemaDefaults = true;
    // initial value stores
    private String initialProjectFieldValue;

    private IPath initialLocationFieldValue;
    
    private Path initialOpsLocationFieldValue;
    //
    private Path initialSchemaLocationFieldValue;

    private String initialMainFunctionNameFieldValue;
    // the value the user has entered
    private String customLocationFieldValue;

    private String opsCustomLocationFieldValue; 
    // widgets
    
    //
    private String schemaCustomLocationFieldValue;
    private Text projectNameField;

    private Text locationPathField;
    private Text schemalocationPathField;

    private Label locationLabel;
    private Label schemalocationLabel;
    
    private Label opsLocationLabel;
    
    private Button opsBrowseButton;
    
    private Button browseButton;
    private Button schemabrowseButton;
    
    private Text opsLocationPathField;
    
    private Text mainFunctionNameField;
    
    private String mainFunctionName;

    private String initialMainFunctionName;
    
    private String schemaPath = "";

    //获得schema文件路径，自定义的或是默认的
    public String getSchemaPath() {
        return ("".equals(schemaPath)?initialSchemaLocationFieldValue.toOSString():schemaPath);
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }


    private PyProjectPythonDetails.ProjectInterpreterAndGrammarConfig details;

    /**
     * @return a string as specified in the constants in IPythonNature
     * @see IPythonNature#PYTHON_VERSION_XXX 
     * @see IPythonNature#JYTHON_VERSION_XXX
     * @see IPythonNature#IRONPYTHON_VERSION_XXX
     */
    public String getProjectType() {
        return details.getSelectedPythonOrJythonAndGrammarVersion();
    }
 
    public String getProjectCreateType() {
        return details.getSelectedPythonOrJythonAndGrammarVersion();
    }
 
    public String getProjectInterpreter() {
        return details.getProjectInterpreter();
    }

    private Listener nameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            //Set the location to the default location if we are set to useDefaults.
            //setLocationForSelection();
            //Sets whether this page is complete.
            //This information is typically used by the wizard to decide
            //when it is okay to move on to the next page or finish up.
            //validatePage()è¿”å›žå½“å‰é¡µçš„æŽ§åˆ¶æƒä¸­æ˜¯å¦åŒ…å«æœ‰æ•ˆå€¼
            setPageComplete(validatePage());
        }
    };

//    private Listener locationModifyListener = new Listener() {
//        public void handleEvent(Event e) {
//            setPageComplete(validatePage());
//        }
//    };

//    protected Button checkSrcFolder;
//    protected Button projectAsSrcFolder;
//    protected Button noSrcFolder;

    // constants
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;

    /**
     * Creates a new project creation wizard page.
     *
     * @param pageName the name of this page
     */
    public NewProjectNameAndLocationWizardPage(String pageName) {
        super(pageName);
        
        setTitle( "OpsDev Project" );
        setDescription( "Create a new OpsDev Project" );
        setPageComplete(false);
        initialLocationFieldValue = Platform.getLocation();
        String opsPath = PathTools.getPlugOpsViewPath();
        initialOpsLocationFieldValue = new Path(opsPath+File.separator+"OPS2.0"+File.separator+"ops");
        
        //
        initialSchemaLocationFieldValue = new Path(opsPath+File.separator+"OPS2.0"+File.separator+"schema");
        customLocationFieldValue = ""; //$NON-NLS-1$
        opsCustomLocationFieldValue="";
        schemaCustomLocationFieldValue = "";
        initialMainFunctionName="defaultfunction";
        
    }
    
    /* (non-Javadoc)
     * Method declared on IDialogPage.
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setFont(parent.getFont());
        
        createProjectNameGroup(composite);//è®²è®¾ç½®projectåå­—çš„ä¸€è¡Œè®¾ç½®å¥½
        createMainFunctionGroup(composite);
        //createOPSLocationGroup(composite);//è®¾ç½®opsä½ç½®çš„è¡Œ
        //createProjectLocationGroup(composite);//è®¾ç½®projectä½ç½®çš„ è¡Œ
        
        
        //createSchemaLocationGroup(composite);//è®¾ç½®schemaä½ç½®è¡Œ
        createProjectDetails(composite);//è®¾ç½®projecttype
       

        PydevPrefs.getPreferences().setValue(IWizardNewProjectNameAndLocationPage.PYDEV_NEW_PROJECT_CREATE_PREFERENCES,
                PYDEV_NEW_PROJECT_CREATE_SRC_FOLDER);


        validatePage();

        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }

    /**
     * @param composite
     */
    private void createProjectDetails(Composite parent) {
        Font font = parent.getFont();
        Composite projectDetails = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        projectDetails.setLayout(layout);
        projectDetails.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectDetails.setFont(font);

        Label projectTypeLabel = new Label(projectDetails, SWT.NONE);
        projectTypeLabel.setFont(font);
        projectTypeLabel.setText( "Project type"  );
        //let him choose the type of the project
        details = new PyProjectPythonDetails.ProjectInterpreterAndGrammarConfig(new ICallback() {

            //Whenever the configuration changes there, we must evaluate whether the page is complete
            public Object call(Object args) throws Exception {
                setPageComplete(NewProjectNameAndLocationWizardPage.this.validatePage());
                return null;
            }
        });//æ·»åŠ é€‰æ‹©project type æ¨¡å—  æš‚æ—¶åˆ é™¤æŽ‰ Jython å’Œ IronPython

        Control createdOn = details.doCreateContents(projectDetails);
        details.setDefaultSelection();
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        createdOn.setLayoutData(data);
    }

    /**
     * Creates the project location specification controls.
     *
     * @param parent the parent composite
     */
    
    
    
    
    
    
    
//    private final void createSchemaLocationGroup(Composite parent) {
//        Font font = parent.getFont();
//        // project specification group
//        Composite projectGroup = new Composite(parent, SWT.NONE);
//        GridLayout layout = new GridLayout();
//        layout.numColumns = 3;
//        projectGroup.setLayout(layout);
//        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//        projectGroup.setFont(font);
//
//        // new project label
////        Label schemaContentsLabel = new Label(projectGroup, SWT.NONE);
////        schemaContentsLabel.setFont(font);
////
////        schemaContentsLabel.setText( "Schema contents" );//åŠ å…¥ä¸€è¡Œæ–‡å­—
////
////        GridData gd_schemaContentsLabel = new GridData();
////        gd_schemaContentsLabel.horizontalSpan = 3;
////        schemaContentsLabel.setLayoutData(gd_schemaContentsLabel);
//
//        final Button useSchemaButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);//åˆ›å»ºä¸€ä¸ªæŒ‰é’®
//        useSchemaButton.setText(  "Use  schema default" );//åœ¨æŒ‰é’®åŽé¢åŠ ä¸€ä¸ªtext
//        useSchemaButton.setSelection(useSchemaDefaults);//é»˜è®¤æ˜¯é€‰æ‹©
//        useSchemaButton.setFont(font);
//
//        GridData gd_useSchemaButton = new GridData();
//        gd_useSchemaButton.horizontalSpan = 3;
//        useSchemaButton.setLayoutData(gd_useSchemaButton);//è®¾ç½®æŒ‰é”®çš„å¸ƒå±€
//
//        createUserSpecifiedSchemaLocationGroup(projectGroup, !useSchemaDefaults);//åˆ›å»ºä¸‹ä¸€è¡Œçš„é»˜è®¤ä½ç½®
//
//        SelectionListener listener = new SelectionAdapter() {//
//            public void widgetSelected(SelectionEvent e) {
//                useSchemaDefaults = useSchemaButton.getSelection();
//                schemabrowseButton.setEnabled(!useSchemaDefaults);
//                schemalocationPathField.setEnabled(!useSchemaDefaults);
//                schemalocationLabel.setEnabled(!useSchemaDefaults);
//                if (useSchemaDefaults) {
//                    schemaCustomLocationFieldValue = schemalocationPathField.getText();
//                    setSchemaPath(schemaCustomLocationFieldValue);
//                    setSchemaLocationForSelection();
//                } else {
//                    schemalocationPathField.setText(schemaCustomLocationFieldValue);
//                }
//            }
//        };
//        useSchemaButton.addSelectionListener(listener);//ç»™ä¸Šé¢çš„æŒ‰é’®æ·»åŠ çš„è§¦å‘å™¨
//    }
    
    
    
    
    
    
    
    
    
//    private final void createProjectLocationGroup(Composite parent) {
//        Font font = parent.getFont();
//        // project specification group
//        Composite projectGroup = new Composite(parent, SWT.NONE);
//        GridLayout layout = new GridLayout();
//        layout.numColumns = 3;
//        projectGroup.setLayout(layout);
//        GridData gd_projectGroup = new GridData(GridData.FILL_HORIZONTAL);
////        gd_projectGroup.heightHint = 67;
//        projectGroup.setLayoutData(gd_projectGroup);
//        projectGroup.setFont(font);
//
//        // new project label
////        Label projectContentsLabel = new Label(projectGroup, SWT.NONE);
////        projectContentsLabel.setFont(font);
////
////        projectContentsLabel.setText( "Project contents" );//åŠ å…¥ä¸€è¡Œæ–‡å­—
////
////        GridData labelData = new GridData();
////        labelData.horizontalSpan = 3;
////        projectContentsLabel.setLayoutData(labelData);
//
//        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);//åˆ›å»ºä¸€ä¸ªæŒ‰é’®
//        useDefaultsButton.setText(  "Use default project contents" );//åœ¨æŒ‰é’®åŽé¢åŠ ä¸€ä¸ªtext
//        useDefaultsButton.setSelection(useDefaults);//é»˜è®¤æ˜¯é€‰æ‹©
//        useDefaultsButton.setFont(font);
//
//        GridData buttonData = new GridData();
//        buttonData.horizontalSpan = 3;
//        useDefaultsButton.setLayoutData(buttonData);//è®¾ç½®æŒ‰é”®çš„å¸ƒå±€
//
//        createUserSpecifiedProjectLocationGroup(projectGroup, !useDefaults);//åˆ›å»ºä¸‹ä¸€è¡Œçš„é»˜è®¤ä½ç½®
//
//        SelectionListener listener = new SelectionAdapter() {//
//            public void widgetSelected(SelectionEvent e) {
//                useDefaults = useDefaultsButton.getSelection();
//                browseButton.setEnabled(!useDefaults);
//                locationPathField.setEnabled(!useDefaults);
//                locationLabel.setEnabled(!useDefaults);
//                if (useDefaults) {
//                    customLocationFieldValue = locationPathField.getText();
//                    setLocationForSelection();
//                } else {
//                    locationPathField.setText(customLocationFieldValue);
//                }
//            }
//        };
//        useDefaultsButton.addSelectionListener(listener);//ç»™ä¸Šé¢çš„æŒ‰é’®æ·»åŠ çš„è§¦å‘å™¨
//    }

    /**
     * Creates the project name specification controls.
     *
     * @param parent the parent composite
     */
    private final void createProjectNameGroup(Composite parent) {
        Font font = parent.getFont();//è®¾ç½®å­—ä½“
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);//
        GridLayout layout = new GridLayout();//åˆ›å»º
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setFont(font);

        projectLabel.setText( "Project name" );
        // new project name entry field
        projectNameField = new Text(projectGroup, SWT.BORDER); //åˆ›å»ºä¸€ä¸ªtextç”¨æ¥è¾“å…¥projectname
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        projectNameField.setLayoutData(data);
        projectNameField.setFont(font);//å¯¹projectnameçš„è¾“å…¥æ¡†çš„è®¾ç½®

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialProjectFieldValue != null)
            projectNameField.setText(initialProjectFieldValue);
        projectNameField.addListener(SWT.Modify, nameModifyListener);
       
    }

    /**
     * Creates the project location specification controls.
     *
     * @param projectGroup the parent composite
     * @param enabled the initial enabled state of the widgets created
     */
//    private void createUserSpecifiedSchemaLocationGroup(Composite projectGroup, boolean enabled) {
//        Font font = projectGroup.getFont();
//        // location label
//        schemalocationLabel = new Label(projectGroup, SWT.NONE);
//        schemalocationLabel.setFont(font);
//        schemalocationLabel.setText( "Directory" );
//        schemalocationLabel.setEnabled(enabled);
//
//        // project location entry field
//        schemalocationPathField = new Text(projectGroup, SWT.BORDER);
//        GridData gd_schemalocationPathField = new GridData(GridData.FILL_HORIZONTAL);
//        gd_schemalocationPathField.widthHint = SIZING_TEXT_FIELD_WIDTH;
//        schemalocationPathField.setLayoutData(gd_schemalocationPathField);
//        schemalocationPathField.setFont(font);
//        schemalocationPathField.setEnabled(enabled);
//
//        // browse button
//        schemabrowseButton = new Button(projectGroup, SWT.PUSH);
//        schemabrowseButton.setFont(font);
//        schemabrowseButton.setText( "Browse" );
//        schemabrowseButton.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                handleLocationBrowseButtonPressed();
//            }
//        });//ç»™æŒ‰é’®æ·»åŠ ä¸€ä¸ªç›‘å¬å™¨,æ˜¯æŒ‰é’®å‘ç”Ÿæ”¹å˜å°±è¿è¡Œç¨‹åºhandleLocationBrowseButtonPressed
//
//        schemabrowseButton.setEnabled(enabled);
//
//        // Set the initial value first before listener
//        // to avoid handling an event during the creation.
//        if (initialSchemaLocationFieldValue != null)
//            schemalocationPathField.setText(getInitialSchemaLocationFieldValue());
//        schemalocationPathField.addListener(SWT.Modify, locationModifyListener);
//    }
    
    
    
    
//    private void createUserSpecifiedProjectLocationGroup(Composite projectGroup, boolean enabled) {
//        Font font = projectGroup.getFont();
//        // location label
//        locationLabel = new Label(projectGroup, SWT.NONE);
//        locationLabel.setFont(font);
//        locationLabel.setText( "Directory" );
//        locationLabel.setEnabled(enabled);
//
//        // project location entry field
//        locationPathField = new Text(projectGroup, SWT.BORDER);
//        GridData data = new GridData(GridData.FILL_HORIZONTAL);
//        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
//        locationPathField.setLayoutData(data);
//        locationPathField.setFont(font);
//        locationPathField.setEnabled(enabled);
//
//        // browse button
//        browseButton = new Button(projectGroup, SWT.PUSH);
//        browseButton.setFont(font);
//        browseButton.setText( "Browse" );
//        browseButton.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent event) {
//                handleLocationBrowseButtonPressed();
//            }
//        });//ç»™æŒ‰é’®æ·»åŠ ä¸€ä¸ªç›‘å¬å™¨,æ˜¯æŒ‰é’®å‘ç”Ÿæ”¹å˜å°±è¿è¡Œç¨‹åºhandleLocationBrowseButtonPressed
//
//        browseButton.setEnabled(enabled);
//
//        // Set the initial value first before listener
//        // to avoid handling an event during the creation.
//        if (initialLocationFieldValue != null)
//            locationPathField.setText(initialLocationFieldValue.toOSString());
//        locationPathField.addListener(SWT.Modify, locationModifyListener);
//    }

    
    
    
    

    /**
     * Returns the current project location path as entered by 
     * the user, or its anticipated initial value.
     *
     * @return the project location path, its anticipated initial value, or <code>null</code>
     *   if no project location path is known
     */
    public IPath getLocationPath() {
        if (useDefaults)
            return initialLocationFieldValue;

        return new Path(getProjectLocationFieldValue());
    }

    /**
     * Creates a project resource handle for the current project name field value.
     * <p>
     * This method does not create the project resource; this is the responsibility
     * of <code>IProject::create</code> invoked by the new project resource wizard.
     * </p>
     *
     * @return the new project resource handle
     */
    public IProject getProjectHandle() {
        return PyStructureConfigHelpers.getProjectHandle(getProjectName());
    }

    /**
     * Returns the current project name as entered by the user, or its anticipated
     * initial value.
     *
     * @return the project name, its anticipated initial value, or <code>null</code>
     *   if no project name is known
     */
    public String getProjectName() {
        if (projectNameField == null)
            return initialProjectFieldValue;

        return getProjectNameFieldValue();
    }

    /**
     * Returns the value of the project name field
     * with leading and trailing spaces removed.
     * 
     * @return the project name in the field
     */
    private String getProjectNameFieldValue() {
        if (projectNameField == null)
            return ""; //$NON-NLS-1$
        else
            return projectNameField.getText().trim();
    }

    /**
     * Returns the value of the project location field
     * with leading and trailing spaces removed.
     * 
     * @return the project location directory in the field
     */
    private String getProjectLocationFieldValue() {
        if (locationPathField == null)
            return ""; //$NON-NLS-1$
        else
            return locationPathField.getText().trim();
    }

    /**
     *  Open an appropriate directory browser
     */
    private void handleLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(schemalocationPathField.getShell());
        dialog.setMessage( "Select the project contents directory" );

        String dirName = getProjectLocationFieldValue();
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customLocationFieldValue = selectedDirectory;
            schemalocationPathField.setText(customLocationFieldValue);
            setSchemaPath(schemalocationPathField.getText());
        }
    }

    /**
     * Returns whether the currently specified project
     * content directory points to an existing project
     */
    private boolean isDotProjectFileInLocation() {
        IPath path = getLocationPath();
        path = path.append(IProjectDescription.DESCRIPTION_FILE_NAME);
        return path.toFile().exists();
    }

    /**
     * Sets the initial project name that this page will use when
     * created. The name is ignored if the createControl(Composite)
     * method has already been called. Leading and trailing spaces
     * in the name are ignored.
     * 
     * @param name initial project name for this page
     */
    /* package */void setInitialProjectName(String name) {
        if (name == null)
            initialProjectFieldValue = null;
        else
            initialProjectFieldValue = name.trim();
    }

    /**
     * Set the location to the default location if we are set to useDefaults.
     */
    private void setLocationForSelection() {
        if (useDefaults) {
            IPath defaultPath = Platform.getLocation().append(getProjectNameFieldValue());
            locationPathField.setText(defaultPath.toOSString());
        }
    }
    
    private void setSchemaLocationForSelection(){
        if(useSchemaDefaults){
            schemalocationPathField.setText(initialSchemaLocationFieldValue.toOSString());
        }
    }
   
    /**
     * Returns whether this page's controls currently all contain valid 
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
//        IWorkspace workspace = ResourcesPlugin.getWorkspace();
//
        String projectFieldContents = getProjectNameFieldValue();
        if (projectFieldContents.equals("")) { //$NON-NLS-1$
            setErrorMessage("Project name is empty");
            //setMessage( "Project name is empty" );
            return false;
        }

//        IStatus nameStatus = workspace.validateName(projectFieldContents, IResource.PROJECT);
//        if (!nameStatus.isOK()) {
//            setErrorMessage(nameStatus.getMessage());
//            return false;
//        }
//
//        String locationFieldContents = getProjectLocationFieldValue();
//
//        if (locationFieldContents.equals("")) { //$NON-NLS-1$
//            setErrorMessage("Project location is empty");
//            //setMessage( "Project location is empty" );
//            return false;
//        }
//
//        IPath path = new Path(""); //$NON-NLS-1$
//        if (!path.isValidPath(locationFieldContents)) {
//            setErrorMessage( "Project location is not valid" );
//            return false;
//        }
//
//    
//
        IProject projectHandle = getProjectHandle();
        if (projectHandle.exists()) {
            setErrorMessage( "Project already exists" );
            return false;
        }
//        if(!checkLocation()){
//            setErrorMessage("OPSlocation must be a OPS and it must have a resouce");
//            return false;
//        }
//      //判断选择schema文件是否为空
//        if(!checkSchemaLocation()){
//            setErrorMessage("Schemalocation must be a schema and it must have a resouce");
//            return false;
//        }
//        
//        if (isDotProjectFileInLocation()) {
//            setErrorMessage(".project found in: " + getLocationPath().toOSString() + " (use import project).");
//            return false;
//        }
//
        if (getProjectInterpreter() == null) {
            setErrorMessage( "Project interpreter not specified" );
            return false;
        }

        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    /*
     * see @DialogPage.setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible)
            projectNameField.setFocus();
    }

    public int getSourceFolderConfigurationStyle() {
        IPreferenceStore preferences = PydevPrefs.getPreferences();
        int srcFolderCreate = preferences
                .getInt(IWizardNewProjectNameAndLocationPage.PYDEV_NEW_PROJECT_CREATE_PREFERENCES);
        switch (srcFolderCreate) {

            case PYDEV_NEW_PROJECT_CREATE_PROJECT_AS_SRC_FOLDER:
                return PYDEV_NEW_PROJECT_CREATE_PROJECT_AS_SRC_FOLDER;
            case PYDEV_NEW_PROJECT_NO_PYTHONPATH:
                return PYDEV_NEW_PROJECT_NO_PYTHONPATH;

            default:
                return PYDEV_NEW_PROJECT_CREATE_SRC_FOLDER;
        }
    }

    public void setProjectName(String projectName) {
        this.projectNameField.setText(projectName);
    }
    
    
    
    
//    private final void createOPSLocationGroup(Composite parent) {
//        Font font = parent.getFont();
//        // project specification group
//        Composite projectGroup = new Composite(parent, SWT.NONE);
//        GridLayout layout = new GridLayout();
//        layout.numColumns = 3;
//        projectGroup.setLayout(layout);
//        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//        projectGroup.setFont(font);
//
//        // new project label
////        Label projectContentsLabel = new Label(projectGroup, SWT.NONE);
////        projectContentsLabel.setFont(font);
////
////        projectContentsLabel.setText( "OPS Server" );//åŠ å…¥ä¸€è¡Œæ–‡å­—
////
////        GridData labelData = new GridData();
////        labelData.horizontalSpan = 3;
////        projectContentsLabel.setLayoutData(labelData);
//
//        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);//åˆ›å»ºä¸€ä¸ªæŒ‰é’®
//        useDefaultsButton.setText( "Use inner ops server" );//åœ¨æŒ‰é’®åŽé¢åŠ ä¸€ä¸ªtext
//        useDefaultsButton.setSelection(useOpsDefaults);//é»˜è®¤æ˜¯é€‰æ‹©
//        useDefaultsButton.setFont(font);
//
//        GridData buttonData = new GridData();
//        buttonData.horizontalSpan = 3;
//        useDefaultsButton.setLayoutData(buttonData);//è®¾ç½®æŒ‰é”®çš„å¸ƒå±€
//
//        createUserSpecifiedOPSLocationGroup(projectGroup, !useDefaults);//åˆ›å»ºä¸‹ä¸€è¡Œçš„é»˜è®¤ä½ç½®
//
//        SelectionListener listener = new SelectionAdapter() {//
//            public void widgetSelected(SelectionEvent e) {
//                useOpsDefaults = useDefaultsButton.getSelection();
//                opsBrowseButton.setEnabled(!useOpsDefaults);
//                opsLocationPathField.setEnabled(!useOpsDefaults);
//                opsLocationLabel.setEnabled(!useOpsDefaults);
//                if (useOpsDefaults) {
//                    opsCustomLocationFieldValue = opsLocationPathField.getText();
//                    setOpsLocationForSelection();
//                } else {
//                    opsLocationPathField.setText(opsCustomLocationFieldValue);
//                }
//            }
//        };
//        
//        useDefaultsButton.addSelectionListener(listener);//ç»™ä¸Šé¢çš„æŒ‰é’®æ·»åŠ çš„è§¦å‘å™¨
//    }

    
    private void createUserSpecifiedOPSLocationGroup(Composite projectGroup, boolean enabled) {
        Font font = projectGroup.getFont();
        // location label
        opsLocationLabel = new Label(projectGroup, SWT.NONE);
        opsLocationLabel.setFont(font);
        opsLocationLabel.setText( "Directory" );
        opsLocationLabel.setEnabled(enabled);

        // project location entry field
        opsLocationPathField = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        opsLocationPathField.setLayoutData(data);
        opsLocationPathField.setFont(font);
        opsLocationPathField.setEnabled(enabled);

        // browse button
        opsBrowseButton = new Button(projectGroup, SWT.PUSH);
        opsBrowseButton.setFont(font);
        opsBrowseButton.setText( "Browse");
        opsBrowseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleOpsLocationBrowseButtonPressed();
            }
        });//ç»™æŒ‰é’®æ·»åŠ ä¸€ä¸ªç›‘å¬å™¨,æ˜¯æŒ‰é’®å‘ç”Ÿæ”¹å˜å°±è¿è¡Œç¨‹åºhandleLocationBrowseButtonPressed

        opsBrowseButton.setEnabled(enabled);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialLocationFieldValue != null)
            opsLocationPathField.setText(getInnerOpsLocationPathField());
        opsLocationPathField.addListener(SWT.Modify, nameModifyListener);
    }
    
    
    /**
     * 
     * @ToDo èŽ·å–é»˜è®¤opsæ’ä»¶çš„ä½ç½®
     * 
     */
    public String getInnerOpsLocationPathField(){
        return initialOpsLocationFieldValue.toFile().getPath();
    }
    
    public String getInitialSchemaLocationFieldValue(){
        return initialSchemaLocationFieldValue.toFile().getPath();
    }
    
    /**
     * useræ‰‹åŠ¨é€‰æ‹©opsè·¯å¾„æ˜¯è°ƒå–çš„æ–¹æ³• å¯ä»¥èŽ·å–
     * @ToDo opsæœåŠ¡å™¨çš„è·¯å¾„æ˜¯å¦ç¬¦åˆè§„èŒƒçš„éªŒè¯
     */
    private void handleOpsLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(opsLocationPathField.getShell());
        dialog.setText( "Select the ops contents directory");
        String dirName = getOpsLocationFieldValue();
        //opsæœåŠ¡å™¨çš„è§„æ ¼åœ¨è¿™ä¸ªåœ°æ–¹è¿›è¡ŒéªŒè¯
        
        if (!dirName.equals("")) { //$NON-NLS-1$
            File path = new File(dirName);
            if (path.exists())
                dialog.setFilterPath(new Path(dirName).toOSString());
        }
        String selectedDirectory = dialog.open();
        if (selectedDirectory!=null) {
            opsCustomLocationFieldValue = selectedDirectory;
            opsLocationPathField.setText(opsCustomLocationFieldValue);
        }
    }
    
    public Path getOpsCustomLocationFieldPath() {
        if (useOpsDefaults)
            return initialOpsLocationFieldValue;

        return new Path(getOpsLocationFieldValue());
    }
    
    private String getOpsLocationFieldValue() {
        if (opsLocationPathField == null)
            return ""; //$NON-NLS-1$
        else
            return opsCustomLocationFieldValue;
    }
    
    private void setOpsLocationForSelection() {
        if (useOpsDefaults) {
            opsLocationPathField.setText(getInnerOpsLocationPathField());
        }
    }
    
    private final void createMainFunctionGroup(Composite parent) {
        Font font = parent.getFont();//è®¾ç½®å­—ä½“
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);//
        GridLayout layout = new GridLayout();//åˆ›å»º
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setFont(font);

        projectLabel.setText( "Main function" );

        // new project name entry field
        mainFunctionNameField = new Text(projectGroup, SWT.BORDER); //åˆ›å»ºä¸€ä¸ªtextç”¨æ¥è¾“å…¥projectname
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        mainFunctionNameField.setLayoutData(data);
        mainFunctionNameField.setFont(font);//å¯¹projectnameçš„è¾“å…¥æ¡†çš„è®¾ç½®

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialProjectFieldValue != null)
            mainFunctionNameField.setText(initialMainFunctionNameFieldValue);
        mainFunctionNameField.addModifyListener(new ModifyListener() {
            
            
            public void modifyText(ModifyEvent e) {
                mainFunctionName = mainFunctionNameField.getText();
            }
        });
    }
    
    public boolean checkLocation(){
        
        File path = new File(opsLocationPathField.getText());
        File resourcePath = new File(opsLocationPathField.getText() + File.separator + "resource");
        if (path.exists() && resourcePath.exists()){
            return true;
        }else{
            return false;
        }
        
    }
  //判断选择的文件是否包含schema文件
    public boolean checkSchemaLocation(){
        
        File path = new File(schemalocationPathField.getText());
        if(path.exists()){
            File[] files = path.listFiles();
            for(File f:files){
                if(f.getName().endsWith(".xsd"));
                return true;
            }
        }
        return false;
        
    }
    
    
    public String getMainFunctionName(){
        
        if(mainFunctionName!=null){
            return mainFunctionName;
        }
        return initialMainFunctionName;
        
    }
    
}

