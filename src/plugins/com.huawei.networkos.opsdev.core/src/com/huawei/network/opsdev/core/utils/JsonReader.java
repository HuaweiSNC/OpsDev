package com.huawei.network.opsdev.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


import net.sf.json.JSONArray;


public class JsonReader {
	
	/**
	 * ���ļ��ж�ȡjson��䲢�����Ӧ��json java����
	 * pathname:�洢json�����ļ����ļ���
	 * @return:����һ��JSONArray����洢json����
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
