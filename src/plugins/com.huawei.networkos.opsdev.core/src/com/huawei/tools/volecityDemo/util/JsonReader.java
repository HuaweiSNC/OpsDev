package com.huawei.tools.volecityDemo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


import net.sf.json.JSONArray;


public class JsonReader {
	
	/**
	 * 从文件中读取json语句并生成相应的json java对象
	 * pathname:存储json语句的文件的文件名
	 * @return:返回一个JSONArray对象存储json对象
	 */
	public JSONArray readJson(String pathname) {
		Reader reader = null;
		JSONArray array =null;

		try {
			reader = new FileReader(new File(pathname));
			char[] temp = new char[10];
			StringBuffer buffer = new StringBuffer();
			while (reader.read(temp) != -1) {
				buffer.append(temp);
				temp = new char[10];
			}

			
			
			array = JSONArray.fromObject(buffer.toString().trim());
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;

	}
}
