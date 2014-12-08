package com.huawei.network.opsdev.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.internal.utils.FileUtil;

//import org.apache.velocity.texen.util.FileUtil;
/***
 * IO工具类
 * @author qWX182698
 *
 */
public class IOTools {
    /**
     * 实现文件的复制 从源文件到目标文件夹
     * @param sourceFile 源文件
     * @param targetFolder 目标文件夹
     */
    public static void copyFile(File sourceFile,File targetFile){
        BufferedInputStream inBuff  = null;
        BufferedOutputStream outBuff= null;
        if(!sourceFile.isFile()){
            System.out.println("sourceFile must be file :" + sourceFile);
            ConsoleFactory.printToConsole("Error : sourceFile is not exists :" + sourceFile, true);
            return;
           
        }
        
        try {
            
          //  FileUtil ee = new FileUtil();
       
            InputStream  inputStream = new FileInputStream(sourceFile);
            OutputStream   outputStream = new FileOutputStream(targetFile);
            inBuff = new BufferedInputStream(inputStream);
            outBuff = new  BufferedOutputStream(outputStream);
            
            byte[ ]b = new byte[1024 * 5];
            int len = 0;
            while((len = inBuff.read(b) ) != -1){
                outBuff.write(b, 0 , len);
            }
            outBuff.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inBuff !=  null) {
                try {
                    inBuff .close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (outBuff!=null) {
                try {
                    outBuff.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        }
        
         
    }
    
    /**
     * 重载
     * @param sourcePath
     * @param targetFolder
     */
    public static void copyFile(String sourcePath,String targetFileName){
        copyFile(new File(sourcePath), new File(targetFileName));
    }
    
    
}
