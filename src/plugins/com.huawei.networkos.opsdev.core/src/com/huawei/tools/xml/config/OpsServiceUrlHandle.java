package com.huawei.tools.xml.config;


import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;

import com.huawei.network.opsdev.core.templet.ProjectTempletManager;
import com.huawei.network.opsdev.core.templet.UrlBean;

public class OpsServiceUrlHandle {
    private String apiUrl;
    private Map<String,String> propertiesHandles = new HashMap<String, String>();
    private Boolean isInnerSchemmer;
    private String handle;
    private OpsService parent;
    private String name ; 
    private String contentType;
    private UrlBean bean;
    private UrlBean chosedBean;
    private String fulApiUrl;
    
    
    
    
    public String getFulApiUrl(ProjectTempletManager manager) {
        String json = null;
        if(isInnerSchemmer){
            json = manager.getApiManager().getRestApiJsonByUrl(apiUrl);
        }else{
            json = manager.getUserApiManager().getRestApiJsonByUrl(apiUrl);
        }
        if(json!=null){

            JSONObject obj = JSONArray.fromObject(json).getJSONObject(0);
            return (String)obj.get("curl");
        }  
        
        return null;
    }



    public void setFulApiUrl(String fulApiUrl) {
        this.fulApiUrl = fulApiUrl;
    }



    public UrlBean getChosedBean() {
        return chosedBean;
    }



    public void setChosedBean(UrlBean chosedBean) {
        this.chosedBean = chosedBean;
    }



    public Boolean getIsInnerSchemmer() {
        return isInnerSchemmer;
    }


    public void setIsInnerSchemmer(Boolean isInnerSchemmer) {
        this.isInnerSchemmer = isInnerSchemmer;
    }


    public UrlBean getBean() {
        return bean;
    }


    public void setBean(UrlBean bean) {
        this.bean = bean;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getContentType() {
        return contentType;
    }


    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public OpsServiceUrlHandle(String apiUrl, Map<String, String> propertiesHandles, String handle) {
        super();
        this.apiUrl = apiUrl;
        this.propertiesHandles = propertiesHandles;
        this.handle = handle;
    }
    

    public OpsServiceUrlHandle(String apiUrl, Map<String, String> propertiesHandles, String handle, 
            String name, String contentType,Boolean isinner) {
        super();
        this.apiUrl = apiUrl;
        this.propertiesHandles = propertiesHandles;
        this.handle = handle;
        this.name = name;
        this.contentType = contentType;
        this.isInnerSchemmer = isinner;
    }


    public OpsServiceUrlHandle() {
        // TODO Auto-generated constructor stub
    }


    public String getApiUrl() {
        return apiUrl;
    }
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
    public Map<String, String> getPropertiesHandles() {
        return propertiesHandles;
    }
    public void setPropertiesHandles(Map<String, String> propertiesHandles) {
        this.propertiesHandles = propertiesHandles;
    }
    public String getHandle() {
        return handle;
    }
    public void setHandle(String handle) {
        this.handle = handle;
    }
    public OpsService getParent() {
        return parent;
    }
    public void setParent(OpsService parent) {
        this.parent = parent;
        parent.getHandles().add(this);
    }


    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apiUrl == null) ? 0 : apiUrl.hashCode());
        result = prime * result + ((handle == null) ? 0 : handle.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((propertiesHandles == null) ? 0 : propertiesHandles.hashCode());
        return result;
    }


    
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OpsServiceUrlHandle other = (OpsServiceUrlHandle) obj;
        if (apiUrl == null) {
            if (other.apiUrl != null)
                return false;
        } else if (!apiUrl.equals(other.apiUrl))
            return false;
        if (handle == null) {
            if (other.handle != null)
                return false;
        } else if (!handle.equals(other.handle))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (propertiesHandles == null) {
            if (other.propertiesHandles != null)
                return false;
        } else if (!propertiesHandles.equals(other.propertiesHandles))
            return false;
        return true;
    }


    
    
    
    
    
    
}
