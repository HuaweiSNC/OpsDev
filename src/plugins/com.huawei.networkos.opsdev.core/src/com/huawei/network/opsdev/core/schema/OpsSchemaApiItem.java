package com.huawei.network.opsdev.core.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * opschame文件的bean
 * @author qWX182698
 *
 */
public class OpsSchemaApiItem {

    private String schemaFile;
    private String name;
    private String text;
    private boolean isSchemaFile = false;
    private List<OpsSchemaApiItem> lstChild = new ArrayList<OpsSchemaApiItem>(); 
    
    public OpsSchemaApiItem()
    {
        
    }

    public OpsSchemaApiItem(String strName, String strText )
    {
        this.name = strName;
        this.text = strText;
    }

    public boolean hasChildren()
    {
        return (this.lstChild.size() > 0);
    }
    
    public void addChild(List<OpsSchemaApiItem> lstTmpChild)
    {
        this.lstChild.addAll(lstTmpChild);
    }
    
    public List<OpsSchemaApiItem> getLstChild() {
        return lstChild;
    }

    public void setLstChild(List<OpsSchemaApiItem> lstChild) {
        this.lstChild = lstChild;
    }

    public void setSchemaFile(String _schemaFile)
    {
        this.schemaFile = _schemaFile;
        this.text = new File(_schemaFile).getName();
        this.isSchemaFile = true;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
 
    public String getSchemaFile() {
        return schemaFile;
    }
 
    public boolean isSchemaFile() {
        return isSchemaFile;
    }

    public void setSchemaFile(boolean isSchemaFile) {
        this.isSchemaFile = isSchemaFile;
    }

    
    public String toString()
    {
        return text;
    }
}
