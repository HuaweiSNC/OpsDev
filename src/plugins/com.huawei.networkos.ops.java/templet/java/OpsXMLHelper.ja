/*********************************************************************
Copyright, 2008-2010, Huawei Tech. Co., Ltd.
All Rights Reserved
----------------------------------------------------------------------
Project Code  : OPS V2.1
 *********************************************************************/
package util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helps mapping the XML to object by instantiating and initializing beans.
 * defines the operations to be performed.
 */
public class OpsXMLHelper {

	private static final Logger LOG = Logger.getLogger(OpsXMLHelper.class);

	public OpsXMLHelper() {
		
	}

	class RestApiHelper
	{
		private String fullUrlPath = "";
		private Map<String, String> setMothod = new LinkedHashMap<String, String>();
		private List<Element> childBodys = new ArrayList<Element>();
		private List<JSONArray> childJsonBodys = new ArrayList<JSONArray>();
		public String getFullUrlPath() {
			return fullUrlPath;
		}
		
		public List<JSONArray> getChildJsonBodys() {
			return childJsonBodys;
		}

		public Map<String, String> getSetMothod() {
			return setMothod;
		}
 
		public List<Element> getChildBodys() {
			return childBodys;
		}
  
		public void setFullUrlPath(String fullUrlPath) {
			this.fullUrlPath = fullUrlPath;
		}
		
		public void addBody(JSONArray body)
		{
			if (body != null)
			{
				childJsonBodys.add(body);
			}
		}
		
		public void addBody(Element body)
		{
			if (body != null)
			{
				childBodys.add(body);
			}
		}
		
		public void addMothod(String name, String value)
		{
			if (RestUtil.isNotEmpty(name))
			{
				setMothod.put(name, value);
			}
		}
		
		public RestApiHelper clone()
		{
			
			RestApiHelper help = new RestApiHelper();
			for(Element element : childBodys)
			{
				help.addBody(element);
			}
			
			for (String key: setMothod.keySet())
			{
				help.addMothod(key, setMothod.get(key));
			}
			
			help.setFullUrlPath(fullUrlPath);
			
			return help;
			
		}
		
	}
	
	/***
	 * 
	 * @param objectJson
	 * @param schemaApiPath
	 * @param motherClass
	 * @param pluginClass
	 * @return
	 * @throws Exception
	 */
	public Object configure(JSONObject objectJson, String schemaApiPath,  Object motherClass, Class pluginClass) throws Exception {
 
		   List<Object> objects = new ArrayList<Object>();
	       if (RestUtil.isEmpty(schemaApiPath))
	       {
	    	   Object object = configure(objectJson.getJSONArray("null"), pluginClass, motherClass);
	    	   objects.add(object);
	    	   // add body 
	    	   addBody(motherClass, objects, pluginClass);
	    	   return motherClass;
	           
	       }
	       
	       // parse schema api 
	       RestApiHelper restApi = getRestApiHelperFromElement(objectJson, schemaApiPath);
	       restApi.getSetMothod();
	       
	       Map<String, String> setMethodValue = restApi.getSetMothod();
	       List<JSONArray> arrayJson = restApi.getChildJsonBodys();

	       // set every body method
	       Object pluginInstance = null;
	       for(JSONArray body : arrayJson)
	       {
	    	   pluginInstance = configure(body, pluginClass, motherClass);
	    	   if (null != pluginInstance){
	    		   objects.add(pluginInstance);
	    	   }
	       }
	       
	       // add body 
	       addBody(motherClass, objects, pluginClass);
	       //  set url for class
	       setMothod(motherClass, setMethodValue);
	       
	       return motherClass;
	}
	
	/**
	 * 
	 * @param objectElement
	 *            the JDOM Element defining the plugin configuration
	 * @param pluginClass
	 *            the class to instantiate
	 * @param skipChildElements
	 *            <code>false</code> to recurse the configuration,
	 *            <code>true</code> otherwise
	 * @return fully configured Object
	 * @see #configure(org.jdom.Element, Object, boolean)
	 * @throws Exception
	 *             if the plugin class cannot be instantiated, if the
	 *             configuration fails
	 */
	public Object configure(Element objectElement, String schemaApiPath,  Object motherClass, Class pluginClass) throws Exception {

	   List<Element> oneBodys = new ArrayList<Element>();
	   List<Object> objects = new ArrayList<Object>();
       if (RestUtil.isEmpty(schemaApiPath))
       {
    	   Object object = configure(objectElement, pluginClass, motherClass);
    	   objects.add(object);
    	   // add body 
    	   addBody(motherClass, objects, pluginClass);
    	   return motherClass;
           
       }
       
       // parse schema api 
       RestApiHelper restApi = getRestApiHelperFromElement(objectElement, schemaApiPath);
       restApi.getSetMothod();
       
       Map<String, String> setMethodValue = restApi.getSetMothod();
       oneBodys.addAll(restApi.getChildBodys());

       // set every body method
       Object pluginInstance = null;
       for(Element body : oneBodys)
       {
    	   pluginInstance = configure(body, pluginClass, motherClass);
    	   if (null != pluginInstance){
    		   objects.add(pluginInstance);
    	   }
       }
       
       // add body 
       addBody(motherClass, objects, pluginClass);
       
       //  set url for class
       setMothod(motherClass, setMethodValue);

       return motherClass;
       
    }
 
	/***
	 * get RestApiHelper for json class
	 * @param objectElement
	 * @param schemaApiPath
	 * @return
	 */
	public RestApiHelper getRestApiHelperFromElement(JSONObject objectElement, String schemaApiPath) {
		
		
		RestApiHelper root = new RestApiHelper();
		String[] schemaApiPaths = schemaApiPath.split("[/]");
		JSONObject rootElement = objectElement;
		JSONArray jsonArray = null;
		String apiPath  = "";
		root.setFullUrlPath(schemaApiPath);
		boolean isRoot = true;
		for(int j = 0; j < schemaApiPaths.length; j++)
		{
			apiPath = schemaApiPaths[j];
			if (RestUtil.isEmpty(apiPath))
			{
				continue;
			}
			
			String elementName = "";
			String elementAttrib = "";
			String attribName = "";
			String attribValue = "";
			String[] elementArg = apiPath.split("[?]");
			if (elementArg.length == 1)
			{
				elementName = apiPath;
			} else if (elementArg.length == 2) {
				elementName = elementArg[0];
				elementAttrib = elementArg[1];
			}
			
			if(RestUtil.isNotEmpty(elementAttrib))
			{
				String[] lstArg = elementAttrib.split("[&]");
				for(String strParam: lstArg)
				{
					String[] nameValue = strParam.split("[=]");
					if (nameValue.length == 2)
					{
						attribName = nameValue[0];
						attribValue = nameValue[1];
						root.addMothod(attribName, attribValue);
					}
				}
			}
			
			// if root 
			if (isRoot)
			{
				if (!rootElement.has(elementName))
				{
					break;
				}
 
				jsonArray = rootElement.getJSONArray(elementName);
				isRoot = false;
				continue; 
			}
			
			// get node list
			if(jsonArray.size() > 0)
			{
				if(j + 1 == schemaApiPaths.length)
				{
					for(int i =0; i < jsonArray.size(); i++)
					{
						JSONObject node = jsonArray.getJSONObject(i);
						JSONArray jsonArrayChild = node.getJSONArray(elementName);
						if (jsonArrayChild != null)
						{
							root.addBody(jsonArrayChild);
						}
					}
					
				} else {
					rootElement = jsonArray.getJSONObject(0);
					jsonArray = rootElement.getJSONArray(elementName);
				}
			}
		}  
		
		return root;
	}
	
	
	/***
	 * get RestApiHelper for xml class
	 * @param objectElement
	 * @param schemaApiPath
	 * @return
	 */
	public RestApiHelper getRestApiHelperFromElement(Element objectElement, String schemaApiPath) {
		
		
		RestApiHelper root = new RestApiHelper();
		String[] schemaApiPaths = schemaApiPath.split("[/]");
		Element rootElement = objectElement;
		String apiPath  = "";
		root.setFullUrlPath(schemaApiPath);
		boolean isRoot = true;
		for(int j = 0; j < schemaApiPaths.length; j++)
		{
			apiPath = schemaApiPaths[j];
			if (RestUtil.isEmpty(apiPath))
			{
				continue;
			}
			
			String elementName = "";
			String elementAttrib = "";
			String attribName = "";
			String attribValue = "";
			String[] elementArg = apiPath.split("[?]");
			if (elementArg.length == 1)
			{
				elementName = apiPath;
			} else if (elementArg.length == 2) {
				elementName = elementArg[0];
				elementAttrib = elementArg[1];
			}
			
			if(RestUtil.isNotEmpty(elementAttrib))
			{
				String[] lstArg = elementAttrib.split("[&]");
				for(String strParam: lstArg)
				{
					String[] nameValue = strParam.split("[=]");
					if (nameValue.length == 2)
					{
						attribName = nameValue[0];
						attribValue = nameValue[1];
						root.addMothod(attribName, attribValue);
					}
				}
			}
			
			// if root
			if (isRoot)
			{
				
				if (!StringUtils.equals(elementName, rootElement.getNodeName()))
				{
					break;
				}
 
				isRoot = false;
				continue; 
			}
			
			
			// get node list
			NodeList lstChildNode = rootElement.getElementsByTagName(elementName);
			if(lstChildNode.getLength() > 0)
			{
				if(j + 1 == schemaApiPaths.length)
				{
					for(int i =0; i < lstChildNode.getLength(); i++)
					{
						Node node = lstChildNode.item(i);
						Element element = (Element) node;
						root.addBody(element);
					}
				
				} else {
					Node node = lstChildNode.item(0);
					rootElement = (Element) node;
				}
			}
		}  
		
		return root;
		
	}
	
	/***
	 * add body
	 * @param motherClass
	 * @param objects
	 * @param pluginClass
	 * @throws Exception
	 */
	public void addBody(Object motherClass,	List<Object> objects, Class pluginClass) throws Exception {

		// get method addbody
		Method addBody = motherClass.getClass().getMethod("addBody",
				pluginClass);
 
		// set Add 
		for (Object childObject : objects) {
			
			if (addBody != null) {
				try {
					LOG.debug("treating child with adder " + " adding ");
					addBody.invoke(motherClass, new Object[] { childObject });
				} catch (Exception e) {
					LOG.fatal("Error configuring plugin.", e);
				}
			} else {
				throw new Exception(
						"Nested element: 'addBody' is not supported for the <"
								+ childObject + "> tag.");
			}
		}
	}
	
	/***
	 * set mothod
	 * @param sourceClass
	 * @param getMethod
	 * @throws Exception
	 */
	public Object getObjectFromClass(Object sourceClass, String methodName) throws Exception {

		// get operation from one method
		Method[] methods = sourceClass.getClass().getMethods();
		Object valueObject = null;
		for (int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			final String name = method.getName();
			if (name.equals(methodName)) {
				valueObject = callgetter(method, sourceClass);
				break;
			}
		}
		return valueObject;
	}

	
	/***
	 * set mothod
	 * @param sourceClass
	 * @param getMethod
	 * @throws Exception
	 */
	public void getValueFromGetMethod(Object sourceClass, Map<String, String> outPutValueMap) throws Exception {

		// get operation from one method
		Method[] methods = sourceClass.getClass().getMethods();
		String nameStr = null;
		Object value = null;
		String valueStr = null;
		for (int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			final String name = method.getName();

			if (name.startsWith("get") && !"getClass".equals(name)) {
				nameStr = name.substring("get".length());
				nameStr = nameStr.replaceFirst(nameStr.substring(0, 1), nameStr.substring(0, 1).toLowerCase())  ; 
				value = callgetter(method, sourceClass);
				valueStr = value.toString();
				outPutValueMap.put(nameStr, value.toString());
			}
		}
 
	}

	
	/***
	 * set mothod
	 * @param motherClass
	 * @param setMethod
	 * @throws Exception
	 */
	public void setMothod(Object motherClass, Map<String, String> setMethod) throws Exception {

		// get all set operation
		Map setters = new HashMap();
		Method[] methods = motherClass.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			final String name = method.getName();

			if (name.startsWith("set")) {
				setters.put(name.substring("set".length()).toLowerCase(),
						method);
			}
		}
	 
		//  set method
		for (String key : setMethod.keySet()) {
			String value = setMethod.get(key);
			if (RestUtil.isNotEmpty(key)) {
				callSetter(key, value, setters, motherClass);
			}
		}
	}

	/**
	 * Instantiate a plugin
	 * 
	 * @param pluginClass
	 * @return The instantiated plugin
	 * @throws Exception
	 *             if the plugin class cannot be instantiated
	 */
	private Object instantiatePlugin(Class pluginClass, Object motherClass) throws Exception {
		Object pluginInstance;

		try {
			 
		      pluginInstance = pluginClass.getConstructors()[0].newInstance(motherClass);   
		 
		} catch (Exception e) {
			LOG.fatal("Could not instantiate class", e);
			e.printStackTrace();
			throw new Exception("Could not instantiate class: "
					+ pluginClass.getName());
			
		}
		return pluginInstance;
	}

	
	public Object configure(JSONArray objectElement, Class pluginClass, Object motherClass)
			throws Exception {

		Object pluginInstance = instantiatePlugin(pluginClass, motherClass);
		LOG.debug("configure " + pluginClass + " instance "
				+ pluginInstance.getClass() + " self configuring: "
				+ (pluginInstance instanceof SelfConfiguringPlugin));

		if (pluginInstance instanceof SelfConfiguringPlugin) {
			((SelfConfiguringPlugin) pluginInstance).configure(objectElement);
		} else {
			configureObject(objectElement, pluginInstance);
		}
		return pluginInstance;
	}
	
	/**
	 * Same as {@link #configure(org.jdom.Element, Class, boolean)}, except that
	 * the client already has a pluginInstance.
	 * 
	 * @throws Exception
	 *             if the configuration fails
	 */
	public Object configure(Element objectElement, Class pluginClass, Object motherClass)
			throws Exception {

		Object pluginInstance = instantiatePlugin(pluginClass, motherClass);
		LOG.debug("configure " + objectElement.getNodeName() + " instance "
				+ pluginInstance.getClass() + " self configuring: "
				+ (pluginInstance instanceof SelfConfiguringPlugin));

		if (pluginInstance instanceof SelfConfiguringPlugin) {
			((SelfConfiguringPlugin) pluginInstance).configure(objectElement);
		} else {
			configureObject(objectElement, pluginInstance);
		}
		return pluginInstance;
	}

	/**
	 * Configure the specified plugin object given the JDOM Element defining the
	 * plugin configuration.
	 * 
	 * @param objectElement
	 *            the JDOM Element defining the plugin configuration
	 * @param object
	 *            the instance to configure to instantiate
	 */
	protected void configureObject(Element objectElement, Object object)
			throws Exception {

		LOG.debug("configuring object " + objectElement.getNodeName()
				+ " object " + object.getClass());

		Map<String, Method> setters = getSetters(object);

		// set method
		setFromElement(objectElement, setters, object);

	}
	
	private Map<String, Method> getSetters(Object object)
	{
		Map<String, Method> setters = new HashMap<String, Method>();

		Method[] methods = object.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			final Method method = methods[i];
			final String name = method.getName();

			if (name.startsWith("set")) {
				setters.put(name.substring("set".length()).toLowerCase(),
						method);
			} else if (name.startsWith("create")) {
				// creators.put(name.substring("create".length()).toLowerCase(),
				// method);
			} else if (name.equals("add")
					&& method.getParameterTypes().length == 1) {
				// adders.add(method);
			}
		}

		return setters;
	}
	
	
	protected void configureObject(JSONArray objectElement, Object object)
			throws Exception {

		LOG.debug("configuring object " + object.getClass());

		Map<String, Method> setters = getSetters(object);
		 
		setFromElement(objectElement, setters, object);

	}
	
	private void setFromElement(JSONArray objectElement, Map<String, Method> setters,
			Object object) throws Exception {

		for(int i = 0; i < objectElement.size(); i++)
		{
			JSONObject jobject = objectElement.getJSONObject(i);
			for(Object key : jobject.keySet())
			{
				String keyValue = key.toString();
				String value = jobject.getString(keyValue);
				callSetter(keyValue, value, setters,
						object);
			}
		}
	}
	
	private void setFromElement(Element objectElement, Map<String, Method> setters,
			Object object) throws Exception {

		NodeList list = objectElement.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == node.ELEMENT_NODE) {
				if (StringUtils.isNotBlank(node.getTextContent()))
				{
					callSetter(node.getNodeName(), node.getTextContent(), setters,
							object);
				}
			}
		}
	}

	private boolean validateString(String source) {

		if (source != null && source.length() > 0) {
			return true;
		}
		return false;
	}

	/****
	 * call setter
	 * 
	 * @param propName
	 * @param propValue
	 * @param setters
	 * @param object
	 * @throws Exception
	 */
	private void callSetter(String propName, String propValue, Map<String, Method> setters,
			Object object) throws Exception {

		if (setters.containsKey(propName.toLowerCase())) {
			LOG.debug("Setting " + propName.toLowerCase() + " to " + propValue);
			try {
				Method method = (Method) setters.get(propName.toLowerCase());
				Class[] parameters = method.getParameterTypes();
				if (String.class.isAssignableFrom(parameters[0])) {
					method.invoke(object, new Object[] { propValue });
				} else if (int.class.isAssignableFrom(parameters[0])) {
					if (validateString(propValue)) {

						try {
							Integer tmpValue = Integer.valueOf(propValue);
							method.invoke(object,
									new Object[] { Integer.valueOf(propValue) });
						} catch (NumberFormatException e) {
							LOG.error("Error configuring plugin.", e);
						}

					}

				} else if (long.class.isAssignableFrom(parameters[0])) {

					if (validateString(propValue)) {

						try {
							Long tmpValue = Long.valueOf(propValue);
							method.invoke(object,
									new Object[] { Long.valueOf(propValue) });
						} catch (NumberFormatException e) {
							LOG.error("Error configuring plugin.", e);
						}
					}

				} else if (boolean.class.isAssignableFrom(parameters[0])) {
					method.invoke(object,
							new Object[] { Boolean.valueOf(propValue) });

				} else {
					LOG.error("rCouldn't invoke setter "
							+ propName.toLowerCase());
				}
			} catch (Exception e) {
				LOG.error("Error configuring plugin.", e);
			}
		} else {
			LOG.info("Attribute: '" + propName + "' is not supported for class: '" + object.getClass().getName() + "'.");
			//throw new Exception("Attribute: '" + propName
			//		+ "' is not supported for class: '"
			//		+ object.getClass().getName() + "'.");
		}
	}
	

	/****
	 * call setter
	 * 
	 * @param propName
	 * @param propValue
	 * @param getters
	 * @param object
	 * @throws Exception
	 */
	private Object callgetter(Method getter, Object object) throws Exception {

		Object retValue = null;
		LOG.debug("getting " + getter.getName());
		try {
			Method method = getter;
			Class[] parameters = method.getParameterTypes();
			if (parameters.length == 0)
			{
				Object valueRet = method.invoke(object, new Object[] { });
				if(valueRet != null)
				{
					retValue = valueRet;
				}
			}
		 
		} catch (Exception e) {
			LOG.error("Error configuring plugin.", e);
		}
		 
		return retValue;
	}
	 
	 
	  
}
