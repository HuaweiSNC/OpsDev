package com.huawei.network.opsdev.core.inspectview.bean;

import java.util.ArrayList;
import java.util.List;

public  class  HttpServiceRequestBeanManager  {
    
    public static List<HttpServiceRequestBean> httpServiceRequestBeans = new ArrayList<HttpServiceRequestBean>();
    public static Object mutex = new Object();
    
    public static HttpServiceRequestBean createCurrentBean(){
        
        HttpServiceRequestBean currentBean = new HttpServiceRequestBean();
        return currentBean;
    }
     
    public static void addRequestBean(HttpServiceRequestBean bean)
    {
        synchronized(mutex)
        {
            httpServiceRequestBeans.add(bean); 
        }
    }
    public static HttpServiceRequestBean getRequestBeanByOne(){
        
        HttpServiceRequestBean currentBean =  null;
        synchronized(mutex)
        {
            if (httpServiceRequestBeans.size() > 0)
            {
                currentBean = httpServiceRequestBeans.remove(0);
            }
        }
        return currentBean;
    }
    
    public static void clear(){
        
        synchronized(mutex)
        {
          httpServiceRequestBeans.clear();
        }
    }
    
    
}
