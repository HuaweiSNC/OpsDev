package trap;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Alarm {

	private String alarmTime = "";
	private String trapOid = "";
	private Map<String, String> map = new HashMap<String, String>();

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public Alarm() {

	}

	public Alarm(String content) {
		try {
			Document doc = DocumentHelper.parseText(content);
			Element el = doc.getRootElement();
			Element elSnmpv2trap = el.element("snmpv2trap");
			Element elTimestamp = elSnmpv2trap.element("timestamp");
			setAlarmTime(elTimestamp.getTextTrim());
			Element elTrapoid = elSnmpv2trap.element("trapoid");
			map.put("trapName", getTrapName2(elTrapoid));
			setTrapOid(elTrapoid.getTextTrim());
			Element el4 = elSnmpv2trap.element("vbs");
			List<Element> elVbs = el4.elements("vb");
			for (Iterator<Element> i = elVbs.iterator(); i.hasNext();) {
				Element vb = i.next();
				String oid = "";
				String value = "";
				String key = "";
				if (null != vb.element("oid")
						&& !"".equals(vb.element("oid").getTextTrim())) {
					oid = vb.element("oid").getTextTrim();
					key = getVbName2(vb.element("oid"));
				}
				if (null != vb.element("value")
						&& !"".equals(vb.element("oid").getTextTrim())) {
					value = vb.element("value").getTextTrim();
				}
				if (!"".equals(key)) {
					map.put(key, value);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public String getTrapName2(Element elTrapoid) {
		String trapName = "";
		String oid = formatOid(elTrapoid.getTextTrim().trim());
		ReadConfig config = new ReadConfig();
		String path = config.getExcel("oid-trap2.xls");
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path));
			HSSFWorkbook workBook;
			workBook = new HSSFWorkbook(fs);
			System.out.println(workBook.getNumberOfSheets());
			for (int i = 1; i < workBook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = workBook.getSheetAt(i);
				int rows = sheet.getPhysicalNumberOfRows();
				if (rows > 0) {
					sheet.getMargin(HSSFSheet.TopMargin);
					for (int j = 0; j < rows; j++) {
						HSSFRow row = sheet.getRow(j);
						if (row != null) {
							int cells = row.getLastCellNum();
							if (cells < 2) {
								continue;
							}
							HSSFCell cell = row.getCell(0);
							HSSFCell cell2 = row.getCell(1);
							String value1 = "";
							String value2 = "";
							if (cell != null) {
								value1 = getHSSFCellValue(cell).trim();
							}
							if (cell2 != null) {
								value2 = getHSSFCellValue(cell2).trim();
							}
							if (value1.equals(oid)) {
								trapName = value2;
								break;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trapName;
	}

	public String getVbName2(Element elTrapoid) {
		String VbName = "";
		String oid = formatOid(elTrapoid.getTextTrim().trim());
		ReadConfig config = new ReadConfig();
		String path = config.getExcel("oid-trap2.xls");
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path));
			HSSFWorkbook workBook;
			workBook = new HSSFWorkbook(fs);
			for (int i = 1; i < workBook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = workBook.getSheetAt(i);
				int rows = sheet.getPhysicalNumberOfRows();
				if (rows > 0) {
					sheet.getMargin(HSSFSheet.TopMargin);
					for (int j = 0; j < rows; j++) {
						HSSFRow row = sheet.getRow(j);
						if (row != null) {
							int cells = row.getLastCellNum();
							if (cells < 11) {
								continue;
							}
							HSSFCell cell = row.getCell(8);
							HSSFCell cell2 = row.getCell(10);
							String value1 = "";
							String value2 = "";
							if (cell != null) {
								value1 = getHSSFCellValue(cell).trim();
							}
							if (cell2 != null) {
								value2 = getHSSFCellValue(cell2).trim();
							}
							if (value1.equals(oid)) {
								VbName = value2;
								break;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return VbName;
	}

	public String formatOid(String oid) {
		if (null != oid && !"".equals(oid) && oid.endsWith(".0")) {
			int i = oid.lastIndexOf(".0");
			return formatOid(oid.substring(0, i));
		} else {
			return oid;
		}
	}

	public String getOpsMessage2() {
		StringBuffer xml = new StringBuffer();
		xml.append("<snmpTrapPdu>");
		xml.append("<snmpv2trap>");
		xml.append("<timestamp>").append(getAlarmTime()).append("</timestamp>");
		xml.append("<trapoid>").append(getTrapOid()).append("</trapoid>");
		xml.append("<vbs>");
		for (String key : getMap().keySet()) {
			xml.append("<").append(key).append(">").append(getMap().get(key))
					.append("</").append(key).append(">");
		}
		xml.append("</vbs>");
		xml.append("</snmpv2trap>");
		xml.append("</snmpTrapPdu>");
		return xml.toString();
	}

	public String getOpsMessage3() {
		StringBuffer xml = new StringBuffer();
		xml.append("<snmpTrapPdu>");
		xml.append("<snmpv2trap>");
		xml.append("<timestamp>").append(getAlarmTime()).append("</timestamp>");
		xml.append("<trapoid>").append(getTrapOid()).append("</trapoid>");
		xml.append("<vbs>");
		int num = 0;
		for (String key : getMap().keySet()) {
			num++;
			xml.append("(").append(num).append(")").append(key).append(":")
					.append(getMap().get(key)).append(";");
		}
		xml.append("</vbs>");
		xml.append("</snmpv2trap>");
		xml.append("</snmpTrapPdu>");
		return xml.toString();
	}

	public String getHSSFCellValue(HSSFCell cell) {
		String value = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue())
						.toString();
			} else {
				value = String.valueOf(cell.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().toString();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getNumericCellValue());
			if (value.equals("NaN")) {
				value = cell.getRichStringCellValue().toString();
			}
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = "" + cell.getBooleanCellValue();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			value = "";
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = "";
			break;
		default:
			value = cell.getRichStringCellValue().toString();
		}
		return value;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getTrapOid() {
		return trapOid;
	}

	public void setTrapOid(String trapOid) {
		this.trapOid = trapOid;
	}

}
