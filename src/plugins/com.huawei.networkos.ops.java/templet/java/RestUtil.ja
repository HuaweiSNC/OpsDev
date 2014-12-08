package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.sf.json.JSONObject;

public class RestUtil {

	public static Boolean checkType(JSONObject criterion, String value) {

		String ipv4Pattern = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
		String ipv6Pattern = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
				+ "|(((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0"
				+ "-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-"
				+ "4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-"
				+ "9]|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1"
				+ ",4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4"
				+ "][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9"
				+ "]|1[0-9][0-9]|[1-9]?[0-9])){3})|:))|(([0-9A-Fa-f]{1,"
				+ "4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,"
				+ "4})?:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])("
				+ ".(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))"
				+ "|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,"
				+ "4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4][0-9]|"
				+ "1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9"
				+ "][0-9]|[1-9]?[0-9])){3}))|:))|(([0-9A-Fa-f]{1,4}:){2"
				+ "}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,"
				+ "3}:((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])(.("
				+ "25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])){3}))|:"
				+ "))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6}"
				+ ")|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4][0-9]|1["
				+ "0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0-9]["
				+ "0-9]|[1-9]?[0-9])){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){"
				+ "1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4][0-9"
				+ "]|1[0-9][0-9]|[1-9]?[0-9])(.(25[0-5]|2[0-4][0-9]|1[0"
				+ "-9][0-9]|[1-9]?[0-9])){3}))|:)))(%.+)?)";
		String dataType = criterion.getString("data-type");
		if ("BOOL".equals(dataType)
				&& !("true".equalsIgnoreCase(value) || "false"
						.equalsIgnoreCase(value))) {
			return false;
		}
		if ("IPV4".equals(dataType) && !value.matches(ipv4Pattern)) {
			return false;
		}
		if ("UINT32".equals(dataType)) {
			try {
				Integer.parseInt(value);
			} catch (Exception e) {
				return false;
			}
		}
		if ("USHORT".equals(dataType)) {
			try {
				int x = Integer.parseInt(value);
				if (x > 65535 || x < 0) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		if ("IPV4IPV6".equals(dataType)
				&& !(value.matches(ipv4Pattern) || value.matches(ipv6Pattern))) {
			return false;
		}
		if ("IPV6".equals(dataType) && !value.matches(ipv6Pattern)) {
			return false;
		}
		return true;

	}

	public static boolean validata(JSONObject map, String value) {

		// if (!criterions.containsKey(name)) {
		// return false;
		// }
		// JSONObject map = JSONObject.fromObject(criterions.get(name));
		if (map.containsKey("expected-values")) {
			String[] s = map.getString("expected-values").split(";");
			if (!Arrays.asList(s).contains(value)) {
				return false;
			}
		}
		if (map.containsKey("data-type")) {
			return checkType(map, value);
		}
		if (map.containsKey("max")) {
			int max = map.getInt("max");
			if (value.length() > max) {
				return false;
			}
		}
		if (map.containsKey("min")) {
			int min = map.getInt("min");
			if (value.length() < min) {
				return false;
			}
		}
		if (map.containsKey("max-val")) {
			int maxv = map.getInt("max-val");
			if (value.length() > maxv) {
				return false;
			}
		}
		if (map.containsKey("min-val")) {
			int minv = map.getInt("min-val");
			if (value.length() < minv) {
				return false;
			}
		}
		return true;
	}

	public static String parseUrlPath(String lastWord, boolean isStart,
			BodyType type) {

		StringBuffer buffer = new StringBuffer();
		String[] lstPath = lastWord.split("/");
		

		List<String> lstPathTmp = new ArrayList<String> ();
		for (int i = 0; i < lstPath.length; i++) {
			// if last path , not parse
			if (lstPath.length == i + 1) {
				continue;
			}

			lstPathTmp.add(lstPath[i]);
		}
		if (isStart)
		{
			for (String pathTmp : lstPathTmp) {
				buffer.append(parseOneUrlPath(pathTmp, isStart, type));
			}
		} else {
			for (int i = lstPathTmp.size(); i > 0 ; i--) {
				buffer.append(parseOneUrlPath(lstPathTmp.get(i - 1), isStart, type));
			}
		}

		return buffer.toString();
	}

	public static String parseOneUrlPath(String onePath, boolean isStart,
			BodyType type) {
		String pathName = "";
		String attribList = "";
		String suffixEnd = "/";
		if (isStart) {
			suffixEnd = "";
		}
		String[] spiltArg = onePath.split("[?]");
		StringBuffer buffer = new StringBuffer();
		if (spiltArg.length == 2) {
			pathName = spiltArg[0];
			attribList = spiltArg[1];

		} else if (spiltArg.length == 1) {
			pathName = spiltArg[0];
		} else {
			System.out.println("Error : " + onePath);
			return "";
		}

		if (null != pathName && pathName.length() > 0) {
			if (BodyType.APPLICATION_XML == type) {
				buffer.append("<").append(suffixEnd).append(pathName)
						.append(">");
			}
		}

		if (isStart) {
			buffer.append(parseUrlAttribute(attribList, type));
		}
		return buffer.toString();
	}

	/***
	 * parse url attribute ,for
	 * 
	 * @param node
	 * @param attribList
	 */
	public static String parseUrlAttribute(String urlPath, BodyType type) {
		if (null == urlPath || urlPath.length() == 0) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		String[] lstAttrib = urlPath.split("[&]");
		for (String strAttrib : lstAttrib) {
			String[] arrSpiltEqual = strAttrib.split("[=]");
			if (arrSpiltEqual.length == 2) {
				if (BodyType.APPLICATION_XML == type) {
					buffer.append("<").append(arrSpiltEqual[0]).append(">");
					buffer.append(arrSpiltEqual[1]);
					buffer.append("</").append(arrSpiltEqual[0]).append(">");
				}
			} else if (arrSpiltEqual.length == 1) {
				if (BodyType.APPLICATION_XML == type) {
					buffer.append("<").append(arrSpiltEqual[0]).append("/>");
				}
			}
		}
		return buffer.toString();
	}

	/***
	 * get prefix of body
	 * 
	 * @param fullUrlbody
	 *            full URL
	 * @param bMultiOperate
	 *            if multi-body
	 * @param bodyType
	 *            body type enum : APPLICATION_XML/APPLICATION_JSON
	 * @return
	 */
	public static String getBodyPrefix(String fullUrlbody,
			boolean bMultiOperate, BodyType bodyType) {
		// one body
		if (!bMultiOperate) {
			if (BodyType.APPLICATION_JSON == bodyType) {
				return "[";
			}

			return "";
		}

		// mulit-body
		String[] lastWord = fullUrlbody.split("/");
		if (lastWord.length > 0) {
			String lastW = lastWord[lastWord.length - 1];
			return RestUtil.parseOneUrlPath(lastW, true, bodyType);
		}

		return "";
	}

	/***
	 * get suffix of body
	 * 
	 * @param fullUrlBody
	 *            full URL
	 * @param bMultiOperate
	 *            if multi-body
	 * @param bodyType
	 *            body type enum : APPLICATION_XML/APPLICATION_JSON
	 * @return
	 */
	public static String getBodySuffix(String fullUrlBody,
			boolean bMultiOperate, BodyType bodyType) {
		// one body
		if (!bMultiOperate) {
			if (BodyType.APPLICATION_JSON == bodyType) {
				return "]";
			}
			return "";
		}

		// mulit-body
		String[] lastWord = fullUrlBody.split("/");
		if (lastWord.length > 0) {
			String lastW = lastWord[lastWord.length - 1];
			return RestUtil.parseOneUrlPath(lastW, false, bodyType);
		}
		return "";
	}

	/****
	 * get prefix of body
	 * 
	 * @param fullUrlBody
	 *            full URL
	 * @param bMultiOperate
	 *            if multi-body
	 * @param bodyType
	 *            body type enum : APPLICATION_XML APPLICATION_JSON
	 * @return
	 */
	public static String getPrefix(String fullUrlBody, boolean bMultiOperate,
			BodyType bodyType) {
		String nullSpace = "";
		// one body
		if (!bMultiOperate) {
			return nullSpace;
		}

		String urlPath = fullUrlBody;

		// mulit-body
		int indexArg = urlPath.indexOf("?");
		if (indexArg == -1) {
			return nullSpace;
		}

		String lastWord = urlPath.substring(indexArg);
		return RestUtil.parseUrlPath(lastWord, true, bodyType);
	}

	/***
	 * get suffix of body
	 * 
	 * @param fullUrlBody
	 *            full URL
	 * @param bMultiOperate
	 *            if multi-body
	 * @param bodyType
	 *            body type enum : APPLICATION_XML APPLICATION_JSON
	 * @return
	 */
	public static String getSuffix(String fullUrlBody, boolean bMultiOperate,
			BodyType bodyType) {

		String nullSpace = "";
		// one body
		if (!bMultiOperate) {
			return nullSpace;
		}

		String urlPath = fullUrlBody;

		// mulit-body
		int indexArg = urlPath.indexOf("?");
		if (indexArg == -1) {
			return nullSpace;
		}

		String lastWord = urlPath.substring(indexArg);

		return RestUtil.parseUrlPath(lastWord, false, bodyType);

	}

	public static boolean isNotEmpty(String str) {
		if (null != str && str.length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (null == str || str.length() == 0) {
			return true;
		}
		return false;
	}

	/****
	 * create a document from file
	 * 
	 * @param file
	 *            File for parse
	 * @return Document
	 */
	public static Document getDoc(File file)
			throws ParserConfigurationException, IOException, SAXException {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		doc = builder.parse(file);

		return doc;
	}
 
	/****
	 * create a document from given string
	 * 
	 * @param xmlStr
	 *            String for xml document
	 * @return Document
	 */
	public static Document getDoc(String xmlStr) {

		Document doc = null;
		try {
			StringReader sr = new StringReader(xmlStr);
			InputSource iSrc = new InputSource(sr);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.parse(iSrc);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return doc;
	}
 
    /****
	 * create a String from given File
	 * 
	 * @param file
	 *            File for txt document
	 * @return Document
	 */
    public static String readFile(File file) {
    	
    	StringBuffer buf = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
            	buf.append(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
        return buf.toString();
    }
     
}
