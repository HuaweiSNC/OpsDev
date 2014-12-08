package com.huawei.network.opsdev.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 属性管理
 * @author qWX182698
 *
 */
public class PropertiesManager {
    private Properties properties = new Properties();
    
    public  Object getProperties(String propertiesPath ,String attributeName){
        try {
            properties.load(new FileInputStream(new File("velocity.properties")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return properties.get(attributeName);
    }
    
}
