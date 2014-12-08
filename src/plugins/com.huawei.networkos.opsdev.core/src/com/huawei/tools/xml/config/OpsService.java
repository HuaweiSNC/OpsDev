package com.huawei.tools.xml.config;

import java.util.ArrayList;
import java.util.List;

public class OpsService {
    private String name;
    private String server;
    private String port;
    private String userName;
    private String devicename;
    private String passwd;
    private String deviceId;
    private String xmlVersion;
    private String productType;

    //device的http
    private String protocal = "http";
    
    public static String trustStore = "";
    public static String storePassword = "";


    private List<OpsServiceUrlHandle> handles; 
      
    public  String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }



    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
        result = prime * result + ((devicename == null) ? 0 : devicename.hashCode());
        result = prime * result + ((passwd == null) ? 0 : passwd.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        result = prime * result + ((productType == null) ? 0 : productType.hashCode());
        result = prime * result + ((server == null) ? 0 : server.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((xmlVersion == null) ? 0 : xmlVersion.hashCode());
        result = prime * result + ((protocal == null) ? 0 : protocal.hashCode());
        return result;
    }


    
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OpsService other = (OpsService) obj;
        if (deviceId == null) {
            if (other.deviceId != null)
                return false;
        } else if (!deviceId.equals(other.deviceId))
            return false;
        if (devicename == null) {
            if (other.devicename != null)
                return false;
        } else if (!devicename.equals(other.devicename))
            return false;
        if (passwd == null) {
            if (other.passwd != null)
                return false;
        } else if (!passwd.equals(other.passwd))
            return false;
        if (port == null) {
            if (other.port != null)
                return false;
        } else if (!port.equals(other.port))
            return false;
        if (productType == null) {
            if (other.productType != null)
                return false;
        } else if (!productType.equals(other.productType))
            return false;
        if (server == null) {
            if (other.server != null)
                return false;
        } else if (!server.equals(other.server))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (xmlVersion == null) {
            if (other.xmlVersion != null)
                return false;
        } else if (!xmlVersion.equals(other.xmlVersion))
            return false;
        if (protocal == null) {
            if (other.protocal != null)
                return false;
        } else if (!protocal.equals(other.protocal))
            return false;
        
        return true;
    }


    public OpsService() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
    public List<OpsServiceUrlHandle> getHandles() {
        if(handles!=null)
        return handles;
        return new ArrayList<OpsServiceUrlHandle>();
    }



    public void setHandles(List<OpsServiceUrlHandle> handles) {
        this.handles = handles;
    }

    

    
    public OpsService(String name,String protocal, String server, String port, String userName, String devicename, String passwd,
            String deviceId, String xmlVersion, String productType,List<OpsServiceUrlHandle> handles,String trustStore,String storePassword) {
        super();
        this.name = name;
        this.protocal = protocal;
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.devicename = devicename;
        this.passwd = passwd;
        this.deviceId = deviceId;
        this.xmlVersion = xmlVersion;
        this.productType = productType;
        this.handles = handles;
        this.trustStore = trustStore;
        this.storePassword = storePassword;
    }



//    public OpsService(String server, String port, String userName, String devicename, String passwd, String deviceId,
//            String xmlVersion, String productType, List<OpsServiceUrlHandle> handles) {
//        super();
//        this.server = server;
//        this.port = port;
//        this.userName = userName;
//        this.devicename = devicename;
//        this.passwd = passwd;
//        this.deviceId = deviceId;
//        this.xmlVersion = xmlVersion;
//        this.productType = productType;
//        this.handles = handles;
//    }





    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getXmlVersion() {
        return xmlVersion;
    }

    public void setXmlVersion(String xmlVersion) {
        this.xmlVersion = xmlVersion;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }
    
    public static String getTrustStore() {
        return trustStore;
    }

    public static void setTrustStore(String trustStore) {
        OpsService.trustStore = trustStore;
    }

    public static String getStorePassword() {
        return storePassword;
    }

    public static void setStorePassword(String storePassword) {
        OpsService.storePassword = storePassword;
    }
    

    public String toString() {
        return "OpsService [protocal=" + protocal + ",server=" + server + ", port=" + port + ", userName=" + userName + ", devicename="
                + devicename + ", passwd=" + passwd + ", deviceId=" + deviceId + ", xmlVersion=" + xmlVersion
                + ", productType=" + productType + ", handles=" + handles + "]";
    }
    
    
    
    
    
    
//    'server':'10.138.90.116',
//    'port':'8080',
//    'username':'root',
//    'devicename':'devicename',
//    'passwd':'root123',
//    'id':'1',
//    'version':'2.0',
//    'productType':'NE5000E'
}
