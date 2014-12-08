package com.huawei.network.opsdev.core.treeview.node;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.huawei.network.opsdev.core.utils.ConsoleFactory;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;
import com.huawei.tools.xml.config.ProjectConfigXmlManager;

/**
 * 数据创建工厂
 * @author qWX182698
 *
 */
public class DataFactory {
    /**
     *获取所有的schema文件的
     * @param iProject
     * @param filesUrl
     * @return
     */
    public static List getList(IProject iProject, Map<String, List<String>> filesUrl) {
        SchemaFiles ui = new SchemaFiles();
//        ProjectConfigXmlManager configXmlManager = new ProjectConfigXmlManager(iProject.getLocation().toOSString()
//                + File.separator + ".opsdevproject");
        ProjectConfigXmlManager configXmlManager = OpsManagerProjectTool.getConfigManager(iProject);
        File innerOpsFolder = new File(configXmlManager.getSchemaRootPath());
        ui.setName("InnerSchemaFiles");
        SchemaFiles userUi = new SchemaFiles();
        userUi.setName("UserSchemaFiles");
        for (Entry<String, List<String>> entry : filesUrl.entrySet()) {
            if (new File(entry.getKey()).getParentFile().equals(innerOpsFolder)) {

                SchemaFile infoSchemaFile = new SchemaFile();
                infoSchemaFile.setFullPath(entry.getKey());
                infoSchemaFile.setParent(ui);
                infoSchemaFile.setName(new File(entry.getKey()).getName());
                ui.getChild().add(infoSchemaFile);

                for (String str : entry.getValue()) {
                    RestFulApi api = new RestFulApi();
                    api.setParent(infoSchemaFile);
                    api.setName(str);
                    infoSchemaFile.getChild().add(api);
                }
            } else {
                SchemaFile infoSchemaFile1 = new SchemaFile();
                infoSchemaFile1.setParent(userUi);
                System.out.println(entry.getKey());
                ConsoleFactory.printToConsole(entry.getKey(),true);
                infoSchemaFile1.setName(new File(entry.getKey()).getName());
                userUi.getChild().add(infoSchemaFile1);
                infoSchemaFile1.setFullPath(entry.getKey());
                for (String str : entry.getValue()) {
                    RestFulApi api = new RestFulApi();
                    api.setParent(infoSchemaFile1);
                    api.setName(str);
                    infoSchemaFile1.getChild().add(api);
                }
            }
        }
        List unverList = new ArrayList();
        unverList.add(ui);
        unverList.add(userUi);
        return unverList;
    }

    public static List getFileList(IProject iProject, List<String> filesUrl) {
        SchemaFiles ui = new SchemaFiles();
        ProjectConfigXmlManager configXmlManager = new ProjectConfigXmlManager(iProject.getLocation().toOSString()
                + File.separator + ".opsdevproject");
        File innerOpsFolder = new File(configXmlManager.getSchemaRootPath());
        ui.setName("InnerSchemaFiles");
        SchemaFiles userUi = new SchemaFiles();
        userUi.setName("UserSchemaFiles");
        for (String str : filesUrl) {
            if (new File(str).getParentFile().equals(innerOpsFolder)) {

                SchemaFile infoSchemaFile = new SchemaFile();
                infoSchemaFile.setParent(ui);
                infoSchemaFile.setFullPath(str);
                infoSchemaFile.setName(new File(str).getName());
                ui.getChild().add(infoSchemaFile);

            } else {
                SchemaFile infoSchemaFile1 = new SchemaFile();
                infoSchemaFile1.setParent(userUi);
                infoSchemaFile1.setFullPath(str);
                infoSchemaFile1.setName(new File(str).getName());
                userUi.getChild().add(infoSchemaFile1);
            }
        }
        List unverList = new ArrayList();
        unverList.add(ui);
        unverList.add(userUi);
        return unverList;
    }

    public static List<DeviceNode> getDeviceList(List<OpsService> devices) {
        List<DeviceNode> deviceNodes = new ArrayList<DeviceNode>();
        for (OpsService opsService : devices) {
            DeviceNode deviceNode = new DeviceNode(opsService);

            for (OpsServiceUrlHandle handle : opsService.getHandles()) {
                OpsHandleNode handleNode = new OpsHandleNode(handle);
                handleNode.setParent(opsService);
                deviceNode.getChild().add(handleNode);
            }
            deviceNodes.add(deviceNode);
        }
        return deviceNodes;
    }

    public static void setJsonRootTreeItem(Tree tree, String response) {
        if (null == response || "".equals(response)) {
            return;
        }
 
        JSONObject jsonObject = JSONObject.fromObject(response);
        getJsonResponseNode(jsonObject, tree);
    }
    public static void setJsonRootTreeItemWithRelpace(Tree tree, String response) {
        if (null == response || "".equals(response)) {
            return;
        }
        response = response.replace("[", " {\"body\": [");
        response = response.replace("]", "]}");
        JSONObject jsonObject = JSONObject.fromObject(response);
        getJsonResponseNode(jsonObject, tree);
    }
    
    public static void getJsonResponseNode(JSONObject jsonObject, Object treeItem) {
        if (jsonObject != null) {
            for (Object object : jsonObject.keySet()) {
                TreeItem item = null;
                if (treeItem instanceof Tree) {
                    item = new TreeItem((Tree) treeItem, SWT.NULL);

                } else if (treeItem instanceof TreeItem) {
                    item = new TreeItem((TreeItem) treeItem, SWT.NULL);
                }
                
                try {
//                    jsonObject.getJSONArray(object.toString());
                    JSONArray array = JSONArray.fromObject(jsonObject.getJSONArray(object.toString()));
                    item.setText(new String[] { object.toString() });

                    for (int i = 0; i < array.size(); i++) {
                        getJsonResponseNode(array.getJSONObject(i), item);
                    }
                    return;
                } catch (Exception e) {

                }
               
                 item.setText(new String[] { object.toString(), jsonObject.get(object).toString() });
                
            }
        }
    }

    public static void setXmlRootTreeItem(TreeItem treeItem, String response) {
        if (null == response || "".equals(response)) {
            return;
        }

        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new ByteArrayInputStream(response.trim().getBytes()));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (document != null) {
            Element rootElement = document.getRootElement();

            treeItem.setText(new String[] { rootElement.getName(), rootElement.getText() });
            getXmlResponseNode(rootElement, treeItem);
        }

    }

    public static void getXmlResponseNode(Element element, TreeItem treeItem) {
        List<Element> elements = element.elements();
        if (elements != null && elements.size() != 0) {
            for (Element element2 : elements) {

                TreeItem node2 = new TreeItem(treeItem, SWT.NULL);

                node2.setText(new String[] { element2.getName(), element2.getText() });
                getXmlResponseNode(element2, node2);

            }
        }

    }

}