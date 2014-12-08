package com.huawei.networkos.ops.java.wizards;

import java.io.File;

import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.huawei.network.opsdev.core.utils.PathTools;
/**
 * 配置工程相关路径的页面
 * @author zWX199308
 *
 */

 

public class NewProjectNameAndLocationWizardPage extends WizardPage  {

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

    /**
     * @return a string as specified in the constants in IPythonNature
     * @see IPythonNature#PYTHON_VERSION_XXX 
     * @see IPythonNature#JYTHON_VERSION_XXX
     * @see IPythonNature#IRONPYTHON_VERSION_XXX
     */

    private Listener nameModifyListener = new Listener() {
        public void handleEvent(Event e) {
            setLocationForSelection();
            setPageComplete(validatePage());
        }
    };

    private Listener locationModifyListener = new Listener() {
        public void handleEvent(Event e) {
            setPageComplete(validatePage());
        }
    };
    // constants
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;

    /**
     * Creates a new project creation wizard page.
     */
    public NewProjectNameAndLocationWizardPage(String pageName) {
        super(pageName);
        setTitle( "OpsDev Java Project" );
        setDescription( "Config the new Project" );
        setPageComplete(true);
        initialLocationFieldValue = Platform.getLocation();
        String opsPath = PathTools.getPlugOpsViewPath();
        initialOpsLocationFieldValue = new Path(opsPath+File.separator+"OPS2.0"+File.separator+"ops");
        
        //
        initialSchemaLocationFieldValue = new Path(opsPath+File.separator+"OPS2.0"+File.separator+"schema");
        customLocationFieldValue = ""; 
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
        
        createOPSLocationGroup(composite);
        createProjectLocationGroup(composite);
        
        
        createSchemaLocationGroup(composite);
        validatePage();
        // Show description on opening
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }
    private final void createSchemaLocationGroup(Composite parent) {
        Font font = parent.getFont();
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);

        final Button useSchemaButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
        useSchemaButton.setText(  "Use  schema default" );
        useSchemaButton.setSelection(useSchemaDefaults);
        useSchemaButton.setFont(font);

        GridData gd_useSchemaButton = new GridData();
        gd_useSchemaButton.horizontalSpan = 3;
        useSchemaButton.setLayoutData(gd_useSchemaButton);

        createUserSpecifiedSchemaLocationGroup(projectGroup, !useSchemaDefaults);

        SelectionListener listener = new SelectionAdapter() {//
            public void widgetSelected(SelectionEvent e) {
                useSchemaDefaults = useSchemaButton.getSelection();
                schemabrowseButton.setEnabled(!useSchemaDefaults);
                schemalocationPathField.setEnabled(!useSchemaDefaults);
                schemalocationLabel.setEnabled(!useSchemaDefaults);
                if (useSchemaDefaults) {
                    schemaCustomLocationFieldValue = schemalocationPathField.getText();
                    setSchemaPath(schemaCustomLocationFieldValue);
                    setSchemaLocationForSelection();
                } else {
                    schemalocationPathField.setText(schemaCustomLocationFieldValue);
                }
            }
        };
        useSchemaButton.addSelectionListener(listener);
    }

    private final void createProjectLocationGroup(Composite parent) {
        Font font = parent.getFont();
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        GridData gd_projectGroup = new GridData(GridData.FILL_HORIZONTAL);
//        gd_projectGroup.heightHint = 67;
        projectGroup.setLayoutData(gd_projectGroup);
        projectGroup.setFont(font);

        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
        useDefaultsButton.setText(  "Use default project contents" );
        useDefaultsButton.setSelection(useDefaults);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

        createUserSpecifiedProjectLocationGroup(projectGroup, !useDefaults);

        SelectionListener listener = new SelectionAdapter() {//
            public void widgetSelected(SelectionEvent e) {
                useDefaults = useDefaultsButton.getSelection();
                browseButton.setEnabled(!useDefaults);
                locationPathField.setEnabled(!useDefaults);
                locationLabel.setEnabled(!useDefaults);
                if (useDefaults) {
                    customLocationFieldValue = locationPathField.getText();
                    setLocationForSelection();
                } else {
                    locationPathField.setText(customLocationFieldValue);
                }
            }
        };
        useDefaultsButton.addSelectionListener(listener);
    }

    /**
     * Creates the project location specification controls.
     *
     * @param projectGroup the parent composite
     * @param enabled the initial enabled state of the widgets created
     */
    private void createUserSpecifiedSchemaLocationGroup(Composite projectGroup, boolean enabled) {
        Font font = projectGroup.getFont();
        // location label
        schemalocationLabel = new Label(projectGroup, SWT.NONE);
        schemalocationLabel.setFont(font);
        schemalocationLabel.setText( "Directory" );
        schemalocationLabel.setEnabled(enabled);

        // project location entry field
        schemalocationPathField = new Text(projectGroup, SWT.BORDER);
        GridData gd_schemalocationPathField = new GridData(GridData.FILL_HORIZONTAL);
        gd_schemalocationPathField.widthHint = SIZING_TEXT_FIELD_WIDTH;
        schemalocationPathField.setLayoutData(gd_schemalocationPathField);
        schemalocationPathField.setFont(font);
        schemalocationPathField.setEnabled(enabled);

        // browse button
        schemabrowseButton = new Button(projectGroup, SWT.PUSH);
        schemabrowseButton.setFont(font);
        schemabrowseButton.setText( "Browse" );
        schemabrowseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleLocationBrowseButtonPressed();
            }
        });

        schemabrowseButton.setEnabled(enabled);
        if (initialLocationFieldValue != null)
            schemalocationPathField.setText(initialSchemaLocationFieldValue.toOSString());
        schemalocationPathField.addListener(SWT.Modify, locationModifyListener);
    }
    
    
    
    
    private void createUserSpecifiedProjectLocationGroup(Composite projectGroup, boolean enabled) {
        Font font = projectGroup.getFont();
        // location label
        locationLabel = new Label(projectGroup, SWT.NONE);
        locationLabel.setFont(font);
        locationLabel.setText( "Directory" );
        locationLabel.setEnabled(enabled);

        // project location entry field
        locationPathField = new Text(projectGroup, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathField.setLayoutData(data);
        locationPathField.setFont(font);
        locationPathField.setEnabled(enabled);

        // browse button
        browseButton = new Button(projectGroup, SWT.PUSH);
        browseButton.setFont(font);
        browseButton.setText( "Browse" );
        browseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                handleLocationBrowseButtonPressed();
            }
        });

        browseButton.setEnabled(enabled);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialLocationFieldValue != null)
            locationPathField.setText(initialLocationFieldValue.toOSString());
        locationPathField.addListener(SWT.Modify, locationModifyListener);
    }
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
            return "";
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
            return ""; 
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
        if (!dirName.equals("")) { 
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
        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        if(!checkLocation()){
            setErrorMessage("OPSlocation must be a OPS and it must have a resouce");
            return false;
        }
        
      //判断选择schema文件是否为空
        if(!checkSchemaLocation()){
            setErrorMessage("Schemalocation must be a schema and it must have a resouce");
            return false;
        }
        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    private final void createOPSLocationGroup(Composite parent) {
        Font font = parent.getFont();
        // project specification group
        Composite projectGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);

        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
        useDefaultsButton.setText( "Use inner ops server" );
        useDefaultsButton.setSelection(useOpsDefaults);
        useDefaultsButton.setFont(font);

        GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        useDefaultsButton.setLayoutData(buttonData);

        createUserSpecifiedOPSLocationGroup(projectGroup, !useDefaults);

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                useOpsDefaults = useDefaultsButton.getSelection();
                opsBrowseButton.setEnabled(!useOpsDefaults);
                opsLocationPathField.setEnabled(!useOpsDefaults);
                opsLocationLabel.setEnabled(!useOpsDefaults);
                if (useOpsDefaults) {
                    opsCustomLocationFieldValue = opsLocationPathField.getText();
                    setOpsLocationForSelection();
                } else {
                    opsLocationPathField.setText(opsCustomLocationFieldValue);
                }
            }
        };
        
        useDefaultsButton.addSelectionListener(listener);
    }

    
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
        });
        opsBrowseButton.setEnabled(enabled);

        // Set the initial value first before listener
        // to avoid handling an event during the creation.
        if (initialLocationFieldValue != null)
            opsLocationPathField.setText(getInnerOpsLocationPathField());
        opsLocationPathField.addListener(SWT.Modify, nameModifyListener);
    }
   
    public String getInnerOpsLocationPathField(){
        return initialOpsLocationFieldValue.toFile().getPath();
    }
    
    
    private void handleOpsLocationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(opsLocationPathField.getShell());
        dialog.setText( "Select the ops contents directory");
        String dirName = getOpsLocationFieldValue();
        
        if (!dirName.equals("")) { 
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
            return ""; 
        else
            return opsCustomLocationFieldValue;
    }
    
    private void setOpsLocationForSelection() {
        if (useOpsDefaults) {
            opsLocationPathField.setText(getInnerOpsLocationPathField());
        }
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
            return true;
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

