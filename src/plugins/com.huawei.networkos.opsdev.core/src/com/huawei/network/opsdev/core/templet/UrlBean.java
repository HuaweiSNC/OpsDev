package com.huawei.network.opsdev.core.templet;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UrlBean {
    private List<UrlPropertiesBean> beans = new ArrayList<UrlPropertiesBean>() ;
    private List<UrlPropertiesBean> keys = new ArrayList<UrlPropertiesBean>();
    public UrlBean(String json) {
        JSONObject obj = JSONArray.fromObject(json).getJSONObject(0);
        JSONObject attributes =  (JSONObject)obj.get("attributes");
        for(Object entry:attributes.entrySet()){
            UrlPropertiesBean bean  = new UrlPropertiesBean();
            bean.setName(((Entry)entry).getKey().toString());
            bean.setValicateText(attributes.getJSONObject(((Entry)entry).getKey().toString()));
            beans.add(bean);
        }
        JSONArray keysJson =  ((JSONArray)obj.get("keys"));
        for(Object object : keysJson){
            JSONObject jsonObject  = (JSONObject)object;
            for(Object entry:jsonObject.entrySet()){
                UrlPropertiesBean bean  = new UrlPropertiesBean();
                bean.setName(((Entry)entry).getKey().toString());
                bean.setValicateText(jsonObject.getJSONObject(((Entry)entry).getKey().toString()));
                keys.add(bean);
            }
            
        }
    }
    
    public UrlBean(String json,Map<String,String> chosdeBean){

        JSONObject obj = JSONArray.fromObject(json).getJSONObject(0);
        JSONObject attributes =  (JSONObject)obj.get("attributes");
        for(Object entry:attributes.entrySet()){
            if(chosdeBean.containsKey(((Entry)entry).getKey().toString())){
                UrlPropertiesBean bean  = new UrlPropertiesBean();
                bean.setName(((Entry)entry).getKey().toString());
                bean.setValicateText(attributes.getJSONObject(((Entry)entry).getKey().toString()));
                beans.add(bean);
            }
        }
        JSONArray keysJson =  ((JSONArray)obj.get("keys"));
        for(Object object : keysJson){
            JSONObject jsonObject  = (JSONObject)object;
            for(Object entry:jsonObject.entrySet()){
                if(chosdeBean.containsKey(((Entry)entry).getKey().toString())){
                    UrlPropertiesBean bean  = new UrlPropertiesBean();
                    bean.setName(((Entry)entry).getKey().toString());
                    bean.setValicateText(jsonObject.getJSONObject(((Entry)entry).getKey().toString()));
                    keys.add(bean);
                }
            }
            
        } 
    }
    
    public List<UrlPropertiesBean> getBeans() {
        return beans;
    }
    public void setBeans(List<UrlPropertiesBean> beans) {
        this.beans = beans;
    }
    
    public List<UrlPropertiesBean> getAll(){
        List<UrlPropertiesBean> beans = new ArrayList<UrlPropertiesBean>();
        beans.addAll(this.beans);
        beans.addAll(keys);
        
        return beans;
    }
    public List<UrlPropertiesBean> getKeys() {
        return keys;
    }
    public void setKeys(List<UrlPropertiesBean> keys) {
        this.keys = keys;
    }
    
    
    
}
