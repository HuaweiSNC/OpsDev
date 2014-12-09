package com.huawei.network.opsdev.core.templet;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.network.opsdev.core.schema.RestApiManager;
import com.huawei.network.opsdev.core.schema.RestfulApiSchemaManager;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.tools.xml.config.ProjectTempletXmlManager;
 /**
  * 对生成的模板的管理 
  * 生成各种我们需要的模板
  * @author qWX182698
  *
  */
public class ProjectTempletManager {
    
    
    private List<String> schemaFiles = new ArrayList<String>();
    private RestfulApiSchemaManager restfulApiManager = new RestfulApiSchemaManager();
    private RestfulApiSchemaManager userRestfulApiManager = new RestfulApiSchemaManager();
    private String plugId = PathTools.PATH_OPS_CORE_PLUGIN_ID;
    
    public void clearMain()
    {
        schemaFiles.clear();
        restfulApiManager.getRestApiManager().clear();
    }
    
    
    // @TODO templetManager 没启用，作为后缀名判断用
    private ProjectTempletXmlManager templetXmlManager = null;
     
    public ProjectTempletXmlManager getTempletXmlManager() {
        return templetXmlManager;
    }

    public void setTempletXmlManager(ProjectTempletXmlManager templetXmlManager) {
        this.templetXmlManager = templetXmlManager;
    }
 
    public ProjectTempletManager(String plugId)
    {
        this.plugId = plugId;
    }
     
    public String getPlugId() {
        return plugId;
    }

    public void setPlugId(String plugId) {
        this.plugId = plugId;
    }

    /**
     * 取得所有的schema文件
     * @return
     */

    public List<String> getSchemaFile() {
        return schemaFiles;
    }
    
 
    public void setSchemaFile(List<String> schemaFiles) {
        this.schemaFiles = schemaFiles;
    }
    
    public RestApiManager getApiManager() {
 
        return restfulApiManager.getRestApiManager();
    }
    
    public RestApiManager getUserApiManager() {
       
        return userRestfulApiManager.getRestApiManager();
    }
    
    public Map<String,List<String>> getUrlsMap(){
        return restfulApiManager.getRestApiManager().getMapFileNameUrl();
    }

    /**
     * 增加需要解析的schema 
     * @param schemaFile 解析新增加的schema
     */
    public void addFile(List<String> schemaFile) {
        for(String file: schemaFile){
            if(!schemaFiles.contains(file) && restfulApiManager.getRestApiManager().isContainsUrl(new File(file).getName())){
                schemaFiles.add(file);
                restfulApiManager.processXsd(file, true);
            }
        }
    }
    
    public void addUserFile(List<String> schemaFile) {
        for(String file: schemaFile){
            if(!schemaFiles.contains(file) && restfulApiManager.getRestApiManager().isContainsUrl(new File(file).getName())){
                schemaFiles.add(file);
                
            }
            userRestfulApiManager.processXsd(file, true);
        }
    }
    
    /**
     * 增加需要解析的schema 
     * @param schemaFile huawei的schema全路径
     */
    public void addFile(String schemaFile) {
        if(!schemaFiles.contains(schemaFile)){
            schemaFiles.add(schemaFile);
            
            restfulApiManager.processXsd(schemaFile, true);
        }
         
    }
    
    /**
     * 添加用户自己的schema文件
     * @param schemaFile
     */
    public void addUserFile(String schemaFile) {
        if(!schemaFiles.contains(schemaFile)){
            schemaFiles.add(schemaFile);
            userRestfulApiManager.processXsd(schemaFile, true);
        }
    }
    
    /**
     * 查询是否包含当前的key
     * @return
     */
    public boolean isContainsUrl(String key) {
        
        return restfulApiManager.getRestApiManager().isContainsUrl(key);
    }
    
    /**
     * 获取所有的schema路径
     * @return 获取MAP<文件名, 此文件的url路径>
     */
    public Map<String, List<String>> getApiUrls() {
     
        Map<String, List<String>> map  =  restfulApiManager.getRestApiManager().getMapFileNameUrl();
        map.putAll(userRestfulApiManager.getRestApiManager().getMapFileNameUrl());
        return map;
    }
    
    
    /**
     * 根据文件名，获取需要的schema的url
     * @return url路径列表
     */
    public List<String> getApiUrlsByName(String key) {
        
        return restfulApiManager.getRestApiManager().getUrlsbyFileName(key);
    }
    
    public List<String> getUserApiUrlsByName(String key) {
        
        return userRestfulApiManager.getRestApiManager().getUrlsbyFileName(key);
    }
    
    /**
     * 
     * 解析vm文件，生成需要的restfulapi
     * @param url  当前schema中的路径
     * @param filePath 指定schema文件路径
     * @param vmFilePath 指定当前需要处理的VM模板
     * @param projectType 工程类型 shj
     */
    public void createTempletByUrl(String url , String filePath ,String vmFilePath,String projectType, String pluginID) {
        String json = restfulApiManager.getRestApiManager().getRestApiJsonByUrl(url);
        ProjectTempletCreater creater = new ProjectTempletCreater(vmFilePath, json ,filePath,projectType, plugId);
        creater.start();
    }
    
    /**
     * 
     * @param url 生成restfulApi操作示例文件需要的url
     * @param vmFilePath 模板文件的存放路径
     * @param isGetOut 
     * @return 生成的restfukApi操作示例文件的数据流
     * @param projectType 工程类型 shj
     */
    public StringWriter createTempletByUrl(String url ,String vmFilePath,boolean isGetOut,String projectType) {
        String json = restfulApiManager.getRestApiManager().getRestApiJsonByUrl(url);
        ProjectTempletCreater creater = new ProjectTempletCreater(vmFilePath, json ,null,projectType ,plugId);
        return creater.start(isGetOut);
    }
    
    /**
     * 
     * @param url 生成restfulApi操作示例文件需要的url
     * @param vmFilePath 模板文件的存放路径
     * @param isGetOut 
     * @return 生成的restfukApi操作示例文件的数据流
     */
    public StringWriter createTempletByUserUrl(String url ,String vmFilePath,boolean isGetOut,String projectType) {
        String json = userRestfulApiManager.getRestApiManager().getRestApiJsonByUrl(url);
        ProjectTempletCreater creater = new ProjectTempletCreater(vmFilePath, json ,null,projectType, plugId);
        return creater.start(isGetOut);
    }
    
    /**
     * 生成其他的用户自定义的vm的路径
     * @param filePath 生成的文件存放的目录
     * @param vmFilePath 模板文件的路径
     * @param otherObj 压入到模板文件中的数据
     */
    public void createOtherVm(String filePath ,String vmFilePath ,Map<String, Object> otherObj, String pluginID) {
        ProjectTempletCreater creater  = new ProjectTempletCreater(vmFilePath, null, filePath, pluginID);
        creater.setOtherAttribute(otherObj);
        creater.start();
    }
    
    /**
     * 创建main函数的模板
     * @param mainFunction 主函数的名称
     * @param filePath 生成的主函数文件存放的目录
     * @param vmFilePath 生成的主函数文件所需要的模板文件的路径
     */
    public void createMainFunVm(String mainFunction, String filePath ,String vmFilePath , String pluginID) {
        ProjectTempletCreater creater  = new ProjectTempletCreater(vmFilePath, null, filePath, pluginID );
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("mainfunction", mainFunction);
        map.put("sampletemplatename", mainFunction);
        creater.setOtherAttribute(map);
        creater.start();
    }
    /**
     * 批量生成指定schema文件指定的url列表中的vm模板。
     * @param urls 用户指定的url列表
     * @param filePath 当前处理的vm模板
     */
    public void createTempletByUrl(List<String> urls , String filePath ,String vmFilePath, String pluginID){
        for(String url : urls){
            createTempletByUrl(url,filePath ,vmFilePath,null, pluginID);
        }
    }
    
    public void clearUserManager(){
       // userRestfulApiManager = new RestfulApiSchemaManager();
    }
        
}

