package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/***
 * ops service
 * 
 * @author OPSDEV
 * 
 */
public class OpsServiceCfgXmlutil {

	private Map<String, OpsDevice> opsDevices = new HashMap<String, OpsDevice>();
	private String OpsServiceConfigXmlPath;// config url
	Document document;

	public OpsServiceCfgXmlutil(String OpsServiceConfigXmlPath) {
		this.OpsServiceConfigXmlPath = OpsServiceConfigXmlPath;
		this.init();
	}

	private void init()  
	{
		File file = new File(OpsServiceConfigXmlPath);
		if (!file.exists() && !file.isFile()) {
			System.out.println("can not find file:" + file.getAbsolutePath());
			return;
		}
		try {
			document = RestUtil.getDoc(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * get device information
	 * @param deviceName
	 * @return
	 */
	public OpsDevice getDeivceById(String deviceId)
	{
		return opsDevices.get(deviceId);
	} 
	

	/**
	 * get device name
	 * @param deviceName 
	 * @return
	 */
	public OpsDevice getDeivceByName(String deviceName)
	{
		OpsDevice retDevice = null;
		Set<String> lstKey = opsDevices.keySet();
		for(String key :lstKey)
		{
			retDevice = opsDevices.get(key);
			if (deviceName != null &&deviceName.equalsIgnoreCase(retDevice.getDeviceName()))
			{
				return retDevice;
			}
		}
		return retDevice;
	} 
	/**
	 * parse device list
	 * 
	 * @return
	 */
	public void parseDevicesmap() {
		
		if (null == document)
		{
			return;
		}
		
		Element element = document.getDocumentElement();
		NodeList elements = element.getElementsByTagName("device");
		for (int i = 0; i < elements.getLength(); i++) {

			Node node = elements.item(i);
			Element ele = (Element) node;
			String deviceName = "";
			String sname = "";
			String userName = "";
			String id = "";
			String version = "";
			String productType = "";
			String server = "";
			String port = "";
			String passwd = "";
			String protocal = "";
			String trustStore = "";
			String storePassword = "";

			NodeList childElements = ele.getChildNodes();
			for (int j = 0; j < childElements.getLength(); j++) {
				String name = childElements.item(j).getNodeName();
				String value = childElements.item(j).getTextContent();
				if ("name".equals(name)) {
					sname = value;
				} else if ("deviceName".equals(name)) {
					deviceName = value;
				} else if ("server".equals(name)) {
					server = value;
				} else if ("port".equals(name)) {
					port = value;
				} else if ("userName".equals(name)) {
					userName = value;
				} else if ("passwd".equals(name)) {
					passwd = value;
				} else if ("id".equals(name)) {
					id = value;
				} else if ("version".equals(name)) {
					version = value;
				} else if ("productType".equals(name)) {
					productType = value;
				}else if ("protocal".equals(name)) {
					protocal = value;
				}else if("trustStore".equals(name)){
					trustStore = value;
				}else if("storePassword".equals(name)){
					storePassword = value;
				}

			}
			 
			if (sname != null && sname.length() > 0) {
				OpsDevice device = new OpsDevice(sname, deviceName, server,
						Integer.parseInt(port), userName, passwd, id,  version,
						productType,protocal,trustStore,storePassword);
				opsDevices.put(id, device);
			}

		}
	}
}
