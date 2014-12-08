package com.huawei.networkos.ops.properties;
import java.io.File;

import org.eclipse.core.resources.IProject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;
import org.eclipse.swt.widgets.Group;
/**
 * 工程属性页面，可以修改工程的配置路径
 */
public class OpsPathPropertyPage extends PropertyPage {

	private boolean useOpsDefaults = false;
	private boolean useOpsDefaults_3 = false;
	//判断是否使用默认选择
	private boolean useOpsSchemaDefault = false;
	private Path initialOpsLocationFieldValue;

	private Path initialOpsapiLocationFieldValue;
	
	//文本框中默认的shenma文件路径
	private Path initialOpsSchemaLocationFieldValue;

	private Label opsLocationLabel;
	private Button opsBrowseButton;
	private Text opsLocationPathField;

	private static final int SIZING_TEXT_FIELD_WIDTH = 250;
	private String opsCustomLocationFieldValue;

	private IProject iProject;
	private IProgressMonitor monitor = new NullProgressMonitor();
	private String customOpsLocation;
	private ProjectConfigXmlManager configXmlManager;

	private Label opsapiLocationLabel;
	private Text opsapiLocationPathField;
	private Button opsapiBrowseButton;
	private String customOpsapiLocation;

	private String opsCustomapiLocationFieldValue;
	//private Composite projectGroup_1;

	//schema控件
	private Label opsSchemaLocationLabel;
	private Text opsSchemaLocationPathField;
	private Button opsSchemaBrowseButton;
	//文本框中自定义的schema文件路径
	private String opsCustomSchemaLocationFieldValue;
	//自定义schema文件路径
	private String customOpsSchemaLocation;
	//存放schema文件路径
	private String schemaPath = "";
	private Group projectGroup_1;
	 

	public String getSchemaPath() {
		return schemaPath;
	}

	public void setSchemaPath(String schemaPath) {
		this.schemaPath = schemaPath;
	}

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public OpsPathPropertyPage() {
		super();
	}

	/**
	 * 
	 * @param parent
	 */
	private void addFirstSection(Composite parent) {
		 
	}

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
	}

	/**
	 * 初始化properties页面工作
	 * 
	 * @param parent
	 */
	private void addSecondSection(Composite parent) {
	 
		iProject = PathTools.getCurrentProject();

		String opsPath = PathTools.getPlugOpsViewPath();
		initialOpsLocationFieldValue = new Path(opsPath + File.separator
				+ "OPS2.0" + File.separator + "ops");

		initialOpsapiLocationFieldValue = new Path(opsPath + File.separator
				+ "OPS2.0" + File.separator + "document");
		
		//获得文本框中初始化的schema文件路径
		initialOpsSchemaLocationFieldValue = new Path(opsPath + File.separator
				+ "OPS2.0" + File.separator + "schema");
		

		configXmlManager = OpsManagerProjectTool.getConfigManager(iProject);
		

		customOpsLocation = configXmlManager.getOpspath();// 获取当前项目的Opslocation

		customOpsapiLocation = configXmlManager.getOpsapiDocpath();// 获取当前项目的Opsapilocation
		
		//获得当前项目的schemaLocation
		customOpsSchemaLocation = configXmlManager.getSchemaRootPath();

		if (null != customOpsLocation && !customOpsLocation.isEmpty()) {
			customOpsLocation = (new Path(customOpsLocation))
					.toOSString();
			if (customOpsLocation.equals(initialOpsLocationFieldValue
					.toOSString())) {
				useOpsDefaults = true;
			}
		}

		if (null != customOpsapiLocation && !customOpsapiLocation.isEmpty()) {
			customOpsapiLocation = (new Path(customOpsapiLocation))
					.toOSString();
			if (customOpsapiLocation.equals(initialOpsapiLocationFieldValue
					.toOSString())) {
				useOpsDefaults_3 = true;
			}

		}
		
		//判断当前的schema路径是否是默认的
		if(null != customOpsSchemaLocation && !customOpsSchemaLocation.isEmpty()){
			customOpsSchemaLocation = (new Path(customOpsSchemaLocation))
					.toOSString();
		}

		createOPSLocationGroup(parent);

		opsCustomLocationFieldValue = initialOpsLocationFieldValue.toOSString();

		opsCustomapiLocationFieldValue = initialOpsapiLocationFieldValue
				.toOSString();
		
		//把默认的schema路径的值传给文本框
		opsCustomSchemaLocationFieldValue = initialOpsSchemaLocationFieldValue
				.toOSString();

	}

	/**
	 * 创建一个新的ops路径的ui 并添加一些必要的监听器
	 * 
	 * @param parent
	 */
	private final void createOPSLocationGroup(Composite parent) {
		Font font = parent.getFont();
		
		projectGroup_1 = new Group(parent, SWT.NONE);
		projectGroup_1.setText("OPS Path");

		// project specification group
		//projectGroup_1 = new Composite(parent, SWT.NONE);
		GridLayout gl_projectGroup_1 = new GridLayout();
		gl_projectGroup_1.numColumns = 3;
		projectGroup_1.setLayout(gl_projectGroup_1);
		projectGroup_1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectGroup_1.setFont(font);

		final Button useDefaultsButton = new Button(projectGroup_1, SWT.CHECK
				| SWT.RIGHT);// 创建一个按钮
		useDefaultsButton.setText("Use inner ops server");// 在按钮后面加一个text
		useDefaultsButton.setSelection(true);// 默认是选择
		useDefaultsButton.setFont(font);

		GridData buttonData = new GridData();
		buttonData.horizontalSpan = 3;
		useDefaultsButton.setLayoutData(buttonData);// 设置按键的布局

		createUserSpecifiedOPSLocationGroup(projectGroup_1, !useOpsDefaults);

		// new apiButton
		final Button apiButton = new Button(projectGroup_1, SWT.CHECK
				| SWT.RIGHT);// 创建一个按钮
		apiButton.setText("use inner schema api help document ");// 在按钮后面加一个text
		apiButton.setSelection(true);// 默认是选择
		apiButton.setFont(font);

		GridData buttonDatafor = new GridData();
		buttonDatafor.horizontalSpan = 3;
		apiButton.setLayoutData(buttonDatafor);// 设置按键的布局

		createUserSpecifiedOPSapiLocationGroup(projectGroup_1,
				!useOpsDefaults_3);
		
		//创建schema按钮
		final Button schemaButton = new Button(projectGroup_1, SWT.CHECK
				| SWT.RIGHT);
		schemaButton.setText("use inner schema");// 在按钮后面加一个text
		schemaButton.setSelection(true);// 默认是选择
		schemaButton.setFont(font);

		GridData buttonDataschema = new GridData();
		buttonDataschema.horizontalSpan = 3;
		schemaButton.setLayoutData(buttonDataschema);// 设置按键的布局
		createUserSpecifiedOpsSchemaLocationGroup(projectGroup_1,useOpsSchemaDefault);

		SelectionListener listener = new SelectionAdapter() {//
			public void widgetSelected(SelectionEvent e) {
				useOpsDefaults = useDefaultsButton.getSelection();
				opsBrowseButton.setEnabled(!useOpsDefaults);
				opsLocationPathField.setEnabled(!useOpsDefaults);
				opsLocationLabel.setEnabled(!useOpsDefaults);
				if (useOpsDefaults) {
					opsCustomLocationFieldValue = opsLocationPathField
							.getText();
					setOpsLocationForSelection();
				} else {
					opsLocationPathField.setText(opsCustomLocationFieldValue);
				}
			}
		};
		useDefaultsButton.addSelectionListener(listener);// 给上面的按钮添加的触发器

		SelectionListener listenerthr = new SelectionAdapter() {//
			public void widgetSelected(SelectionEvent e) {
				useOpsDefaults_3 = apiButton.getSelection();
				opsapiBrowseButton.setEnabled(!useOpsDefaults_3);
				opsapiLocationPathField.setEnabled(!useOpsDefaults_3);
				opsapiLocationLabel.setEnabled(!useOpsDefaults_3);
				if (useOpsDefaults_3) {
					opsCustomapiLocationFieldValue = opsapiLocationPathField
							.getText();
					setOpsapiLocationForSelection();
				} else {
					opsapiLocationPathField
							.setText(opsCustomapiLocationFieldValue);
				}
			}
		};
		apiButton.addSelectionListener(listenerthr);
		
		SelectionListener listenerschema = new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				useOpsSchemaDefault = schemaButton.getSelection();
				opsSchemaBrowseButton.setEnabled(!useOpsSchemaDefault);
				opsSchemaLocationPathField.setEnabled(!useOpsSchemaDefault);
				opsSchemaLocationLabel.setEnabled(!useOpsSchemaDefault);
				
				opsCustomSchemaLocationFieldValue = opsSchemaLocationPathField.getText();
				opsSchemaLocationPathField.setText(customOpsSchemaLocation);
				setSchemaPath(opsSchemaLocationPathField.getText());
				//setOpsSchemaLocationForSelection();
			}
		};
		schemaButton.addSelectionListener(listenerschema);

	}

	/**
	 * 如果我们选择了 useDefault 按钮 那么将使用插件内置的ops路径
	 */
	private void setOpsLocationForSelection() {
		if (useOpsDefaults) {
			opsLocationPathField.setText(getInnerOpsLocationPathField());
		}
	}

	private void setOpsapiLocationForSelection() {
		if (useOpsDefaults_3) {
			opsapiLocationPathField.setText(getInnerOpsapiLocationPathField());
		}
	}
	
	private void setOpsSchemaLocationForSelection(){
		if(useOpsSchemaDefault){
			opsSchemaLocationPathField.setText(getInnerOpsSchemaLocationPathField());
		}
	}

	/**
	 * 获取内置的ops路径
	 * 
	 * @return
	 */
	public String getInnerOpsLocationPathField() {
		return initialOpsLocationFieldValue.toFile().getPath();

	}

	public String getInnerOpsapiLocationPathField() {
		return initialOpsapiLocationFieldValue.toFile().getPath();

	}
	
	public String getInnerOpsSchemaLocationPathField(){
		return initialOpsSchemaLocationFieldValue.toFile().getPath();
	}

	/**
	 * 创建ui
	 * 
	 * @param projectGroup
	 * @param 是否使用默认的Ops路径
	 */
	private void createUserSpecifiedOPSLocationGroup(Composite projectGroup,
			boolean enabled) {
		Font font = projectGroup.getFont();
		// location label
		opsLocationLabel = new Label(projectGroup, SWT.NONE);
		opsLocationLabel.setFont(font);
		opsLocationLabel.setText("Directory");
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
		opsBrowseButton.setText("Browse");
		opsBrowseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handleOpsLocationBrowseButtonPressed();
			}
		});

		opsBrowseButton.setEnabled(enabled);

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		if (customOpsLocation != null)
			opsLocationPathField.setText(customOpsLocation);
	}

	private void createUserSpecifiedOPSapiLocationGroup(Composite projectGroup,
			boolean enabled) {
		Font font = projectGroup.getFont();
		// apilocation label
		opsapiLocationLabel = new Label(projectGroup, SWT.NONE);
		opsapiLocationLabel.setFont(font);
		opsapiLocationLabel.setText("Directory");
		opsapiLocationLabel.setEnabled(enabled);

		// project api location entry field
		opsapiLocationPathField = new Text(projectGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		opsapiLocationPathField.setLayoutData(data);
		opsapiLocationPathField.setFont(font);
		opsapiLocationPathField.setEnabled(enabled);

		// browse api button
		opsapiBrowseButton = new Button(projectGroup, SWT.PUSH);
		opsapiBrowseButton.setFont(font);
		opsapiBrowseButton.setText("Browse");
		opsapiBrowseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handleOpsapiLocationBrowseButtonPressed();
			}
		});

		opsapiBrowseButton.setEnabled(enabled);

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		if (customOpsapiLocation != null)
			opsapiLocationPathField.setText(customOpsapiLocation);
	}
	
	/**
	 * 创建schemaGroup
	 */
	private void createUserSpecifiedOpsSchemaLocationGroup(Composite projectGroup,
			boolean enabled){
		Font font = projectGroup.getFont();
		
		opsSchemaLocationLabel = new Label(projectGroup, SWT.NONE);
		opsSchemaLocationLabel.setFont(font);
		opsSchemaLocationLabel.setText("Directory");
		opsSchemaLocationLabel.setEnabled(enabled);
		
		opsSchemaLocationPathField = new Text(projectGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		opsSchemaLocationPathField.setLayoutData(data);
		opsSchemaLocationPathField.setFont(font);
		opsSchemaLocationPathField.setEnabled(enabled);
		
		opsSchemaBrowseButton = new Button(projectGroup, SWT.PUSH);
		opsSchemaBrowseButton.setFont(font);
		opsSchemaBrowseButton.setText("Browse");
		opsSchemaBrowseButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event){
				handleOpsSchemaLocationBrowseButtonPressed();
			}
		});
		
		opsSchemaBrowseButton.setEnabled(enabled);
		if(customOpsSchemaLocation != null){
			opsSchemaLocationPathField.setText(customOpsSchemaLocation);
			setSchemaPath(opsSchemaLocationPathField.getText());
		}	
	}
	

	/**
	 * 获取当前设置的ops路径
	 * 
	 * @return
	 */
	private String getOpsLocationFieldValue() {
		if (opsLocationPathField == null)
			return ""; 
		else
			return opsCustomLocationFieldValue;
	}

	private String getOpsapiLocationFieldValue() {
		if (opsapiLocationPathField == null)
			return "";
		else
			return opsCustomapiLocationFieldValue;
	}
	
	//
	private String getOpsSchemaLocationFieldValue(){
		if(opsSchemaLocationPathField == null)
			return "";
		else
			return opsCustomSchemaLocationFieldValue;
	}

	/**
	 * 为browser按钮添加一个dialog
	 */
	private void handleOpsLocationBrowseButtonPressed() {
		DirectoryDialog dialog = new DirectoryDialog(
				opsLocationPathField.getShell());
		dialog.setText("Select_the_ops_contents_directory");
		String dirName = getOpsLocationFieldValue();
		// ops服务器的规格在这个地方进行验证
		if (dirName != null && !dirName.equals("")) { 
			File path = new File(dirName);
			File resourcePath = new File(dirName + File.separator + "resource");
			if (path.exists() && resourcePath.exists()
					&& !resourcePath.isFile()) {
				dialog.setFilterPath(new Path(dirName).toOSString());
			}
		}
		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			opsCustomLocationFieldValue = selectedDirectory;
			opsLocationPathField.setText(opsCustomLocationFieldValue);
		}
	}

	private void handleOpsapiLocationBrowseButtonPressed() {
		DirectoryDialog dialog = new DirectoryDialog(
				opsapiLocationPathField.getShell());
		dialog.setText("Select_the_ops_contents_directory");
		String dirName = getOpsapiLocationFieldValue();
		// ops服务器的规格在这个地方进行验证
		if (dirName != null && !dirName.equals("")) { 
			File path = new File(dirName);
			File resourcePath = new File(dirName + File.separator + "resource");
			if (path.exists() && resourcePath.exists()
					&& !resourcePath.isFile()) {
				dialog.setFilterPath(new Path(dirName).toOSString());
			}
		}
		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			opsCustomapiLocationFieldValue = selectedDirectory;
			opsapiLocationPathField.setText(opsCustomapiLocationFieldValue);
		}
	}
	
	private void handleOpsSchemaLocationBrowseButtonPressed(){
		DirectoryDialog dialog = new DirectoryDialog(
				opsSchemaLocationPathField.getShell());
		dialog.setText("Select_the_ops_contents_directory");
		String dirName = getOpsSchemaLocationFieldValue();
		if (dirName != null && !dirName.equals("")){
			File path = new File(dirName);
			File resourcePath = new File(dirName + File.separator + "schema");
			if (path.exists() && resourcePath.exists()
					&& !resourcePath.isFile()) {
				dialog.setFilterPath(new Path(dirName).toOSString());
			}
		}
		String selectedDirectory = dialog.open();
		if(selectedDirectory != null){
			opsCustomSchemaLocationFieldValue = selectedDirectory;
			opsSchemaLocationPathField.setText(opsCustomSchemaLocationFieldValue);
			setSchemaPath(opsSchemaLocationPathField.getText());
		}
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		addFirstSection(composite);
		addSeparator(composite);
		addSecondSection(composite);
		
		OpsManagerProjectTool.reLoadProject(ProjectPathManager.getCurrentProject(), null, "java");
		return composite;
	}
 

	/**
	 * 设置default按钮按下以后触发的事件 使用默认地址
	 */
	protected void performDefaults() {
		super.performDefaults();
		opsLocationPathField.setText(customOpsLocation);
		opsCustomLocationFieldValue = opsLocationPathField.getText();

		opsapiLocationPathField.setText(customOpsapiLocation);
		opsCustomapiLocationFieldValue = opsapiLocationPathField.getText();
		
		opsSchemaLocationPathField.setText(customOpsSchemaLocation);
		opsCustomSchemaLocationFieldValue = opsSchemaLocationPathField.getText();
	}

	
	/**
	 * 设置ok按钮 和apply按钮所触发的事件 如果ops地址符合规范并且
	 */
	public boolean performOk() {
		if (opsCustomLocationFieldValue != null
				&& !opsCustomLocationFieldValue.equals("")) {
 
			String newSchema  = getSchemaPath();
			String oldSchema = configXmlManager.getSchemaRootPath();
			
			configXmlManager.setOpsapiDocpath(customOpsapiLocation);
			configXmlManager.setOpspath(customOpsLocation);
			configXmlManager.setSchemaRootPath(newSchema);
			
			if (newSchema != null && !newSchema.equalsIgnoreCase(oldSchema))
			{
				OpsManagerProjectTool.resetMainSchemaPath(iProject, newSchema);
			}
			
			return configXmlManager.configOPSPath( monitor, iProject);
		 
		} else {
			MessageDialog.openWarning(getShell(), "Location missed",
					"OPS location cann't be null");
		}

		return false;

	}

 

}