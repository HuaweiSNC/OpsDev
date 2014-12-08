package com.huawei.tools.xml.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.PathTools;

public class ProjectConfigXmlManager {
    private Document document;
    private String projectType = "python";
    private String projectMonitorState = "off";
    private String xmlPath;
    // 记录当前OPS2.0所在的目录
    private String opspath = "";
    // 记录当前api文档是存放路径
    private String opsapiDocpath = "";
    // 记录当前工程引用的schema文件路径
    private String schemaRootPath = "";
    
    //记录创建当前工程的pluginID
    private String plugID = "";
    //记录创建当前工程的templetXML
    private String projectTemplet = "";
    
    private String type ="";
    private String state = "";
    // 自定义的schema路径
    private List<String> userSchemaFiles = new ArrayList<String>();
    ProjectConfigXmlManager projectconfigXmlManager;
    
    
    public String getPlugID() {
        return plugID;
    }


    public void setPlugID(String pluginID) {
        this.plugID = pluginID;
    }


    public String getProjectTempletXml() {
        return projectTemplet;
    }


    public void setProjectTempletXml(String projectTempletXml) {
        this.projectTemplet = projectTempletXml;
    }
    
    
    public String getOpsapiDocpath() {
        return opsapiDocpath;
    }


    public void setOpsapiDocpath(String opsapiDocpath) {
        this.opsapiDocpath = opsapiDocpath;
    }


    public String getProjectType(){
        return projectType;
    }



    public String getProjectMonitorState() {
        return projectMonitorState;
    }


    public String getOpspath() {
        return opspath;
    }


    public void setOpspath(String opspath) {
        this.opspath = opspath;
    }
  
   


    public String getSchemaRootPath() {
        return schemaRootPath;
    }


    public void setSchemaRootPath(String apifiles) {
        this.schemaRootPath = apifiles;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }

    public ProjectConfigXmlManager(String xmlPath){
        this.xmlPath = xmlPath;
        ConfigXmlManager configXmlManager = new ConfigXmlManager(xmlPath);
        document  = configXmlManager.getDocument();
    }
  
    public void initXmlConfig(String opspath, String schemaPath, String opsapiDocpath , String type)
    {
        
        this.opspath = opspath;
        // 记录当前api文档是存放路径
        this.opsapiDocpath = opsapiDocpath;
        // 记录当前工程引用的schema文件路径
        this.schemaRootPath = schemaPath;
        this.type = type;
    }
    
    public void setProjectType(String projectType) {
        this.projectType = projectType;
        /*Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("pydev_projecttype");
        for(Element element : elements){
            Attribute attr = element.attribute("type");
            attr.setValue(projectType);
        }*/
        close();
        
    }

     
    public void setProjectMonitorState(String projectMonitorState) {
        
        this.projectMonitorState = projectMonitorState;
       /* Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("pydev_projectmonitor");
        for(Element element : elements){
            Attribute attr = element.attribute("state");
            attr.setValue(projectMonitorState);
        }*/
        close();
    }

    public List<String> getUserSchemaFiles()
    {
        return userSchemaFiles;
    }

    public List<String> getInnerSchemaFiles(){
        
        List<String> list = new ArrayList<String>();
        if (StringUtils.isEmpty(schemaRootPath))
        {
            return list;
        }
        
        File file =new File(schemaRootPath);
        File[] files= file.listFiles();
        for(File f:files){
            String filename = f.getName();
            if(filename.endsWith(".xsd") && (!filename.contains("_") || filename.contains("_action") )){
                list.add(f.getAbsolutePath());
            }
        }
        return list;
    }
  
    public boolean addUserSchemaFiles(String schemaFile)
    {
        return userSchemaFiles.add(schemaFile);
    }
    
    public boolean removeSchemaFiles(String schemaFile)
    {
        return userSchemaFiles.remove(schemaFile);
    }
    
    private String unreplaceVar(String path)
    {
        if(path.isEmpty())
        {
            return path;
        }
        
        File projectPath = new File(path); 
        String pathTmp = projectPath.getAbsolutePath();
        String eclipsepath = PathTools.getEclipsePath();
        String plugPath = PathTools.getPlugOpsViewPath();
        File plugPathFile = new File(plugPath); 
        File eclipsepathFile = new File(eclipsepath); 
        pathTmp = pathTmp.replace("$(OPSDEV_PLUGIN_PATH)", plugPathFile.getAbsolutePath());
        pathTmp = pathTmp.replace("$(ECLIPSE_PATH)", eclipsepathFile.getAbsolutePath());
        return pathTmp;
    }
    
    private String replaceVar(String path)
    {
        File projectPath = new File(path); 
        String pathTmp = projectPath.getAbsolutePath();
        String eclipsepath = PathTools.getEclipsePath();
        String plugPath = PathTools.getPlugOpsViewPath();
        
        File plugPathFile = new File(plugPath); 
        File eclipsepathFile = new File(eclipsepath); 
        
        pathTmp = pathTmp.replace(plugPathFile.getAbsolutePath(), "$(OPSDEV_PLUGIN_PATH)");
        pathTmp = pathTmp.replace(eclipsepathFile.getAbsolutePath(), "$(ECLIPSE_PATH)");
        return pathTmp;
    }
    
    public boolean configOPSPath(IProgressMonitor monitor, IProject iProject){
       
        document = DocumentFactory.getInstance().createDocument();
        Element rootElement = document.addElement("pydev_project");
        
        // 记录当前OPS2.0所在的目录
        Element childElement = rootElement.addElement("pydev_pathproperty");
        childElement.addAttribute("name", "opspath");
        Element threeElement = childElement.addElement("path");
        threeElement.setText(replaceVar(opspath));
        
        //存储创建工程的pluginID
        String pluginID = OpsManagerProjectTool.getProjectCreatePlugId(iProject);
        if (null != pluginID && pluginID.length() > 0) {
            childElement = rootElement.addElement("pydev_pathproperty");
            childElement.addAttribute("name", "plugid");
            threeElement = childElement.addElement("path");
            threeElement.setText(pluginID);
        }
        //存储创建工程的模板路径
        String projectTempletXmlPath = OpsManagerProjectTool.getProjectTempletXmlPath(iProject);
        if (null != projectTempletXmlPath && projectTempletXmlPath.length() > 0) {
            childElement = rootElement.addElement("pydev_pathproperty");
            childElement.addAttribute("name", "projecttemplet");
            threeElement = childElement.addElement("path");
            threeElement.setText(replaceVar(projectTempletXmlPath));
        }
        // 记录当前schema文件路径,根据路径框中的路径指向输入的schema文件
/*        childElement = rootElement.addElement("pydev_pathproperty");
        childElement.addAttribute("name", "schemafiles");
        threeElement = childElement.addElement("path");
        threeElement.setText(this.schemaPath);*/

        // 记录当前api文档是存放路径
        //new opsapipath
        childElement = rootElement.addElement("pydev_pathproperty");
        childElement.addAttribute("name", "opsapipath");
        threeElement = childElement.addElement("path");
        threeElement.setText(replaceVar(this.opsapiDocpath));
        
        //new apifiles
        // 记录当前工程引用的schema文件路径
        childElement = rootElement.addElement("pydev_pathproperty");
        childElement.addAttribute("name", "apifiles");
        threeElement = childElement.addElement("path");
        threeElement.setText(replaceVar(this.schemaRootPath));
        
        // 自定义的schema路径
        childElement = rootElement.addElement("pysdev_schema_path");
        for(String str: userSchemaFiles){
            threeElement = childElement.addElement("path");
            threeElement.setText(str);
        } 
        
        // 记录当前工程是什么类型 
        rootElement.addElement("pydev_projecttype").addAttribute("type", this.projectType);
        // 记录当前工程是什么类型 
        rootElement.addElement("pydev_projectmonitor").addAttribute("state", this.projectMonitorState);
       
        close();
        try {
            if(null != monitor)
            {
                iProject.refreshLocal(IProject.DEPTH_INFINITE, monitor);
            }
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
     
    
    public Boolean isMonitorTrunOn(){
        
        return ("on".equalsIgnoreCase(projectMonitorState)? true: false);
    }
    
    public void parseFile()
    {
        
        String value = "";
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("pydev_pathproperty");
        for(Element element : elements){
            //getAPIFilePath()
            String attribName = element.attributeValue("name");
            if("apifiles".equals(attribName)){
                List<Element> elements3 = element.elements("path");
                if(elements3!=null&&elements3.size()!=0){
                    value= elements3.get(0).getText();
                    this.schemaRootPath = unreplaceVar(value);
                    continue;
                }
            }
     
            // getOpsapiPath()
            if("opsapipath".equals(attribName)){
                List<Element> elements3 = element.elements("path");
                if(elements3!=null&&elements3.size()!=0){
                    value = elements3.get(0).getText();
                    this.opsapiDocpath = unreplaceVar(value);
                    continue;
                }
            }
            
            // getOpsPath()
            if("opspath".equals(element.attributeValue("name"))){
                List<Element> elements2 = element.elements("path");
                if(elements2!=null&&elements2.size()!=0){
                    value = elements2.get(0).getText();
                    this.opspath = unreplaceVar(value);
                    continue;
                }
            }
            
            //getProjectPluginID
            if("plugid".equals(element.attributeValue("name"))){
                List<Element> elements2 = element.elements("path");
                if(elements2!=null&&elements2.size()!=0){
                    this.plugID = elements2.get(0).getText();
                    continue;
                }
            }
            
            //getProjectTempletXML
            if("projecttemplet".equals(element.attributeValue("name"))){
                List<Element> elements2 = element.elements("path");
                if(elements2!=null&&elements2.size()!=0){
                    value = elements2.get(0).getText();
                    this.projectTemplet = unreplaceVar(value); 
                    continue;
                }
            }
        }
        
        // getUserSchemaFiles()
        Element elementSchema = rootElement.element("pydev_schema_path");
        if( null != elementSchema){
            elements = elementSchema.elements("path");
            for(Element element2 : elements){
                String eleString = element2.getText();
                if (StringUtils.isNotEmpty(eleString))
                {
                     userSchemaFiles.add(eleString);
                }
            }
        }
        
         
        // getProjectMonitorState()
        projectMonitorState = "off";
        elements = rootElement.elements("pydev_projectmonitor");
        for(Element element : elements){
            projectMonitorState = element.attributeValue("state");
            break;
        }
        
        // getProjectType()
        elements = rootElement.elements("pydev_projecttype");
        for(Element element : elements){
            projectType = element.attributeValue("type");
            break;
        }
        
        
        
        
    }
    
    public void close()
    {

        FileOutputStream out = null;
        try
        {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            out = new FileOutputStream(xmlPath);
            XMLWriter xmlWriter = new XMLWriter(out, format);
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (IOException e)
        {

        } finally
        {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
