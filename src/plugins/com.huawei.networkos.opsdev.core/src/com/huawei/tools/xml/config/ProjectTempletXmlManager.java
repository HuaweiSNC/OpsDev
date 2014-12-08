package com.huawei.tools.xml.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.LinkedMap;
import org.dom4j.Document;
import org.dom4j.Element;

import com.huawei.network.opsdev.core.templet.TempletVMNode;
import com.huawei.network.opsdev.core.utils.PathTools;

public class ProjectTempletXmlManager {
    private Document document;
    private Element selectedElement;
    private String configXmlPath;
    private String templetPlugId;
    
 
    private List<TempletVMNode> lstVmNodes = new ArrayList<TempletVMNode>();
    private List<TempletVMNode> velocityNodes = new ArrayList<TempletVMNode>();
    
    
    public ProjectTempletXmlManager(final String templetPlugId, String projectType){
    
        this.templetPlugId = templetPlugId;
        this.configXmlPath = PathTools.getTempletFilePath(templetPlugId);
        ConfigXmlManager configXmlManager = new ConfigXmlManager(this.configXmlPath);
        document  = configXmlManager.getDocument();
    }
    
     
    public String getTempletPlugId() {
        return templetPlugId;
    }



    public void setTempletPlugId(String templetPlugId) {
        this.templetPlugId = templetPlugId;
    }



    /****
     * 获取当前文件夹
     * @return
     */
    public Map<String,String> getBuilderFolder(){
        return getAttributes("folder", "value","ispackage");
    }
    
    /****
     * 获取当前ops2.0的资源文件夹
     * @return
     */
    public List<String> getBuilderResource(){
        List<String> list  = new ArrayList<String>();
        for(Entry<String, String> entry :getAttributes("resource", "path","urlplugin").entrySet()){
            String str = PathTools.getPlugPath(entry.getValue())+File.separator+entry.getKey();
            list.add(str);
        }
        return list;
    }
    
    /****
     * 获取当前模板的路径信息
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TempletVMNode> getBuilderVm(){
        
        if(0 == velocityNodes.size())
        {
            // 获取当前工程所有的mv模板
            Element element1 =  getProjectElement();
            List<Element> elements = element1.elements("velocity");
            for(Element element : elements){
                TempletVMNode node = new TempletVMNode();
                node.setVmFileName(element.attributeValue("path"));
                node.setOuturl(element.attributeValue("outurl"));
                node.setVmName(element.attributeValue("name"));
                node.setExampleapi(element.attributeValue("exampleapi"));
                node.setExampleapifile(element.attributeValue("exampleapifile"));
                velocityNodes.add(node);
            }
            
            // 获取选择的例程
            if(selectedElement!=null){
               
                List<Element> elements1 = selectedElement.elements("velocity");
                for(Element element : elements1){
                    TempletVMNode node = new TempletVMNode();
                    node.setVmFileName(element.attributeValue("path"));
                    node.setOuturl(element.attributeValue("outurl"));
                    node.setVmName(element.attributeValue("name"));
                    node.setExampleapi(element.attributeValue("exampleapi"));
                    node.setExampleapifile(element.attributeValue("exampleapifile"));
                    velocityNodes.add(node);
                }
            }
        }
        
        return velocityNodes; 
    }
    
    public String getOpsPointVMFileByName(String name)
    {
        TempletVMNode node = getOpsPointByName(name);
        if(null != node){
            
            
            return (PathTools.getPlugPath(templetPlugId)+File.separator + node.getVmFileName());
        }
        return null;
    }
    
    
    
    public String getOpsOutputByName(String name)
    {
        TempletVMNode node = getOpsPointByName(name);
        if(null != node){
            return  node.getOuturl();
        }
        return null;
    }
    
    public TempletVMNode getOpsPointByName(String name)
    {
        getOpsPointBuilderVm();
        for(TempletVMNode node : lstVmNodes){
            if(name.equalsIgnoreCase(node.getVmName()))
            {
                return node;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public List<TempletVMNode> getOpsPointBuilderVm()
    {
        if(0 == lstVmNodes.size())
        {
            // 获取当前工程所有的mv模板
            Element element1 =  getProjectElement();
            List<Element> elements = element1.elements("opspoint");
            for(Element element : elements){
                TempletVMNode node = new TempletVMNode();
                node.setVmFileName(element.attributeValue("path"));
                node.setOuturl(element.attributeValue("outurl"));
                node.setVmName(element.attributeValue("name"));
                node.setExampleapi(element.attributeValue("exampleapi"));
                node.setExampleapifile(element.attributeValue("exampleapifile"));
                lstVmNodes.add(node);
            }
        }
        
        return lstVmNodes; 
    }
    
    /****
     * 获取当前需要拷贝的文件路径
     * @return
     */
    public Map<String,String> getBuliderFile(){
        return getFileAttributes("file", "name", "path","outurl");
    }
    
    @SuppressWarnings("unchecked")
    public Map<String,String> getTempletList(){
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("templet");
        Map<String,String> map = new HashMap<String, String>();
        for(Element element : elements){
            map.put(element.attributeValue("name"), element.elementText("description"));

        }
        return map;
    }
    
    /***
     * 根据当前工程类型完成所选择的工程模板
     * @return 返回可选的工程模板类
     */
    @SuppressWarnings("unchecked")
    public Element getProjectElement(){
        
        Element rootElement = document.getRootElement();
        String projectElementName = rootElement.attributeValue("default");
        List<Element> elements = rootElement.elements("projecttemplet");
        for(Element element : elements){
            if(projectElementName.equals(element.attributeValue("name"))){
                return element;
            }
        }
        return null;
    }
    
    public void setSelectedElement(String selectedName){
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("templet");

        for(Element element : elements){
            if(selectedName.equals(element.attributeValue("name"))){
                selectedElement  = element;
                return;
            }
        }
        
    }
   
    public Map<String,String> getAttributes(String elementName,String keyAttrName,String valueAttrName){
        
        Map<String,String> map = new LinkedMap();
        Element element1 =  getProjectElement();
        
        // 获取选择的例程
        if(selectedElement!=null){
            List<Element> elements1 = selectedElement.elements(elementName);
            for(Element element : elements1){
                map.put(element.attributeValue(keyAttrName),element.attributeValue(valueAttrName));
            }
        }
        
        // 获取当前工程所有的mv模板
        List<Element> elements = element1.elements(elementName);
        for(Element element : elements){
            map.put(element.attributeValue(keyAttrName),element.attributeValue(valueAttrName));
        }
        
        return map;
    }
    
  public Map<String,String> getFileAttributes(String elementName,String name, String keyAttrName,String valueAttrName){
        
        Map<String,String> map = new LinkedMap();
        Element element1 =  getProjectElement();
        
        // 获取选择的例程
        if(selectedElement!=null){
            List<Element> elements1 = selectedElement.elements(elementName);
            for(Element element : elements1){
                
                map.put(element.attributeValue(keyAttrName),element.attributeValue(valueAttrName) + File.separator + element.attributeValue(name));
                
            }
        }
        
        // 获取当前工程所有的mv模板
        List<Element> elements = element1.elements(elementName);
        for(Element element : elements){
            
            
            map.put(element.attributeValue(keyAttrName),element.attributeValue(valueAttrName) + File.separator + element.attributeValue(name));
            
        }
        
        return map;
    }
}

