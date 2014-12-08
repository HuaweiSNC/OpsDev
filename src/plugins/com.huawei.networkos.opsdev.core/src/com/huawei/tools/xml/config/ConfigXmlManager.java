package com.huawei.tools.xml.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.huawei.network.opsdev.core.utils.ConsoleFactory;

/**
 * 工程配置文件的管理
 * @author qWX182698
 *
 */
public class ConfigXmlManager {
    public String xmlOpsServicePath;
    private Document document;

    private String protocal;
    private String trustStore;
    private String storePassword;

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    /**
     * 构造器 ,构造一个新的管理对象,每个配置文件都对应一个管理对象
     * @param xmlOpsServicePath xml配置文件的路径
     */
    public ConfigXmlManager(String xmlOpsServicePath) {
        this.xmlOpsServicePath = xmlOpsServicePath;
        init();
    }

    /**
     * 初始化得过程,加载xml配置文件
     */
    private void init() {
        File file = new File(xmlOpsServicePath);
        if (!file.exists()) {
            initXmlObject(file);
            close();
            return;
        }
        if (!file.isFile()) {
            System.out.println("can not find file:" + file.getAbsolutePath());
            ConsoleFactory.printToConsole("can not find file:" + file.getAbsolutePath().toString(), true);
            return;
        }

        SAXReader reader = new SAXReader();
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 解析xml文件，获取truststore和storepassword
     */
    public void praseFile()
    {
        Element root = document.getRootElement();
        Element rootElement = root.element("device");

        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            if ("truststore".equals(element.getName())) {
                OpsService.trustStore = element.getText();
            } else if ("storepassword".equals(element.getName())) {
                OpsService.storePassword = element.getText();
            }
        }
    }

    /**
     * 将opsService的信息加入到Xml文件中
     * @param opsService
     * @return
     */
    public boolean addOpsServiceToXMl(OpsService opsService) {
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("device");
        for (Element element1 : elements) {
            if (element1.attributeValue("name").equals(opsService.getName())) {
                return false;
            }
        }

        Element deviceElement = rootElement.addElement("device");
        deviceElement.addAttribute("id", opsService.getDeviceId());
        deviceElement.addAttribute("name", opsService.getName());
        deviceElement.addAttribute("devicename", opsService.getDevicename());
        deviceElement.addElement("server").setText(opsService.getServer());
        deviceElement.addElement("protocal").setText(opsService.getProtocal());
        if (null != opsService.getTrustStore() && !"".equals(opsService.getTrustStore())
                && null != opsService.getStorePassword() && !"".equals(opsService.getStorePassword())) {
            deviceElement.addElement("truststore").setText(opsService.getTrustStore());
            deviceElement.addElement("storepassword").setText(opsService.getStorePassword());
        }else {
            deviceElement.addElement("truststore").setText("");
            deviceElement.addElement("storepassword").setText("");
        }
        
        if (opsService.getUserName() == null) {
            deviceElement.addElement("user_name").setText("");
        } else {
            deviceElement.addElement("user_name").setText(opsService.getUserName());
        }
        if (opsService.getPasswd() == null) {
            deviceElement.addElement("passwd").setText("");
        } else {
            deviceElement.addElement("passwd").setText(opsService.getPasswd());
        }
        deviceElement.addElement("port").setText(opsService.getPort());

        deviceElement.addElement("version").setText(opsService.getXmlVersion());
        deviceElement.addElement("producttype").setText(opsService.getProductType());
        Element element = deviceElement.addElement("handles");
        if (opsService.getHandles() != null) {
            for (OpsServiceUrlHandle handle : opsService.getHandles()) {
                Element element2 = element.addElement("handle").addAttribute("apiUrl", handle.getApiUrl())
                        .addAttribute("handle", handle.getHandle()).addAttribute("name", handle.getName())
                        .addAttribute("content_type", handle.getContentType())
                        .addAttribute("is_inner", handle.getIsInnerSchemmer().toString());
                for (Entry<String, String> entry : handle.getPropertiesHandles().entrySet()) {
                    element2.addElement("attribute").addAttribute("name", entry.getKey()).setText(entry.getValue());
                }

            }
        }
        close();
        return true;
    }

    /**
     * 更改某一个device的信息,device以name为唯一标识并且不可改变
     * @param opsService 更新后的opsService实体类
     * @return
     */
    public boolean modefyDevice(OpsService opsService) {
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("device");
        for (Element element1 : elements) {
            if (element1.attributeValue("name").equals(opsService.getName())) {
                rootElement.remove(element1);
                return addOpsServiceToXMl(opsService);
            }
        }
        System.out.println("the device is not exist!");
        ConsoleFactory.printToConsole("the device is not exist!", true);
        return false;
    }

    /**
     * 删除某一个device,device以name为唯一标识并且不可改变
     * @param opsService 需要删除的opsService
     * @return
     */
    public boolean delectDevice(OpsService opsService) {
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("device");
        for (Element element1 : elements) {
            if (element1.attributeValue("name").equals(opsService.getName())) {
                if (rootElement.remove(element1)) {
                    close();
                    return true;
                }
            }
        }
        System.out.println("The OpsService is not exist");
        ConsoleFactory.printToConsole("The OpsService is not exist", true);
        return false;

    }

    /**
     * 获取所有的device的列表
     * @return
     */
    public List<OpsService> getDevices() {
        List<OpsService> list = new ArrayList<OpsService>();
        Element element = document.getRootElement();
        List<Element> elements = element.elements("device");
        for (Element ele : elements) {
            List<OpsServiceUrlHandle> handles = new ArrayList<OpsServiceUrlHandle>();
            if (ele.element("handles") != null) {
                List<Element> elements2 = ele.element("handles").elements("handle");

                for (Element element2 : elements2) {
                    Map<String, String> map = new HashMap<String, String>();
                    List<Element> elements3 = element2.elements("attribute");
                    for (Element element3 : elements3) {
                        map.put(element3.attributeValue("name"), element3.getText());
                    }
                    handles.add(new OpsServiceUrlHandle(element2.attributeValue("apiUrl"), map, element2
                            .attributeValue("handle"), element2.attributeValue("name"), element2
                            .attributeValue("content_type"), Boolean.parseBoolean(element2.attributeValue("is_inner"))));

                }
            }

            list.add(new OpsService(ele.attributeValue("name"), ele.elementText("protocal"), ele.element("server")
                    .getText(), ele.elementText("port"), ele
                    .elementText("user_name"), ele.attributeValue("devicename"), ele.elementText("passwd"), ele
                    .attributeValue("id"), ele.elementText("version"), ele.elementText("producttype"), handles, ele
                    .elementText("truststore"), ele.elementText("storepassword")));
        }

        return list;
    }

    /**
     * 获取document对象
     * @return
     */
    public Document getDocument() {
        return document;
    }

    /**
     * 批量添加opsService到xml文件中
     * @param opsService
     */
    public void addOpsServiceToXMl(OpsService[] opsService) {
        for (OpsService opsService2 : opsService) {
            addOpsServiceToXMl(opsService2);
        }
    }

    /**
     * 初始化devices的element
     * @param xmlFile
     */
    public void initXmlObject(File xmlFile) {
        try {
            xmlFile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document document = DocumentFactory.getInstance().createDocument();

        document.addElement("DEVICES");
        this.document = document;

    }

    //    public void documentWrite(File xmlFile,Document document){
    //        Writer writer = null;
    //        try {
    //             writer=  new PrintWriter(xmlFile);
    //            document.write(writer);
    //        } catch (FileNotFoundException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        } catch (IOException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        } finally {
    //            try {
    //                writer.close();
    //            } catch (IOException e) {
    //                // TODO Auto-generated catch block
    //                e.printStackTrace();
    //            }
    //        }    
    //    }
    /**
     * 格式化写入到文件中并关闭对应的流
     */
    public void close()
    {

        FileOutputStream out = null;
        try
        {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            out = new FileOutputStream(xmlOpsServicePath);
            XMLWriter xmlWriter = new XMLWriter(out, format);
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (IOException e)
        {

        } finally
        {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
