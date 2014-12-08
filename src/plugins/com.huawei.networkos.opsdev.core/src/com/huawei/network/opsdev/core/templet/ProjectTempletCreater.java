package com.huawei.network.opsdev.core.templet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.huawei.network.opsdev.core.schema.RestApiManager;
import com.huawei.network.opsdev.core.utils.PathTools;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;


/**
 * 根据模板和我相应的信息生成对应的模板文件
 * @author qWX182698
 *
 */
public class ProjectTempletCreater {

    private Map<String, Object> otherAttribute = null;
    private Map<String, Object> someAttribute = new LinkedHashMap<String, Object>();

    private JSONObject obj;
    //private Properties properties = new Properties();
    private RestApiManager restApiManager = new RestApiManager();
    private static Map<String, VelocityEngine> mapVelocity = new LinkedHashMap<String, VelocityEngine>();
    
    String temlateFile;
    String json;
    String saveFilePath;
    
    //创建工程时选择的工程类型（python、java）构造器实例初始化
    String projectType ;
    String plugid;
    
    //文件名后缀
    String suffix;

    public void addAttribute(String name, Object value)
    {
        someAttribute.put(name, value);
    }
    
    public synchronized static VelocityEngine getVelocity(String path)
    {
        
        if (mapVelocity.containsKey(path))
        {
            return mapVelocity.get(path);
        }
        
        Properties properties = new Properties();
        properties.put(VelocityEngine.FILE_RESOURCE_LOADER_PATH, path);
        VelocityEngine velocityTmp = new VelocityEngine();
        velocityTmp.init(properties);
        mapVelocity.put(path, velocityTmp);
        
        return velocityTmp;
    }

    /**
     * 
     * @param 模板文件
     * @param json语句  生成文件需要的json语句
     * @param 保存模板文件的路径
     */
    public ProjectTempletCreater(String temlateFile, String json, String saveFilePath, String plugId ) {
        this.temlateFile = temlateFile;
        this.json = json;
        this.saveFilePath = saveFilePath;
        this.plugid = plugId;
        if (json != null) {

            obj = JSONArray.fromObject(json).getJSONObject(0);
        }
    }

    public ProjectTempletCreater(String temlateFile, String json, String saveFilePath, String projectType, String plugId) {
        this.temlateFile = temlateFile;
        this.json = json;
        this.saveFilePath = saveFilePath;
        this.projectType = projectType;
        this.plugid = plugId;
        
        if("java".equals(projectType)){
            suffix =".java";
        }
        else if("python".equals(projectType)){
            suffix =".py";
        }
        if (json != null) {

            obj = JSONArray.fromObject(json).getJSONObject(0);
        }
    }

    public ProjectTempletCreater(String plugid) {
        this.plugid = plugid;
    }

    public StringWriter startPreview(TempletAutoPythonNode templetAutoPythonNode, String temlateFile) {
        
        VelocityEngine velocity = getVelocity(PathTools.getPlugOpsTempletPath(plugid));
         
        VelocityContext context = new VelocityContext();
        context.put("attributesAndKeys", templetAutoPythonNode.getAttributesAndKeys());
        context.put("attributesAndKeysExampleMap", templetAutoPythonNode.attributesAndKeysExampleMap);
        context.put("handleName", templetAutoPythonNode.getHandleName());
        context.put("deviceConfigName", templetAutoPythonNode.getDeviceConfigName());
        Template template = null;
        //String templateName = new File(temlateFile).getName();
        template = velocity.getTemplate(PathTools.getRelativeFileTempletPath(plugid, temlateFile));
        StringWriter out = new StringWriter();
        template.merge(context, out);
        out.flush();
        return out;
    }

    public StringWriter startAntoInfo(TempletAutoPythonNode templetAutoPythonNode, String temlateFile) {
        
       
        this.temlateFile = temlateFile;
        
        VelocityEngine velocity = getVelocity(PathTools.getPlugOpsTempletPath(plugid));
        
        VelocityContext context = new VelocityContext();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        context.put("currentTime", sdf.format(date));
        context.put("handleName", templetAutoPythonNode.getHandleName());
        context.put("attributes", templetAutoPythonNode.getAttributes());
        context.put("deviceConfigName", templetAutoPythonNode.getDeviceConfigName());
        context.put("attributesAndKeys", templetAutoPythonNode.getAttributesAndKeys());
        context.put("keys", templetAutoPythonNode.getKeys());
        context.put("fullUrl", templetAutoPythonNode.getFullUrl());
        context.put("pythonMethod", templetAutoPythonNode.getPythonMethod());

        Template template = null;
        //String templateName = new File(temlateFile).getName();
        template = velocity.getTemplate(PathTools.getRelativeFileTempletPath(plugid, temlateFile));
        StringWriter out = new StringWriter();
        template.merge(context, out);
        out.flush();
        return out;
    }

    /**
     * 开始生成模板文件到指定的位置
     */
    public void start() {
       
        VelocityEngine velocity = getVelocity(PathTools.getPlugOpsTempletPath(plugid));
        
        VelocityContext context = new VelocityContext();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");

        context.put("currentTime", sdf.format(date));
        if (obj != null) {
            Map<String, String> attributes = getAttributeNames();
            context.put("itemList", attributes);
            context.put("listLen", attributes.size());
            context.put("className", getClassName());
            context.put("selFolder", getSelFolder());
            context.put("url", getUrl());
            context.put("exapelMap", getExampleAttributes());
           
            //context.put("jsonindexurl", getJsonIndexUrl());

            context.put("chosefilename", saveFilePath);
            context.put("filename", getSchameFileName());
            context.put("methodname", getMethodName());
            context.put("fullurl", getFullUrl());
            context.put("keymethod", getMethodKeyName());
           // context.put("keynamewithoutvalue", getKeyNamesWhithOutValue());
            context.put("restApiManager", restApiManager);
        }
        if (otherAttribute != null) {
            context.put("otherAttribute", otherAttribute);
        }
        if (someAttribute.size() > 0)
        {
            String str = null;
            Object value = null;
            Iterator<Entry<String, Object>> iterator = someAttribute.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                value = entry.getValue();
                str = entry.getKey();
                context.put(str, value);
            }
        }
        
        Template template = null;
        template = velocity.getTemplate(PathTools.getRelativeFileTempletPath(plugid, temlateFile));
      
        BufferedWriter out = null;
        if (json != null) {
            try {

                if (saveFilePath != null && saveFilePath != ""
                            && saveFilePath.substring(saveFilePath.length() - 1).equals(File.separator)) {
                        out = new BufferedWriter(new PrintWriter(new File(saveFilePath + getClassName() + suffix)));
                    } else if (saveFilePath == null | saveFilePath == "") {
                        out = new BufferedWriter(new PrintWriter(new File(getClassName() + suffix)));
                    } else {
                        out = new BufferedWriter(new PrintWriter(new File(saveFilePath + File.separator
                                + getClassName()
                                + suffix)));
                    }
                
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            if (template != null) {
                template.merge(context, out);
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                out = new BufferedWriter(new PrintWriter(new File(saveFilePath)));

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            if (template != null) {
                template.merge(context, out);
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getSelFolder() {
      return OpsManagerProjectTool.getPackageName();
    }

    /**
     * 开始生成模板文件并返回一个模板文件的数据流
     * @param isGetStream
     * @return
     */
    public StringWriter start(boolean isGetStream) {
         
        VelocityEngine velocity = getVelocity(PathTools.getPlugOpsTempletPath(plugid));
         
        VelocityContext context = new VelocityContext();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        context.put("currentTime", sdf.format(date));
        if (obj != null) {
            Map<String, String> attributes = getAttributeNames();
            context.put("itemList", attributes);
            context.put("keysmap", getKeyNames());
            context.put("listLen", attributes.size());
            context.put("className", getClassName());
            context.put("url", getUrl());
            context.put("exapelMap", getExampleAttributes());
            //context.put("jsonindexurl", getJsonIndexUrl());
            context.put("filename", getSchameFileName());
            context.put("Chosefilename", temlateFile);
            context.put("methodname", getMethodName());
            context.put("fullurl", getFullUrl());
            context.put("keymethod", getMethodKeyName());
            context.put("selFolder", getSelFolder());
           // context.put("keynamewithoutvalue", getKeyNamesWhithOutValue());
            context.put("restApiManager", restApiManager);
        }
        if (otherAttribute != null) {
            context.put("otherAttribute", otherAttribute);
        }
        Template template = null;
        template = velocity.getTemplate(PathTools.getRelativeFileTempletPath(plugid, temlateFile));
        StringWriter out = new StringWriter();
        template.merge(context, out);
        out.flush();
        return out;
    }

 

    /**
     * 获取schmafile的文件名
     * @return
     */
    public String getSchameFileName() {
        String url = getUrl();
        String[] arrayList = url.split("/");
        
        String str = arrayList[0] ;
          
        return str + ".xsd";
    }

    /**
     * 在生成模板的过程中压入其他的属性
     * @param otherAttribute
     */
    public void setOtherAttribute(Map<String, Object> otherAttribute) {
        this.otherAttribute = otherAttribute;
    }

    /**
     * 获取示例属性的键值对
     * @return
     */
    public Map<String, String> getExampleAttributes() {
        Map<String, String> exampleMap = new HashMap<String, String>();
        Map<String, String> itemList = getAttributeNames();

        Iterator<Entry<String, String>> iterator = itemList.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            String value = entry.getValue();
            String str = value.substring(value.indexOf("example") + "example".length() + 3);
            str = str.substring(0, str.indexOf('\''));
            exampleMap.put(entry.getKey(), str);
        }
        return exampleMap;
    }

    public Map<String, String> getKeyExampleAttributes() {
        Map<String, String> exampleMap = new HashMap<String, String>();
        Map<String, String> itemList = getKeyNames();

        Iterator<Entry<String, String>> iterator = itemList.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            String value = entry.getValue();
            String str = value.substring(value.indexOf("example") + "example".length() + 3);
            str = str.substring(0, str.indexOf('\''));
            exampleMap.put(entry.getKey(), str);
        }

        return exampleMap;
    }

    /**
     * 获取对应的属性
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getAttributeNames() {
        Map<String, String> itemList = new HashMap<String, String>();
        Iterator<Entry<String, Object>> iterator = ((JSONObject) obj.get("attributes"))
                .entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            itemList.put(entry.getKey(), entry.getValue().toString().replace("\"", "'"));
        }
        return itemList;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getKeyNames() {
        Map<String, String> itemList = new HashMap<String, String>();
        if (obj.containsKey("keys")) {
            JSONArray jarray = (JSONArray) obj.get("keys");
            for (int i = 0; i < jarray.size(); i++) {
                JSONObject jobj = jarray.getJSONObject(i);
                Iterator<Entry<String, Object>> iterator = jobj.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                  
                        itemList.put(entry.getKey(), entry.getValue().toString().replace("\"", "'"));
                }
            }
        }
        return itemList;
    }

    /**
     * 获取类q
     * @return
     */
    public String getClassName() {
        
        String url = getUrl();
        String name = "Undefine";
        if (url != null && url.length() > 0)
        {
            if (url.charAt(0) == '/'){
                url = url.substring(1);
            }
            String[] arrayList = url.split("/");
            if (arrayList.length == 1) {
                name = arrayList[0];
                name = getUpperString(name);
    
            } else {
                String firstName = arrayList[0];
                String lastName = arrayList[arrayList.length -1];
                firstName = getUpperString(firstName);
                lastName = getUpperString(lastName);
                name = firstName + lastName;
            }        
        }
        return name;
    }


    private String getUpperString(String name)
    {
        if (name == null || name.length() == 0 )
        {
            return name;
        }
        
        StringBuffer sb = new StringBuffer(name);
        sb.setCharAt(0,  Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
    
    

    /**
     * 获取生成restfulApi文件的url不包含key
     * @return
     */
    public String getUrl() {
        return (String) obj.get("url");
    }

    public String getFullUrl() {
        return (String) obj.get("curl");
    }

    public Map<String, String> getMethodName() {
        Map<String, String> map = new HashMap<String, String>();
        Set<String> set = getAttributeNames().keySet();
        for (String str : set) {
            map.put(str, str.substring(0, 1).toUpperCase() + str.substring(1));
        }
        return map;
    }

    public Map<String, String> getMethodKeyName() {
        Map<String, String> map = new HashMap<String, String>();
        Set<String> set = getKeyNames().keySet();
        for (String str : set) {
            map.put(str, str.substring(0, 1).toUpperCase() + str.substring(1));
        }
        return map;
    }

     
}
