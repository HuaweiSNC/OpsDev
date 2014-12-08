package com.huawei.network.opsdev.core.templet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.Workbench;

import com.huawei.network.opsdev.core.utils.ConsoleFactory;
import com.huawei.network.opsdev.core.utils.OpsManagerProjectTool;
import com.huawei.network.opsdev.core.utils.ProjectPathManager;
import com.huawei.network.opsdev.wizard.LoadFileDialog;
import com.huawei.tools.xml.config.OpsService;
import com.huawei.tools.xml.config.OpsServiceUrlHandle;

public class TempletAutoPythonNode {
    public OpsService opsService;
    public String fullUrl = "";
    public OpsServiceUrlHandle handle;
    public String deviceConfigName = "";
    public String pythonMethod = "";
    public IProject iProject;
    public String handleName = "";
    public String attributesAndKeys = "";
    public List<String> keys = new ArrayList<String>();
    public List<String> attributes = new ArrayList<String>();
    public Map<String, String> attributesAndKeysExampleMap = new HashMap<String, String>();

    public TempletAutoPythonNode(OpsService opsService, OpsServiceUrlHandle handle, IProject iProject) {
        this.opsService = opsService;
        this.handle = handle;
        this.iProject = iProject;
        setUrl();
        this.deviceConfigName = opsService.getName();
        this.handleName = handle.getName();

        System.out.println(iProject);
        ConsoleFactory.printToConsole(iProject.toString(),true);
        setAttributesAndKeys();
        setPythonMethod();
    }

    public OpsService getOpsService() {
        return opsService;
    }

    public void setOpsService(OpsService opsService) {
        this.opsService = opsService;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public OpsServiceUrlHandle getHandle() {
        return handle;
    }

    public void setHandle(OpsServiceUrlHandle handle) {
        this.handle = handle;
    }

    public String getDeviceConfigName() {
        return deviceConfigName;
    }

    public void setDeviceConfigName(String deviceConfigName) {
        this.deviceConfigName = deviceConfigName;
    }

    public String getPythonMethod() {
        return pythonMethod;
    }

    public void setPythonMethod(String pythonMethod) {
        this.pythonMethod = pythonMethod;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getAttributesAndKeys() {
        return attributesAndKeys;
    }

    public void setAttributesAndKeys(String attributesAndKeys) {
        this.attributesAndKeys = attributesAndKeys;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    private void setPythonMethod() {
        StringBuffer buffer = new StringBuffer("restcaller.");
        if ("GET".equals(handle.getHandle()) || "GETALL".equals(handle.getHandle())) {
            buffer.append("get(url)");
        }
        else if ("DELETE".equals(handle.getHandle())) {
            buffer.append("delete(url)");
        }
        else if ("MODIFY".equals(handle.getHandle())) {
            buffer.append("put(url, content)");
        }
        else if ("CREATE".equals(handle.getHandle())) {
            buffer.append("post(url, content");
        }
        this.pythonMethod = buffer.toString();
    }

    private void setAttributesAndKeys() {

        ProjectTempletManager manager = (ProjectTempletManager) OpsManagerProjectTool.getTempletManager(iProject);
        LoadFileDialog dialog = new LoadFileDialog(Display.getDefault().getActiveShell(), manager,
                ApiFileShower.getFileListFromFilePath(
                        ProjectPathManager.getSchemaFolderPath(iProject), iProject), iProject);
        dialog.open();
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.SCHEMAFILE_CONFIG, manager);

        if (handle.getIsInnerSchemmer()) {
            handle.setChosedBean(new UrlBean(manager.getApiManager().getRestApiJsonByUrl(handle.getApiUrl()), handle
                    .getPropertiesHandles()));
        }
        StringBuffer attributesAndKeys = new StringBuffer();
        if (handle.getChosedBean() != null) {
            for (UrlPropertiesBean propertiesBean : handle.getChosedBean()
                    .getAll()) {
                attributesAndKeys.append(propertiesBean.getName() + ",");
                attributesAndKeysExampleMap.put(propertiesBean.getName(), propertiesBean.getExample());
            }
            if (attributesAndKeys.length() != 0) {
                this.attributesAndKeys = attributesAndKeys.deleteCharAt(attributesAndKeys.length() - 1).toString();
            }

            for (UrlPropertiesBean propertiesBean : handle.getChosedBean()
                    .getKeys()) {
                keys.add(propertiesBean.getName());
            }
            for (UrlPropertiesBean propertiesBean : handle.getChosedBean().getBeans()) {
                attributes.add(propertiesBean.getName());
            }
        }
    }

    private void setUrl() {
        ProjectTempletManager manager = (ProjectTempletManager) OpsManagerProjectTool.getTempletManager(iProject);

        LoadFileDialog dialog = new LoadFileDialog(Workbench.getInstance()
                .getDisplay().getActiveShell(), manager,
                ApiFileShower.getFileListFromFilePath(
                        ProjectPathManager.getSchemaFolderPath(iProject),
                        iProject), iProject);
        dialog.open();
        OpsManagerProjectTool.saveManagerInToProject(iProject, OpsManagerProjectTool.SCHEMAFILE_CONFIG,
                manager);
        //        StringBuffer url = new StringBuffer();

        String fullUrl = handle.getFulApiUrl(manager);
        //        for (UrlPropertiesBean propertiesBean : handle.getChosedBean()
        //                .getKeys()) {
        //            fullUrl = fullUrl.replaceFirst(propertiesBean.getName() + "=%s",
        //                    propertiesBean.getName() + "=" + propertiesBean.getValue());
        //            propertiesBean.getName();
        //        }
        //
        //        url.append(fullUrl);
        //        for (UrlPropertiesBean propertiesBean : handle.getChosedBean()
        //                .getBeans()) {
        //            url.append(propertiesBean.getName() + "="
        //                    + propertiesBean.getValue().replaceAll("/", "%2F") + "&");
        //
        //        }
        //        if(url.toString().trim().endsWith("&")){
        //            fullUrl =  url.substring(0, url.length() - 1);
        //        }else{
        //            fullUrl = url.toString();
        //        }
        this.fullUrl = fullUrl + "/" + handle.getContentType();

    }

    @Override
    public String toString() {
        return "TempletAutoPythonNode [opsService=" + opsService + ", fullUrl=" + fullUrl + ", handle=" + handle
                + ", deviceConfigName=" + deviceConfigName + ", pythonMethod=" + pythonMethod + ", iProject="
                + iProject + ", handleName=" + handleName + ", attributesAndKeys=" + attributesAndKeys + ", keys="
                + keys + ", attributes=" + attributes + "]";
    }

}
