package com.huawei.network.opsdev.core.templet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.huawei.network.opsdev.core.schema.RestApiManager;
import com.huawei.network.opsdev.core.utils.ConsoleFactory;

/**
 *  根据url 生成对应的 json 格式 或者或者是xml格式的预览
 * @author qWX182698
 *
 */
public class ApiJsonXmlPreviewManager {
    public String showXml(String url, ProjectTempletManager manager, boolean isInner) {
        RestApiManager apiManager = null;
        if (isInner) {
            apiManager = manager.getApiManager();
        } else {
            apiManager = manager.getUserApiManager();
        }
        ProjectTempletCreater creater = new ProjectTempletCreater(null, apiManager.getRestApiJsonByUrl(url), null, manager.getPlugId());
        Map<String, String> map = creater.getExampleAttributes();
        Map<String, String> keyMap = creater.getKeyExampleAttributes();
        String[] urlSplit = url.split("/");
        String[] curlSplit = creater.getFullUrl().split("/");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < urlSplit.length; i++) {
            if (!urlSplit[i].equals("")) {
                buffer.append("<" + urlSplit[i] + ">" + "\n");
                for (int j = 0; j < i; j++) {
                    buffer.append("    ");
                }
                String s = curlSplit[i];
                if (s.contains("?")) {
                    s = s.substring(s.indexOf("?") + 1);
                    String[] strs = s.split("&");
                    for (int j = 0; j < strs.length; j++) {

                        String keyName = strs[j].substring(0, strs[j].indexOf("="));
                        buffer.append("<" + keyName + ">" + keyMap.get(keyName) + "</" + keyName + ">\n");

                        if (j < strs.length - 1) {
                            for (int x = 0; x <= i; x++) {
                                buffer.append("    ");
                            }
                        } else {
                            for (int x = 0; x <= i - 1; x++) {
                                buffer.append("    ");
                            }
                        }
                    }
                }
            }
        }
        for (Entry<String, String> entry : map.entrySet()) {
            buffer.append("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">\n");
            for (int i = 1; i < urlSplit.length; i++) {
                buffer.append("    ");
            }
        }
        buffer.delete(buffer.length() - 4, buffer.length());
        for (int i = urlSplit.length - 1; i >= 0; i--) {
            if (!urlSplit[i].equals("")) {
                buffer.append("</" + urlSplit[i] + ">" + "\n");
                for (int j = 2; j <= i; j++) {
                    buffer.append("    ");
                }
            }
        }

        return buffer.toString();
    }

    //string格式展示
    public static List<String> showString(List<UrlPropertiesBean> currentChosedBeands, String str) {

        List<String> list = new ArrayList<String>();
        list.add("private" + " " + "String" + " " + str + " = \"{");
        for (UrlPropertiesBean propertiesBean : currentChosedBeands) {

            list.add("\\" + "\"" + propertiesBean.getName() + "\\" + "\":" + "\\" + "\"" + propertiesBean.getValue() + "\\" + "\",");

        }
        String str1 = list.get(list.size() - 1);
        list.remove(str1);
        str1 = str1.substring(0, str1.length() - 1);
        list.add(str1);
        list.add("}\";");
        return list;

    }

    public List<String> showSetMethon(String url, ProjectTempletManager manager, List<String> checkedItem,
            boolean isInner) {
        RestApiManager apiManager = null;
        if (isInner) {
            apiManager = manager.getApiManager();
        } else {
            apiManager = manager.getUserApiManager();
        }
        ProjectTempletCreater creater = new ProjectTempletCreater(null, apiManager.getRestApiJsonByUrl(url), null, manager.getPlugId());
        Map<String, String> map = creater.getExampleAttributes();
        List<String> list = new ArrayList<String>();
        for (Entry<String, String> entry : map.entrySet()) {
            if (checkedItem.contains(entry.getKey())) {
                list.add("restfulapi.set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1)
                        + "(\"" + entry.getValue() + "\")");
            }
        }

        return list;
    }

    public static List<String> showSetMethon(List<UrlPropertiesBean> currentChosedBeands, String str) {
        List<String> list = new ArrayList<String>();

        for (UrlPropertiesBean propertiesBean : currentChosedBeands) {

            list.add(".set" + propertiesBean.getName().substring(0, 1).toUpperCase()
                    + propertiesBean.getName().substring(1) + "(\"" + propertiesBean.getValue() + "\")");

        }

        return list;
    }
    
    //java
    public static List<String> showMethon(List<UrlPropertiesBean> currentChosedBeands, String str,String className,int beansNum,boolean flag,UrlPropertiesBean pBeans) {
        List<String> list = new ArrayList<String>();
        if(beansNum > 1 && flag){
            list.add(str + ".set" + pBeans.getName().substring(0, 1).toUpperCase()
                    + pBeans.getName().substring(1) + "(\"" + pBeans.getValue() + "\");");
        }
        list.add(className + ".OneBody body = " + str + ".new OneBody();");

        for (UrlPropertiesBean propertiesBean : currentChosedBeands) {

            list.add("body.set" + propertiesBean.getName().substring(0, 1).toUpperCase()
                    + propertiesBean.getName().substring(1) + "(\"" + propertiesBean.getValue() + "\");");

        }

        return list;
    }
    
    //java
    public static List<String> showJson(List<UrlPropertiesBean> currentChosedBeands, String str) {

        List<String> list = new ArrayList<String>();
        list.add("String " + str + " = \"[\" + ");
        for (UrlPropertiesBean propertiesBean : currentChosedBeands) {

            list.add("\"{\'" + propertiesBean.getName() + "\':\'" + propertiesBean.getValue() + "\'},\"+");

        }
        String str1 = list.get(list.size() - 1);
        list.remove(str1);
        str1 = str1.substring(0, str1.length() - 3);
        str1 += "\"+";
        list.add(str1);
        list.add("\"]\";");
        return list;

    }
    

    public static List<String> showJsonWithOutUrl(List<UrlPropertiesBean> currentChosedBeands, String str) {

        List<String> list = new ArrayList<String>();
        list.add(str + " = {");
        for (UrlPropertiesBean propertiesBean : currentChosedBeands) {

            list.add("\"" + propertiesBean.getName() + "\":\"" + propertiesBean.getValue() + "\",");

        }
        String str1 = list.get(list.size() - 1);
        list.remove(str1);
        str1 = str1.substring(0, str1.length() - 1);
        list.add(str1);
        list.add("}");
        return list;

    }

    public static List<String> showJsonForUrl(List<UrlPropertiesBean> currentChosedBeands, String str) {

        List<String> list = new ArrayList<String>();
        list.add(str + " = [");
        for (UrlPropertiesBean propertiesBean : currentChosedBeands) {

            list.add("{\"" + propertiesBean.getName() + "\":\"" + propertiesBean.getValue() + "\"},");

        }
        String str1 = list.get(list.size() - 1);
        list.remove(str1);
        str1 = str1.substring(0, str1.length() - 1);
        list.add(str1);
        list.add("]");
        return list;

    }

    public List<String> showJsonWithOutUrl(String url, ProjectTempletManager manager, List<String> checkedItem,
            boolean isInner) {

        RestApiManager apiManager = null;
        if (isInner) {
            apiManager = manager.getApiManager();
        } else {
            apiManager = manager.getUserApiManager();
        }
        ProjectTempletCreater creater = new ProjectTempletCreater(null, apiManager.getRestApiJsonByUrl(url), null, manager.getPlugId());
        Map<String, String> map = creater.getExampleAttributes();
        List<String> list = new ArrayList<String>();
        list.add("exampleBody = {");
        for (Entry<String, String> entry : map.entrySet()) {
            if (checkedItem.contains(entry.getKey())) {
                list.add("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",");
            }
        }
        String str = list.get(list.size() - 1);
        list.remove(str);
        str = str.substring(0, str.length() - 1);
        list.add(str);
        list.add("}");
        return list;
    }

    public String showJson(String url, ProjectTempletManager manager, boolean isInner) {

        RestApiManager apiManager = null;
        if (isInner) {
            apiManager = manager.getApiManager();
        } else {
            apiManager = manager.getUserApiManager();
        }
        ProjectTempletCreater creater = new ProjectTempletCreater(null, apiManager.getRestApiJsonByUrl(url), null, manager.getPlugId());
        Map<String, String> map = creater.getExampleAttributes();
        Map<String, String> keyMap = creater.getKeyExampleAttributes();
        String[] urlSplit = url.split("/");
        System.out.println(creater.getUrl());
        ConsoleFactory.printToConsole(creater.getUrl(),true);
        System.out.println(creater.getFullUrl());
        ConsoleFactory.printToConsole(creater.getFullUrl(),true);
        String[] curlSplit = creater.getFullUrl().split("/");
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < urlSplit.length; i++) {
            if (!urlSplit[i].equals("")) {
                buffer.append("{" + urlSplit[i] + ":[" + "\n");
                for (int j = 0; j < i; j++) {
                    buffer.append("    ");
                }
                String s = curlSplit[i];
                if (s.contains("?")) {
                    s = s.substring(s.indexOf("?") + 1);
                    String[] strs = s.split("&");
                    for (int j = 0; j < strs.length; j++) {
                        String keyName = strs[j].substring(0, strs[j].indexOf("="));
                        buffer.append("{\"" + keyName + "\":\"" + keyMap.get(keyName) + "\"},\n");
                        for (int x = 0; x < i; x++) {
                            buffer.append("    ");
                        }
                    }
                }
            }
        }
        int count = 0;
        for (Entry<String, String> entry : map.entrySet()) {
            if (count != map.size() - 1) {
                buffer.append("{\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"},\n");
                for (int i = 1; i < urlSplit.length; i++) {
                    buffer.append("    ");
                }
            } else {
                buffer.append("{\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"}\n");
                for (int i = 1; i < urlSplit.length - 1; i++) {
                    buffer.append("    ");
                }
            }
            count++;
        }
        for (int i = urlSplit.length - 1; i >= 0; i--) {
            if (!urlSplit[i].equals("")) {
                buffer.append("]}\n");
                for (int j = 2; j < i; j++) {
                    buffer.append("    ");
                }
            }
        }

        return buffer.toString();
    }

}
