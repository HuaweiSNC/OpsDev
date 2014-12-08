package com.huawei.networkos.ops.java.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.huawei.network.opsdev.core.templet.ApiJsonXmlPreviewManager;
import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.UrlBean;
import com.huawei.network.opsdev.core.templet.UrlPropertiesBean;
import com.huawei.network.opsdev.core.treeview.node.SchemaFiles;
import com.huawei.network.opsdev.core.utils.TableItemControls;
/**
 * Add Post Body的第二个页面
 * 获得schema的相关属性，填写必填项，以相应的格式在代码中进行体现
 * @author zWX199308
 *
 */

public class AddPostBodyPage2 extends WizardPage {
    private Table table;
    private Text text;
    private ProjectTempletManager manager;
    private boolean canToNextPage = false;
    private TableEditor tedit;
    private boolean isUseInnerSchema;
    private UrlBean currentUrlBean;
    private String choseUrl;



	private boolean isChosed = false;
    private List<UrlPropertiesBean> currentChosedBeands = new ArrayList<UrlPropertiesBean>();
    private List<String> createdBody = new ArrayList<String>();
    private Button jsonRadioButton;
    private Button setMethodRadioButton;
    private Button stringButton;
    private StyledText previewText;
    private List<TableItemControls> controls = new ArrayList<TableItemControls>();
    private Text varianText;
    private Button btnJsonForUrl;
    private Map<String ,String> errorStringMap = new HashMap<String, String>();
    
    public String getChoseUrl() {
		return choseUrl;
	}

	public void setChoseUrl(String choseUrl) {
		this.choseUrl = choseUrl;
	}

    /**
     * Create the wizard.
     */
    public AddPostBodyPage2() {
        super("wizardPage");
        setTitle("Create Post Body");
        setDescription("Post Body description");
    }

    /**
     * Create contents of the wizard.
     * @param parent
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(new GridLayout(6, false));

        Label label = new Label(container, SWT.NONE);
        label.setText("Set Body Properties Values:");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        table = new Table(container, SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));

        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(28);

        TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
        tableColumn_1.setWidth(125);
        tableColumn_1.setText("Name");

        TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
        tableColumn_2.setWidth(65);
        tableColumn_2.setText("Value");

        TableColumn tableColumn_3 = new TableColumn(table, SWT.NONE);
        tableColumn_3.setWidth(65);
        tableColumn_3.setText("Type");
        
        TableColumn tblclmnAccess = new TableColumn(table, SWT.NONE);
        tblclmnAccess.setWidth(90);
        tblclmnAccess.setText("Access");

        TableColumn tableColumn_4 = new TableColumn(table, SWT.NONE);
        tableColumn_4.setWidth(65);
        tableColumn_4.setText("Example");

        Label label_1 = new Label(container, SWT.NONE);
        label_1.setText("Preview:");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        previewText = new StyledText(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.READ_ONLY);
        previewText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
        new Label(container, SWT.NONE);
        

        jsonRadioButton = new Button(container, SWT.RADIO);
        jsonRadioButton.setText("body");
        jsonRadioButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                if (jsonRadioButton.getSelection()) {
                    setMethodRadioButton.setSelection(false);
//                    btnJsonForUrl.setSelection(false);
                }
                changePreview();
                dialogChanged();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });
        jsonRadioButton.setSelection(true);

        setMethodRadioButton = new Button(container, SWT.RADIO);
        setMethodRadioButton.setText("method");
        setMethodRadioButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                if (setMethodRadioButton.getSelection()) {
//                    jsonRadioButton.setSelection(false);
                	jsonRadioButton.setSelection(false);
                }
                changePreview();
                dialogChanged();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Label lblVarian = new Label(container, SWT.NONE);
        lblVarian.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblVarian.setText("Varian:");

        varianText = new Text(container, SWT.BORDER);
        varianText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        varianText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                changePreview();
                dialogChanged();
            }
        });
        isChosed = true;
        table.removeAll();
        for (TableItemControls itemControls : controls) {
            itemControls.dispose();
            
        }
        dialogChanged();
        controls.clear();
        errorStringMap.clear();

    }

    public void createItem(UrlBean currentUrlBean,final String choseUrl) {
        
    	setChoseUrl(choseUrl);
        this.currentUrlBean = currentUrlBean;
        table.removeAll();
        
        // 回收所有行
        for (TableItemControls control : controls) {
            control.dispose();
        }
        // 清空控件
        controls.clear();
        // 清空错误提示信息
        errorStringMap.clear();
        // 清空当前选择的url信息
        currentChosedBeands.clear();
        //画出当前的表
        for (int i = 0; i < currentUrlBean.getAll().size(); i++) {
            final UrlPropertiesBean pBeans = currentUrlBean.getAll().get(i);

            TableItem item = new TableItem(table, SWT.NULL);
            Label lable = new Label(table, SWT.NULL);
            lable.setText( pBeans.getName());
            if(pBeans.isKey()){
                item.setText(1, "[*]"+pBeans.getName());
            }else{
                item.setText(1, pBeans.getName());
            }
            //设置勾选框
            final Button button1 = new Button(table, SWT.CHECK);
            button1.addSelectionListener(new SelectionListener() {
            
                public void widgetSelected(SelectionEvent e) {
                    if (button1.getSelection()) {
                        if (currentChosedBeands.contains(pBeans)) {
                            currentChosedBeands.remove(pBeans);
                        }
                        currentChosedBeands.add(pBeans);
                    } else {
                        if (currentChosedBeands.contains(pBeans)) {
                            currentChosedBeands.remove(pBeans);
                        }
                    }
                    changePreview();
                }

                public void widgetDefaultSelected(SelectionEvent e) {
                    if (button1.getSelection()) {
                        if (currentChosedBeands.contains(pBeans)) {
                            currentChosedBeands.remove(pBeans);
                        }
                        currentChosedBeands.add(pBeans);
                    } else {
                        if (currentChosedBeands.contains(pBeans)) {
                            currentChosedBeands.remove(pBeans);
                        }
                    }
                    changePreview();

                }
            });
            
            if(pBeans.isKey()){
                button1.setSelection(true);
                currentChosedBeands.add(pBeans);
            }
            
            
            TableEditor tedit2 = new TableEditor(table);
            tedit2.grabHorizontal = true;
            tedit2.setEditor(button1, item, 0);
            item.setText(5, pBeans.getExample());
            
            //当前是枚举类型
            if (pBeans.isEnumerate()) {
                //创建一个下拉框
                final CCombo cCombo = new CCombo(table, SWT.NULL);
                for (String str : pBeans.getEnumTexts()) {
                    cCombo.add(str);
                }
                final String errorMessage = pBeans.getName() + "'s value cant't be null \n";

                cCombo.addModifyListener(new ModifyListener() {

                    public void modifyText(ModifyEvent e) {
                        //对下拉框进行监听
                        currentChosedBeands.remove(pBeans);
                        pBeans.setValue(cCombo.getText());
                        currentChosedBeands.add(pBeans);
                        button1.setSelection(true);
                        changePreview();
                    }
                });
                cCombo.setEditable(false);
                TableEditor tedit3 = new TableEditor(table);
                tedit3.grabHorizontal = true;
                tedit3.setEditor(cCombo, item, 2);
                controls.add(new TableItemControls(null, cCombo, button1, new TableEditor[] { tedit2,
                        tedit3 }));
            } 
            //当前是布尔类型
            else if (pBeans.isBoolean()) {
                final CCombo cCombo = new CCombo(table, SWT.NULL);
                cCombo.add("true");
                cCombo.add("false");

                cCombo.addModifyListener(new ModifyListener() {

                    public void modifyText(ModifyEvent e) {
                        currentChosedBeands.remove(pBeans);
                        pBeans.setValue(cCombo.getText());
                        currentChosedBeands.add(pBeans);
                        button1.setSelection(true);
                        changePreview();
                    }
                });
                cCombo.setEditable(false);
                TableEditor tedit3 = new TableEditor(table);
                tedit3.grabHorizontal = true;
                tedit3.setEditor(cCombo, item, 2);
                controls.add(new TableItemControls(null, cCombo, button1, new TableEditor[] { tedit2,
                        tedit3 }));
            } else {
                //当前是文本类型
                final Text text = new Text(table, SWT.NULL);
                TableEditor tedit3 = new TableEditor(table);
                //把勾选框存入数据中
                text.setData(button1);
                tedit3.grabHorizontal = true;
                tedit3.setEditor(text, item, 2);

                final String errorMessage = pBeans.getName()
                        + "'s value is not match the type ,please check it and enter it again\n";
                text.addModifyListener(new ModifyListener() {

                    public void modifyText(ModifyEvent e) {
                        if (currentChosedBeands.contains(pBeans)) {
                            String str = pBeans.setValue(text.getText());
                            if (str!=null) {
                                errorStringMap.put(pBeans.getName(), str);
                            } else {
                                errorStringMap.remove(pBeans.getName());
                            }
                            changePreview();
                        } else {
                            
                            String textValue =text.getText();
                            //判断value列是否有值
                            //判断文本中的值可以用null,也可以用length属性
                            if((textValue != null) && (textValue.length() > 0)){
                                
                                //Button button1 = ( Button) text.getData();
                                //若value列的值不为空则把对应的选中按钮打钩
                                if(button1.getSelection()==false){
                                    pBeans.setValue(textValue);
                                    currentChosedBeands.add(pBeans);
                                    button1.setSelection(true);
                                    changePreview();
                                }else{
                                    changePreview();
                                }
                                
                            }
                            String str = pBeans.setValue(text.getText());
                            if (str!=null) {
                                errorStringMap.put(pBeans.getName(), str);
                            } else {
                                errorStringMap.remove(pBeans.getName());
                            }
                            changePreview();
                            dialogChanged();
                        }
                    }

                });
                controls.add(new TableItemControls(text, null, button1, new TableEditor[] { tedit2,
                        tedit3 }));

            }
            item.setText(3, pBeans.getType());
            item.setText(4, (pBeans.getAccess() == null ? "":pBeans.getAccess()));
        }
        dialogChanged();
    }

    private void changePreview() {
        StringBuffer str = new StringBuffer();
        if (isCurrentChosedBeandsNotNull()) {
        	String url = getChoseUrl();
        	String[] name = url.split("/");
        	String firstName = name[1];
        	String fFirstChar = firstName.substring(0, 1).toUpperCase();
        	String fOtherChar = firstName.substring(1);
        	String lastName = name[name.length-1];
        	String lFirstChar = lastName.substring(0, 1).toUpperCase();
        	String lOtherChar = lastName.substring(1);
        	//获得选择的URL所对应的类
        	String className = fFirstChar + fOtherChar + lFirstChar + lOtherChar;
        	if (jsonRadioButton.getSelection() && currentChosedBeands.size() > 0) {
                createdBody = ApiJsonXmlPreviewManager.showJson(currentChosedBeands, varianText.getText());
                for (String str1 : createdBody) {
                    str.append(str1 + "\n");
                }
            }else if (setMethodRadioButton.getSelection() && currentChosedBeands.size() > 0) {
            	//获得所有参数的个数
            	int beansNum = currentUrlBean.getAll().size();
            	//获得最后一个参数
            	UrlPropertiesBean pBeans = currentUrlBean.getAll().get(beansNum-1);
            	//判断最后一个参数是否是key
            	boolean flag = pBeans.isKey();
                createdBody = ApiJsonXmlPreviewManager.showMethon(currentChosedBeands, varianText.getText(),className,beansNum,flag,pBeans);
                for (String str1 : createdBody) {
                    str.append(str1 + "\n");
                }
            } 
        }
        previewText.setText(str.toString());
        dialogChanged();
    }
    
    //获得preview中的数据
    public String getPreviewContext(){
    	StringBuffer str = new StringBuffer();
    	List<String> createdBody = getCreatedBody();
    	for (String str1 : createdBody) {
        	str.append(str1);
        }
    	return str.toString();
    }

    public List<String> getCreatedBody() {
        return createdBody;
    }

    public void setCreatedBody(List<String> createdBody) {
        this.createdBody = createdBody;
    }

    /**
     * 改变页面的日志 只有我们选择了url 才能点击完成按钮, 不然将显示相应的需要改正的错误,并且不能进入到下一步
     */
    private void dialogChanged() {

        if (errorStringMap.size() != 0) {
            StringBuffer error = new StringBuffer();
            for (String str : errorStringMap.values()) {
                error.append(str);
            }
            updateStatus(error.toString());
            return;
        }
        if (currentChosedBeands.size() == 0) {
            updateStatus("You should chose at least one attribute!");
            return;
        }
        if (!isCurrentChosedBeandsNotNull()) {
            updateStatus("The attribute value you chosed cant't be null");
            return;
        }
        if(varianText.getText() == ""){
            updateStatus("The Varian value cant't be null");
            return;
        }
        //画出当前的表
        for (int i = 0; i < currentUrlBean.getAll().size(); i++) {
            final UrlPropertiesBean pBeans = currentUrlBean.getAll().get(i);
            if (pBeans.isKey() && !currentChosedBeands.contains(pBeans))
            {
                updateStatus("The " + pBeans.getName() + " must be select");
                return;
            }
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);

        if(message == null){
            setPageComplete(true);
            IWizardPage iWizardPage  =     getPreviousPage();
            if(iWizardPage instanceof AddPostBodyPage){
                ((AddPostBodyPage)iWizardPage).setPageComplete(true);
            }
        }else{
            IWizardPage iWizardPage  =     getPreviousPage();
            if(iWizardPage instanceof AddPostBodyPage){
                ((AddPostBodyPage)iWizardPage).setPageComplete(false);
            }
        }
    }
    
    public void changeIsUseInnerOps(SchemaFiles files) {
        if ("InnerSchemaFiles".equals(files.toString())) {
            isUseInnerSchema = true;
        } else {
            isUseInnerSchema = false;
        }
    }

    private boolean isCurrentChosedBeandsNotNull() {
        for (UrlPropertiesBean bean : currentChosedBeands) {
            if (bean.getValue() == null || bean.getValue().equals("")) {
                return false;
            }
        }
        return true;
    }
}
