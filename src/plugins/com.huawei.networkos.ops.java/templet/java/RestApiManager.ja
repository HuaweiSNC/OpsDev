package util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class RestApiManager {

	private OpsRestCaller rest = null;
	private String errorstr = "";
	private Document bodyDoc = null;
	private OpsXMLHelper xmlHelper = new OpsXMLHelper();
	private List<String> lstUrlBody = new ArrayList<String>();
	private RestapiType actionType = RestapiType.RESTAPI;
	
	enum RestapiType {
		RESTAPI,
		SELFBODY,
	}
	
	/***
	 * get rest api manager
	 * @param rest
	 */
	public RestApiManager(OpsRestCaller rest) {

		this.rest = rest;
	}

	public void cleanBody() {
		lstUrlBody.clear();
		bodyDoc = null;
	}

	public OpsRestCaller getRest() {
		return rest;
	}
	
	public void setRest(OpsRestCaller rest) {
		this.rest = rest;
	}
	
	public RestapiType getActionType() {
		return actionType;
	}

	public void setActionType(RestapiType actionType) {
		this.actionType = actionType;
	}

	/***
	 * get body
	 * @return
	 */
	public String getBody()
	{
		if (null != bodyDoc)
		{
 
			StringWriter writer = new StringWriter();
			StreamResult stream = new StreamResult(writer);
			
			DOMSource domsource = new javax.xml.transform.dom.DOMSource(bodyDoc.getDocumentElement());
			try {
				Transformer tr = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
				
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		 
			 
				tr.transform(domsource, stream);
				//tr.setOutputProperty(OutputKeys., "");
				return writer.toString(); 
			} catch (TransformerException
					| TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	/***
	 * deal with url
	 * @param spiltUrl
	 */
	private void dealWithUrl( String[] spiltUrl)
	{
		
		List<String> lstUrlArray = new ArrayList<String>();
		int countj = 0;
        for(String siglePath : spiltUrl)
        {
        	countj++;
        	if (RestUtil.isEmpty(siglePath))
        	{
        	     continue;
        	}
        	
        	String apiPath = siglePath;
        	int indexof = siglePath.indexOf("?");
        	if(indexof != -1)
        	{
        		apiPath = siglePath.substring(0, indexof);
        		lstUrlArray.add(apiPath);
        		break;
        	} 
        	lstUrlArray.add(apiPath);
        }
        
        
		if (this.lstUrlBody.size() == 0)
		{
			this.lstUrlBody.addAll(lstUrlArray);
			
		} else {
			
			
			if (lstUrlArray.size() > 0 && this.lstUrlBody.size() > lstUrlArray.size())
			{
				for(int i = this.lstUrlBody.size(); i > lstUrlArray.size(); i--)
				{
					lstUrlBody.remove(i -1);
				}
			}
		}
	}
	
	/***
	 * add rest api
	 * @param restApi
	 * @return
	 * @throws Exception
	 */
	public boolean addRestApi(Object restApi, String action) throws Exception {

		// 判断是哪种类型数据
		if (!OpsRestCaller.ACTION_POST.equals(action) && !OpsRestCaller.ACTION_PUT.equals(action))
		{
			action = OpsRestCaller.ACTION_POST;
		}
	 
		// if doc equal null , then new it
		Element childRoot = null;
		Element childNode = null;
		Element paramNode = null;
		
		if (null == bodyDoc) {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException pce) {
				System.err.println(pce); // 出现异常时，输出异常信息
			}
	 
	        this.bodyDoc = db.newDocument();
		}
	        
	        // get restful url
	        Object urlPath = xmlHelper.getObjectFromClass(restApi, "getUrlBody");
	        String url = urlPath.toString();
	        String[] spiltUrl = url.split("/");
	        String lastElementName = null; 
	        int countj = 0;
	        String apiPath = null;
	        String apiPathParam = null;
	        dealWithUrl(spiltUrl);
	        for(String siglePath : spiltUrl)
	        {
	        	countj++;
	        	if (RestUtil.isEmpty(siglePath))
	        	{
	        	     continue;
	        	}
	        	
	        	Map<String, String> argMap = new LinkedHashMap<String, String> ();
	        	apiPathParam = null;
	        	apiPath = siglePath;
	        	String strName = "";
	        	String strValue = "";
	        	int indexof = siglePath.indexOf("?");
	        	if(indexof != -1)
	        	{
	        		apiPath = siglePath.substring(0, indexof);
	        		apiPathParam = siglePath.substring(indexof + 1);
	        		if(apiPathParam.indexOf("&") > -1)
	        		{
	        			String[] lstParam = apiPathParam.split("&");
	        			for(String param : lstParam)
	        			{
	        				putArgParamMap(param, argMap);
	        			}
	        		} else {
	        			putArgParamMap(apiPathParam, argMap);
	        		}
	        	} 
	        	
	        	lastElementName = apiPath;
        		// 如果是首选增加API
        		if (null == childRoot && null == this.bodyDoc.getDocumentElement())
        		{
        			childNode = this.bodyDoc.createElement(apiPath);
        			this.bodyDoc.appendChild(childNode);
        			childRoot = childNode;
        			// 增加param key数据内容
        			for(String argKey : argMap.keySet())
        			{
        				paramNode = this.bodyDoc.createElement(argKey);
        				paramNode.setTextContent(argMap.get(argKey));
        				childRoot.appendChild(paramNode);
        			}
        			continue;
        		}  
        		
        		// 如果是第二次增加API, 刚进入设置 
    			if (null == childRoot && this.bodyDoc.getDocumentElement()!= null)
    			{
    				Element childRootTmp = this.bodyDoc.getDocumentElement();
    				if(!childRootTmp.getNodeName().equals(apiPath))
    				{
    					return false;
    				}
    				childRoot = childRootTmp;
        			// 增加param key数据内容
        			for(String argKey : argMap.keySet())
        			{
        				paramNode = this.bodyDoc.createElement(argKey);
        				paramNode.setTextContent(argMap.get(argKey));
        				childRoot.appendChild(paramNode);
        			}
    				
    				continue;
    			}  
    			
    			// 进入获取数据区
				NodeList childList = childRoot.getElementsByTagName(apiPath);
				Element childSigleNode = null; 
				//boolean bFind = true;
				for(int i =0; i < childList.getLength(); i++)
				{
					// 如果有当前节点的根路径存在的话，就设置为找到，否则没有找到对应的节点
					Node node = childList.item(i);
					if (argMap.size() == 0)
					{
						childSigleNode = (Element)node;
						break;
					}
					
					boolean bFind = true;
	      			for(String argKey : argMap.keySet())
        			{
	      				Element childParamNode = (Element)node; 
	      				if (childParamNode.getElementsByTagName(argKey).getLength() == 0)
	      				{
	      					bFind = false;
	      					break;
	      				}
        			}
	      			
	      			if (bFind)
	      			{
	      				// 判断是否是需要是可以合并的的属性名称
						childSigleNode = (Element)node;
						break;
	      			}
				}
				
				if (null == childSigleNode || spiltUrl.length == countj)
				{
					childNode = this.bodyDoc.createElement(apiPath);
					childRoot.appendChild(childNode);
					childRoot = childNode;
					// 增加param key数据内容
        			for(String argKey : argMap.keySet())
        			{
        				paramNode = this.bodyDoc.createElement(argKey);
        				paramNode.setTextContent(argMap.get(argKey));
        				childRoot.appendChild(paramNode);
        			}
				} else {
					
					childRoot = childSigleNode;
					childNode = childSigleNode;
				}
	        }
	        
	        Element parentNode = (Element) childRoot.getParentNode();
	        dealwithaddRestApi(restApi, parentNode, childNode, lastElementName, action);
		
		return true;
	}
	
	/***
	 * put arg to param Map
	 * @param apiPathParam
	 * @param argMap
	 */
	private void putArgParamMap(String apiPathParam, Map<String, String> argMap)
	{
		
		if (RestUtil.isEmpty(apiPathParam))
		{
			return ;
		}
		String strName = "";
		String strValue = "";
		String[] lstArg = apiPathParam.split("=");
		if (lstArg.length > 1)
		{
			strName = lstArg[0];
			strValue = lstArg[1];
		} else {
			strName = apiPathParam;
			strValue = "";
		}
		argMap.put(strName, strValue);
	}
	
	/***
	 * deal with rest api
	 * @param restApi
	 * @param childRoot
	 * @param childNode
	 * @param lastElementName
	 * @throws Exception
	 */
	private void dealwithaddRestApi(Object restApi, Element childRoot, Element childNode, String lastElementName, String action) throws Exception
	{
		
		// 判断是哪种类型数据
		String operation = "create";
		if (!OpsRestCaller.ACTION_POST.equals(action) && !OpsRestCaller.ACTION_PUT.equals(action))
		{
			return;
		}
		if (OpsRestCaller.ACTION_PUT.equals(action))
		{
			operation = "merge";
		}
	 
        // get multiOperate data
        Object retIsMulti = xmlHelper.getObjectFromClass(restApi, "getbMultiOperate");
  
        Element childNodeTmp = childNode;
    	if (true == Boolean.valueOf(retIsMulti.toString()))
    	{
    		Object multiBody = xmlHelper.getObjectFromClass(restApi, "getMultiBody");
    		if (multiBody instanceof List)
    		{
    			int loopi = 0;
    			for(Object oneBody : (List) multiBody)
    			{
    				// if childNode is not equal 0, append a new node to it 
    				 if (loopi != 0)
    				 {
    					 childNodeTmp = childRoot.getOwnerDocument().createElement(lastElementName);
    					 childRoot.appendChild(childNodeTmp);
    				 }
    				 childNodeTmp.setAttribute("operation", operation);
	        		 dealwithRestApiBody(oneBody, childNodeTmp);
	        		 loopi++;
    			}
    		}
    		
    	} else {
    		
    		 Object oneBody = xmlHelper.getObjectFromClass(restApi, "getBody");
    		 childNodeTmp.setAttribute("operation", operation);
    		 dealwithRestApiBody(oneBody, childNodeTmp);
    	}
		
	}
	
	
	/***
	 * dealwith restapi body
	 * @param onbody
	 * @param parentNode
	 * @throws Exception
	 */
	private void dealwithRestApiBody(Object onbody, Element parentNode) throws Exception
	{
		if (null == parentNode || null == onbody)
		{
			return;
		}
	    Map<String, String> outPutValueMap = new LinkedHashMap<String, String>();
        xmlHelper.getValueFromGetMethod(onbody, outPutValueMap);
        String value = null;
        Element childNode = null;
        for(String key :outPutValueMap.keySet())
        {
        	value = outPutValueMap.get(key);
        	if (!RestUtil.isEmpty(value))
        	{
            	childNode = parentNode.getOwnerDocument().createElement(key);
            	childNode.setTextContent(value);
            	parentNode.appendChild(childNode);
        	}
        }
	}

	/***
	 * get param body
	 * @return
	 */
	public String getUrl() {

		if (this.lstUrlBody.size() == 0)
		{
			return "";
		}
		 
		return "/" + StringUtils.join(this.lstUrlBody, "/");
  
	}

	/**
	 * modify api, equal to method post of rest
	 * 
	 * @param attributes
	 *            : URL parameters, for example "?Xxx=yy&aa=b"
	 * @return get : url
	 * @throws Exception
	 */
	public boolean modify() throws Exception {

		String body = getUrl();
		if (RestUtil.isEmpty(body)) {
			return false;
		}

		String urlPath = getUrl();
		RetRpc response = this.getRest().put(urlPath, body);
		int status = response.getStatusCode();
		if (status == 200) {
			return true;
		}
		if (status != 200)
			this.setErrorstr(response.toString());
		return false;
	}

	/**
	 * create api, equal to method post of rest
	 * 
	 * @param attributes
	 *            : URL parameters, for example "?Xxx=yy&aa=b"
	 * @return get : url
	 * @throws Exception
	 */
	public boolean create() throws Exception {

		String body = getUrl();
		if (RestUtil.isEmpty(body)) {
			return false;
		}

		String urlPath = getUrl();
		RetRpc response = this.getRest().post(urlPath, getUrl());
		int status = response.getStatusCode();
		if (status == 200) {
			return true;
		}
		if (status != 200)
			this.setErrorstr(response.toString());
		return false;
	}
  
	public String getErrorstr() {
		return errorstr;
	}

	public void setErrorstr(String errorstr) {
		this.errorstr = errorstr;
	}

}
