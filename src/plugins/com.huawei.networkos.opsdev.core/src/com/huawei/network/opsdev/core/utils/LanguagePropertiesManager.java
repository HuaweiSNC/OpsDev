package com.huawei.network.opsdev.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class LanguagePropertiesManager {
    private static Properties properties = new Properties();

    static {

        try {
            properties.load(new FileInputStream(PathTools.getPlugPath("com.huawei.network.opsdev.core")
                    + File.separator
                    + getAttributeValuesFromPropertiesFiles(PathTools.getPlugPath("com.huawei.network.opsdev.core")
                            + File.separator + "pluginlanguage.properties", "language")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(properties.get("Operation"));
        ConsoleFactory.printToConsole(properties.get("Operation").toString(),true);
    }

    public static void setLanguagePath(String configPath) {
        Properties configProperties = new Properties();
        try {
            configProperties.load(new FileInputStream(configPath));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            properties.load(new FileInputStream(configProperties.getProperty("languagepath")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getAttributeValuesFromPropertiesFiles(String propertiesFilePathname, String attributeName) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFilePathname));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return properties.getProperty(attributeName);
    }

    public static String getPropertiesValue(String key) {
        String str = properties.getProperty(key);
        System.out.println(str+"xxxx");
        ConsoleFactory.printToConsole(str+"xxxx",true);
        if(str!=null){
            try {
                
                str = new String(str.getBytes("ISO-8859-1"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return str;
    }
}
