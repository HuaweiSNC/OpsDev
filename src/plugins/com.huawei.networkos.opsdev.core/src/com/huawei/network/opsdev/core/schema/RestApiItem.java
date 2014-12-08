package com.huawei.network.opsdev.core.schema;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
 
/**
 * @author gWX179864
 *
 */
public class RestApiItem {

    private List<RestApiAttrib> lstRestAttrib = new LinkedList<RestApiAttrib>();
    private Map<String, RestApiAttrib> mapKeys = new LinkedHashMap<String, RestApiAttrib>();
	private String name;
	private String strUrl;
	private String strqueryUrl;
	private XmlObject xmlContent;
	private XmlObject xmlMotherDoc;
	private boolean isDealwith = false;
	 
	public List<String> getKeysByParentName(String parentName)
	{
	    List<String> lstAttribName = new ArrayList<String>(); 
	    RestApiAttrib attrib = null;
	    List<RestApiAttrib> lstRestApiAttrib = getlstKeys();
	    for(int i =0; i < lstRestApiAttrib.size(); i++)
	    {
	        attrib = lstRestApiAttrib.get(i);
	        if(attrib.getParentUrl().equalsIgnoreCase(parentName))
	        {
	            lstAttribName.add(attrib.getName());
	        } 
	    }
	    return lstAttribName;
	}
	
	public void addKey(RestApiAttrib pAttrib)
	{
	    isDealwith = false;
	    mapKeys.put(pAttrib.getUrl(), pAttrib);
	}
    public Map<String, RestApiAttrib> getMapKeys() {
        return mapKeys;
    }
    public List<RestApiAttrib> getlstKeys() {
        List<RestApiAttrib> lstRestApiKeys = new ArrayList<RestApiAttrib>();
        lstRestApiKeys.addAll(mapKeys.values());
        return lstRestApiKeys;
    }
    
    public void setMapKeys(Map<String, RestApiAttrib> mapKeys) {
        isDealwith = false;
        this.mapKeys = mapKeys;
    }
    public String getStrqueryUrl() {
        return strqueryUrl;
    }
    public void setStrqueryUrl(String strqueryUrl) {
        this.strqueryUrl = strqueryUrl;
    }
    public XmlObject getXmlContent() {
        return xmlContent;
    }
    public void setXmlContent(XmlObject xmlContent) {
        this.xmlContent = xmlContent;
    }
    public XmlObject getXmlMotherDoc() {
        return xmlMotherDoc;
    }
    public void setXmlMotherDoc(XmlObject xmlMotherDoc) {
        this.xmlMotherDoc = xmlMotherDoc;
    }
    public List<RestApiAttrib> getLstRestAttrib() {
		return lstRestAttrib;
	}
	public void setLstRestAttrib(List<RestApiAttrib> lstRestAttrib) {
		this.lstRestAttrib = lstRestAttrib;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStrUrl() {
		return strUrl;
	}
	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}
	public void addApiAttrib(RestApiAttrib pAttrib)
	{
	    isDealwith = false;
	    lstRestAttrib.add(pAttrib);
	}
	
    private String getUrlForRestApi(String[] lstUrl,int stati, String nodeName)
    {
        StringBuffer strBuf = new StringBuffer();
        if(stati < lstUrl.length && stati >=0)
        {
            for(int i = 0 ; i <= stati - 1 ; i++)
            {
                if(lstUrl[i].isEmpty())
                {
                    continue;
                }
                strBuf.append("/").append(lstUrl[i]);
            }
            strBuf.append("/").append(nodeName);
        }
        
        return strBuf.toString();
    }
	
	  /***
     * 刷新restapi,增加keys信息
     */
    public void refurbishRestApi()
    {
        // 如果已由新过，就不再进行刷新 
        if(isDealwith)
        {
            return;
        }
        
        String Url = this.strUrl;
        String[] lstUrl = Url.split("/");
        String nodeName;
        String nodeValue;
        XmlObject xmlRestApi = this.xmlMotherDoc;
        XmlCursor cursor = xmlRestApi.newCursor();
        for(int j = 0; j < lstUrl.length - 1; j++)
        {
            String currentNodeName = lstUrl[j];
            if(currentNodeName.isEmpty())
            {
                continue;
            }
            
            // 找到第一个节点，第一个节点不处理子节点
            if(1 == j && cursor.toFirstChild())
            {
                nodeName = cursor.getName().getLocalPart();
                if(nodeName.equalsIgnoreCase(currentNodeName))
                {
                    continue;
                }
                while(cursor.toNextSibling())
                {
                    nodeName = cursor.getName().getLocalPart();
                    if(nodeName.equalsIgnoreCase(currentNodeName))
                    {
                        break;
                    }   
                }
                continue;
            }
            
 
            // 进入子节点
            if(cursor.toFirstChild())
            {   
                if(!cursor.toFirstChild())
                {
                    nodeName = cursor.getName().getLocalPart();
                    nodeValue = cursor.getTextValue();
                    if(nodeValue.contains("'key':'true'"))
                    {
                        // add api attrib to api 
                        addRestApiKey(xmlRestApi, this, nodeName,getUrlForRestApi(lstUrl, j, nodeName) , nodeValue);
                    }
                } else 
                {
                    cursor.toParent();
                }

                while(cursor.toNextSibling())
                {
                    if(!cursor.toFirstChild())
                    {
                        nodeName = cursor.getName().getLocalPart();
                        nodeValue = cursor.getTextValue();
                        if(nodeValue.contains("'key':'true'"))
                        {
                            // add api attrib to api 
                            addRestApiKey(xmlRestApi, this,  nodeName, getUrlForRestApi(lstUrl, j, nodeName) , nodeValue);
                        } 
                    } else 
                    {
                        cursor.toParent();
                    }
                }
                
                // 重新定位到某个子节点
                cursor.toParent();
                if(cursor.toFirstChild())
                {
                    nodeName = cursor.getName().getLocalPart();
                    if(nodeName.equalsIgnoreCase(currentNodeName))
                    {
                        continue;
                    }
                    while(cursor.toNextSibling())
                    {
                        nodeName = cursor.getName().getLocalPart();
                        if(nodeName.equalsIgnoreCase(currentNodeName))
                        {
                            break;
                        }   
                    }
                    continue;
                }
            }
        }
        
        
        // 刷新 需要的URL地址信息
        StringBuffer lstbufUrlTmp = new StringBuffer();
        StringBuffer lstbufUrl = new StringBuffer();
        List<String> lstKeyAttribName = new ArrayList<String>();
        for(int j = 0; j < lstUrl.length; j++)
        {
            String currentNodeName = lstUrl[j];
            if(currentNodeName.isEmpty())
            {
                continue;
            }
            lstbufUrlTmp.append("/").append(currentNodeName);
            lstbufUrl.append("/").append(currentNodeName);
            lstKeyAttribName =  getKeysByParentName(lstbufUrlTmp.toString());
            if(lstKeyAttribName.size() > 0)
            {
                for(int k = 0; k <lstKeyAttribName.size(); k++)
                {
                    if (0 == k)
                    {
                        lstbufUrl.append("?").append(lstKeyAttribName.get(k)).append("=%s");
                    } else {
                        
                        lstbufUrl.append("&").append(lstKeyAttribName.get(k)).append("=%s");
                    }
                }
            }
        }
        strqueryUrl = lstbufUrl.toString();
        isDealwith = true;
        

    }
    
    private void addRestApiKey(XmlObject xmlRestApi, RestApiItem restApi, String attribName, String strAttrUrl,  String strJsonValue)
    {
            // add api attrib to api 
            strJsonValue = strJsonValue.replace(", ',",  ",");
            RestApiAttrib apiAttr = getRestfulApiAttribByUrl(xmlRestApi, attribName, strAttrUrl, strJsonValue); 
            if(null != apiAttr)
            {
                restApi.addKey(apiAttr);
            }
     }
    
    
    /***
     * @todo 解析json数据为具体的属性值数据
     * @param xmlDoc
     * @param url
     * @return 
     */
    public static RestApiAttrib getRestfulApiAttribByUrl(XmlObject xmlDoc, String attribName, String url, String strJsonValue)
    {
        //XmlCursor cursor = xmlDoc.newCursor();
        //cursor = cursor.execQuery(url);

        RestApiAttrib apiAttrib = new RestApiAttrib();
        apiAttrib.setUrl(url);
        
        int lastStatSpilt = url.lastIndexOf("/");
        apiAttrib.setParentUrl(url.substring(0, lastStatSpilt));
        
        apiAttrib.setDefJson(strJsonValue);
        apiAttrib.setName(attribName);
        
        // 判断是否为空
        
        if (null == strJsonValue || strJsonValue.isEmpty())
        {
            return null;
        } 
        
        // @todo 其它属性值的设置
        // 去除{}符号
        int firstStat = strJsonValue.indexOf("{");
        int lastStat = strJsonValue.lastIndexOf("}");
 

        
        String jsonValue = strJsonValue.substring(firstStat + 1, lastStat);
        String[] lstAtribGroup = jsonValue.split(",");
        String strAttribName = "";
        String strAttribValue = "";
        for(int i =0 ; i < lstAtribGroup.length; i++)
        {
            
            String[] lstAtribmember = lstAtribGroup[i].split(":");
            if(2 == lstAtribmember.length)
            {
                strAttribName = lstAtribmember[0];
                strAttribValue = lstAtribmember[1];
                strAttribName = strAttribName.substring( strAttribName.indexOf("'") + 1, strAttribName.lastIndexOf("'"));
                if(!strAttribValue.isEmpty())
                {
                   strAttribValue = strAttribValue.replace("@@@", ",");
        
                   strAttribValue = strAttribValue.substring( strAttribValue.indexOf("'") + 1, strAttribValue.lastIndexOf("'"));
                }
                if(strAttribName.equalsIgnoreCase("access"))
                {
                    apiAttrib.setAccess(strAttribValue);
                } else  if(strAttribName.equalsIgnoreCase("data-type"))
                {
                    apiAttrib.setDatetype(strAttribValue);
                }else  if(strAttribName.equalsIgnoreCase("min"))
                {
                    apiAttrib.setMin(strAttribValue);
                }else  if(strAttribName.equalsIgnoreCase("max"))
                {
                    apiAttrib.setMax(strAttribValue);
                } else  if(strAttribName.equalsIgnoreCase("example"))
                {
                    apiAttrib.setExample(strAttribValue);
                }
                else  if(strAttribName.equalsIgnoreCase("expected-values"))
                {
                    apiAttrib.setExceptedValue(strAttribValue);
                }
                else  if(strAttribName.equalsIgnoreCase("min-val"))
                {
                    apiAttrib.setMinValue(strAttribValue);
                }
                else  if(strAttribName.equalsIgnoreCase("max-val"))
                {
                    apiAttrib.setMaxValue(strAttribValue);
                }
            }
        }
        
        return apiAttrib;
    }
    
    
}
