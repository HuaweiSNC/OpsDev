package util;

 
/***
 * ops service
 * @author OPSDEV
 *
 */
public class OpsServiceManager {
    
    private  static OpsServiceManager serviceManager;
    private OpsServiceCfgXmlutil OpsServiceConfigXmlutil = null  ;
    private OpsDevice  defaultconfig = null;
    public static String servicecfgxmlpath = "src\\util\\OpsServiceConfig.xml";
    
    public synchronized static OpsServiceManager getInstance(){
    
        if(null == serviceManager){
            return new OpsServiceManager(servicecfgxmlpath); 
        }
        return serviceManager;
    }
    
    private OpsServiceManager(String servicecfgxmlpath) {
        
        OpsServiceCfgXmlutil servicecfgxml = new OpsServiceCfgXmlutil(servicecfgxmlpath);
        this.OpsServiceConfigXmlutil = servicecfgxml;
        servicecfgxml.parseDevicesmap();
    }

    /**
     * get default device
     * @return
     */
    public OpsDevice getDefaultOpsConfig(){
   
        defaultconfig = OpsServiceConfigXmlutil.getDeivceByName("default");
        return defaultconfig;
    }

    /**
     * get default restcall 
     * @return
     */
    public OpsRestCaller getDefaultOpsRestCall(){
        return new OpsRestCaller(getDefaultOpsConfig());
    }
        
    /**
     * get restcall by device 
     * @return
     */
    public OpsRestCaller getOpsRestCallByName(String name){
        return new OpsRestCaller(OpsServiceConfigXmlutil.getDeivceByName(name));
    }

}
