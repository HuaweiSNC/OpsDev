package service;

import java.io.File;

import util.OpsDevice;
import util.OpsRestCaller;
import util.OpsServiceCfgXmlutil;

public class HandleInfoService {
    
    private static HandleInfoService HandleInfoServiceInstance;
    private OpsServiceCfgXmlutil OpsServiceConfigXmlutil;
    OpsDevice  defaultconfig;
    
    //parse OpsServiceConfig.xml
    public static HandleInfoService getHandleInfoServiceInstance(){
        if(null == HandleInfoServiceInstance){
            String path = System.getProperty("user.dir")+File.separator+"src"+File.separator+"util"+File.separator;
            String servicecfgxmlpath =path+ "OpsServiceConfig.xml";
            return new HandleInfoService(servicecfgxmlpath);
        }
        return HandleInfoServiceInstance;
    }
    
 
    public HandleInfoService(String servicecfgxmlpath){
        super();
        OpsServiceCfgXmlutil servicecfgxml = new OpsServiceCfgXmlutil(servicecfgxmlpath);
        this.OpsServiceConfigXmlutil = servicecfgxml;
    }
    
    public Object __ifmdefaultHandle__(){
        String url = "/ifm/interfaces/interface/json";
        defaultconfig = OpsServiceConfigXmlutil.getDeivceByName("default");
        OpsRestCaller restCaller = new OpsRestCaller(defaultconfig);
        return restCaller.get(url);
    }

}
