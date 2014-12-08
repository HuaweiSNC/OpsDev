package com.huawei.network.opsdev.core.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
 
public class RestApiManager {

    
    private  Map<String, RestApiItem> m_mapRestApi = new LinkedHashMap<String, RestApiItem>();
    private  Map<String, List<String>> m_mapFileNameUrl =  new LinkedHashMap<String, List<String>>();
     
    public  RestApiManager()
    {
        
    }
    public void clear()
    {
        m_mapRestApi.clear();
        m_mapFileNameUrl.clear();
    }
    
    /***
     * 
     * 刷新restapi,增加keys信息
     */
    public void refurbishAllRestApi()
    {
        RestApiItem restApi = null;
        List<RestApiItem> lstRestApis = new ArrayList<RestApiItem>();
        lstRestApis.addAll(m_mapRestApi.values());
         
        for(int i =0 ; i < lstRestApis.size(); i++)
        {
            restApi = lstRestApis.get(i);
            restApi.refurbishRestApi();
        }
    }
  
    /**
     * 获取json语句中的地址
     * @return
     */
    public String getJsonUrl(String url, String mothed) {
        String str = url;
        String[] strs = str.split("/");
        StringBuffer buffer = new StringBuffer("");
        for (int i = 1; i < strs.length; i++) {
            if (i < strs.length - 2) {
                buffer.append("['" + strs[i] + "'][0]");
            }
            if (i == strs.length - 2) {
                buffer.append("['" + strs[i] + "'][" +  mothed + "]");
            }
            if (i == strs.length - 1) {
                buffer.append("['" + strs[i] + "']");
            }
        }
        return buffer.toString();
    }
    
    
    

    /**
     * 获取json语句中对应的index的地址
     * @return
     */
    public String getJsonIndexUrl(String url) {

        String str = url;
        String[] strs = str.split("/");
        StringBuffer buffer = new StringBuffer("");
        for (int i = 1; i < strs.length - 1; i++) {
            if (i < strs.length - 2) {
                buffer.append("['" + strs[i] + "'][0]");
            }
            if (i == strs.length - 2) {
                buffer.append("['" + strs[i] + "']");
            }

        }
        return buffer.toString();
    }
    
    public Map<String, RestApiItem> getMapRestApi() {
        return m_mapRestApi;
    }

    public Map<String, List<String>> getMapFileNameUrl() {
        return m_mapFileNameUrl;
    }

    public List<String> getUrlsbyFileName(String fileName) {
        
        List<String> lstUrl = new ArrayList<String>();
        if(m_mapFileNameUrl.containsKey(fileName))
        {
            return m_mapFileNameUrl.get(fileName);
        }
        return new ArrayList<String>();
    }
    
    public boolean isContainsUrl(String key)
    {
        return m_mapFileNameUrl.containsKey(key);
    }
    
    /***
     * 根据选择的列表，输出所选择的url
     * @param lstApiUrl 需要过滤的url列表
     * @return 当前输入列表中的url
     */
    public String getRestfulApiByList(List<String> lstApiUrl)
    {
        RestApiItem restApi = null;
        Map<String, RestApiItem> mapRestApi = new LinkedHashMap<String, RestApiItem>();
        for(int i = 0; i< lstApiUrl.size(); i++)
        {
            restApi = m_mapRestApi.get(lstApiUrl.get(i));
            if(null != restApi)
            {
                mapRestApi.put(lstApiUrl.get(i), restApi);
            }
        }
        return processRestfulApi(mapRestApi);
    }
    
    /***
     * 获取当前加载的所有的restapi
     * @return 当前api的json语句
     */
    public String getAllRestfulApi()
    {
       return processRestfulApi(m_mapRestApi);
    }
    
    /***
     * 根据url地址获取api
     * @param restApiUrl 当前需要的url路径
     * @return 当前api的json语句
     */
    public String getRestApiJsonByUrl(String restApiUrl)
    {
       
        Map<String, RestApiItem> mapRestApi = new LinkedHashMap<String, RestApiItem>();
        RestApiItem restApi = m_mapRestApi.get(restApiUrl);
        if(null != restApi)
        {
            mapRestApi.put(restApiUrl, restApi);
        }

        return processRestfulApi(mapRestApi);
    }
    
    /***
     * 根据当前数据api, 获取所需要的json表达式
     * @param _mapRestApi
     * @return 当前api列表所示的json表达式
     */
    private String processRestfulApi(Map<String, RestApiItem> _mapRestApi)
    {
        RestApiItem api = null;
        String restApiUrl = "";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("[\n");
        Iterator<String> its = _mapRestApi.keySet().iterator();
        List<RestApiAttrib> lstAttrs;
        List<RestApiAttrib> lstKeys;
        RestApiAttrib restApiAttrib = null;
        int isize = 0;
        while(its.hasNext())
        {
            isize++;
            restApiUrl = its.next();
            api = _mapRestApi.get(restApiUrl);
            api.refurbishRestApi();
            strBuf.append("    {  'url':'").append(restApiUrl).append("',\n");
            strBuf.append("       'curl':'").append(api.getStrqueryUrl()).append("',\n");
            
            //=======================attributes begin==============================//
            lstAttrs = api.getLstRestAttrib();
            strBuf.append("       'attributes': \n");
            strBuf.append("        {\n");
            for(int i = 0 ; i < lstAttrs.size(); i++)
            {
               restApiAttrib = lstAttrs.get(i);
               strBuf.append("            '").append(restApiAttrib.getName()).append("':").append(restApiAttrib.getDefJson());

               if(i + 1 != lstAttrs.size() && lstAttrs.size() != 1 )
               {
                   strBuf.append(",\n");
               }
            }
             strBuf.append("\n        },\n");
             //=======================attributes begin==============================//
             
             //=======================keys begin==============================//
             lstKeys = api.getlstKeys();
             strBuf.append("       'keys': \n");
             strBuf.append("        [\n");
             for(int i = 0 ; i < lstKeys.size(); i++)
             {
                restApiAttrib = lstKeys.get(i);
                strBuf.append("            {");
                strBuf.append(" '").append(restApiAttrib.getName()).append("':").append(restApiAttrib.getDefJson());
                strBuf.append(" }");
                if(i + 1 != lstKeys.size() && lstKeys.size() != 1 )
                {
                    strBuf.append(",\n");
                }
                
                
             }
              strBuf.append("\n        ]\n");
            //=======================keys end==============================//
              
             if(its.hasNext())
             {
                  strBuf.append("    },\n");
             } else {
                 strBuf.append("    }\n");
             }
        }
        strBuf.append("]\n");
        return strBuf.toString();
        
    }
    
 
    public XmlObject createXMLNode(XmlObject xmlDoc, List<String> lstAttribXpath)
    {
        //String attribXpath = "";
       /* XmlObject xmlNewobject = XmlObject.Factory.newInstance();
        XmlCursor xmlNewcursor = xmlNewobject.newCursor();
        xmlNewcursor.toNextToken();
        
        List<XmlObject> lstXmlObject = new ArrayList<XmlObject>();
        XmlObject xmlObject = null;
         
        //newCursor.toNextToken();
        XmlCursor oldCursor = xmlDoc.newCursor();
        
        if(oldCursor.toParent())
        {
            xmlObject = oldCursor.getObject();
            lstXmlObject.add(xmlObject);
        }
        
        String nodeName = null;
        for(int i = 0; i < lstAttribXpath.size(); i++)
        {
            nodeName = lstAttribXpath.get(i);
            if(null != nodeName)
            {
                xmlNewcursor.insertElement(nodeName);
                xmlNewcursor.toFirstChild();
            }
        }
        
        xmlNewcursor.insertChars(sample);
       */
/*
        XmlOptions options = new XmlOptions();
        options.put(XmlOptions.SAVE_PRETTY_PRINT);
        options.put(XmlOptions.SAVE_PRETTY_PRINT_INDENT, 2);
        options.put(XmlOptions.SAVE_USE_DEFAULT_NAMESPACE);
        String strXmlText = xmlDoc.xmlText(options);*/
        
        return null;
    }
    
    /***
     * 将restfulApi的属性值数据输入到库中
     * @param xmlDoc 当前所在的restful api文档
     * @param xmlContent 当前属性所在的restful api文档
     * @param attribName 当前属性节点
     * @param lstAttribXpath 当前url路径
     * @param strJsonValue 当前节点的属性值
     */
    public void addRestfulApiAttrib(XmlObject xmlDoc,  XmlObject xmlContent, String attribName, List<String> lstAttribXpath, String strJsonValue ,String fileName)
    {
     

        String apiName = "";
        StringBuffer strUrlBuffer = new StringBuffer();
        StringBuffer strAtrribBuffer = new StringBuffer();
        List<String> lstAttribMotherXpath = new ArrayList<String>();
        for(int i =0; i < lstAttribXpath.size(); i++)
        {
            strAtrribBuffer.append("/").append(lstAttribXpath.get(i));
            if( i < lstAttribXpath.size() - 1)
            {
                strUrlBuffer.append("/").append(lstAttribXpath.get(i)); 
            }
            if( i == lstAttribXpath.size() - 2)
            {
                apiName = lstAttribXpath.get(i);
            }
        }  
        
        // get restful api url
        String strUrl = strUrlBuffer.toString();
        // get restful api attrib 
        String strAttrUrl = strAtrribBuffer.toString();
        // get existed api
        RestApiItem pFulApi= getRestfulApiByUrl(strUrl);
        
        if (null != pFulApi && xmlDoc ==  pFulApi.getXmlMotherDoc())
        {
            
        } else {
            pFulApi = new RestApiItem();
            pFulApi.setStrUrl(strUrl);
            pFulApi.setName(apiName);
            pFulApi.setXmlMotherDoc(xmlDoc);
            
            //XmlObject xmlObject = createXMLNode(xmlContent, lstAttribXpath);
            pFulApi.setXmlContent(xmlContent);
            m_mapRestApi.put(strUrl, pFulApi);

            if(m_mapFileNameUrl.containsKey(fileName)){
                m_mapFileNameUrl.get(fileName).add(strUrl);
            }else{
                List<String> urls = new ArrayList<String>();
                urls.add(strUrl);
                m_mapFileNameUrl.put(fileName,urls);
            }
        }
        // add api attrib to api 
        strJsonValue = strJsonValue.replace(", ',",  ",");
        
        RestApiAttrib apiAttr = RestApiItem.getRestfulApiAttribByUrl(xmlDoc, attribName, strAttrUrl, strJsonValue);
        if(null != apiAttr)
        {
            pFulApi.addApiAttrib(apiAttr);
        }
    }
    
    
    
    public RestApiItem getRestfulApiByUrl(String url)
    {
       return m_mapRestApi.get(url);
    }
 
    /***
     * 通过api的名称获取当前的数据
     * @param name 名称
     * @return restful api 
     */
	public RestApiItem getRestApiByName(String name)
	{
		
		RestApiItem restApi = null;
		
		
		for(int i =0; i < m_mapRestApi.size(); i++)
		{
			restApi = m_mapRestApi.get(i);
			if(restApi.getName().equals(name))
			{
				return restApi;
			}
		}
		return null;
	}
	
	/***
	 * 根据url获取api类
	 * @param url
	 * @return
	 */
	public RestApiItem getRestApiByUrl(String url)
	{
	   return m_mapRestApi.get(url);
	}
 

}
