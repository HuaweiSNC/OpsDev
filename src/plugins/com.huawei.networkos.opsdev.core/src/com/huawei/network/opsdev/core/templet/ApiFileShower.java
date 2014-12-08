package com.huawei.network.opsdev.core.templet;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.huawei.network.opsdev.core.schema.OpsSchemaApiItem;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;


public class ApiFileShower {
    /**
     * 获取schema资源文件夹的路径 获取所有的schema文件的路径
     * @param schemaFilePath schema资源文件夹的路径 
     * @return 返回所有的schema文件路径的集合
     */
    public List<OpsSchemaApiItem> getApiFileListFromPath(String schemaFilePath){
        List<OpsSchemaApiItem> list = new ArrayList<OpsSchemaApiItem>();

        File schemaFile = new File(schemaFilePath);
        File[] schemaFiles = schemaFile.listFiles(new FilenameFilter() {
            public boolean accept(File file, String fileName) {
                if(fileName.contains("_") && !fileName.contains("_action")){
                    return false;
                }else{
                    return true;
                }
            }
        });
        
        OpsSchemaApiItem schemaItem = null;
        for(File file: schemaFiles){
            schemaItem = new OpsSchemaApiItem();
            schemaItem.setSchemaFile(file.getPath());
            list.add(schemaItem);
        }
        return list;
    }
    
    /**
     * 根据文件名获取与之对应的api urls
     * @param schemaFilePath shcema文件的路劲
     * @return schema文件对应的urls
     */
    public static List<String>  getFileListFromFilePath(String schemaFilePath,IProject iProject){
        
        List<String> list = new ArrayList<String>();
        File file =new File(schemaFilePath);
        File[] files= file.listFiles();
        
        for(File f:files){
            String filename = f.getName();
            if((filename.endsWith(".xsd") && !filename.contains("_"))
                    || filename.contains("_action.xsd")){
                list.add(f.getAbsolutePath());
            }
        }
        ProjectConfigXmlManager configXmlManager = OpsManagerProjectTool.getConfigManager(iProject);
        List<String> fileList = configXmlManager.getUserSchemaFiles();
        list.addAll(fileList);
        
        return list;
    }
    
}
