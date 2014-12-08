package util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Device manager
 * @author sWX203247
 * @change zwx199308
 */
public class OpsDeviceManager {

    private OpsDevice device = null;
    public static final String ACTION_GET = "GET";
    public static final String ACTION_ADD = "POST";
    public static final String ACTION_MODIFY = "PUT";
    public static final String ACTION_DELETE = "DELETE";

    public OpsDeviceManager(OpsDevice device) {
        this.device = device;
    }
    //add a device
    public String[] addDevice(String data) {
        return restcall("/devices", data, ACTION_ADD);
    }
    //delete a device
    public String[] delDevice(int deviceId) {
        String url = "/devices/" + deviceId;
        return restcall(url, null, ACTION_DELETE);
    }
    //modify a device
    public String[] modifyDevice(String data) {
        return restcall("/devices", data, ACTION_MODIFY);
    }
    //get a device by id
    public String[] getDevice(int deviceId) {
        String url = "/devices/" + deviceId;
        return restcall(url, null, ACTION_GET);
    }
    //get all devices
    public String[] getDevices() {
        return restcall("/devices", null, ACTION_GET);
    }
    //handle the client request
    public String[] restcall(String path, String data, String action) {
        if (null == this.device) {
            return null;
        }
        String tmpserverip = this.device.getServer();
        int tmpserverport = this.device.getPort();
        return connect(tmpserverip, tmpserverport, path, action, data);
    }
    
    //get a http connection
    public String[] connect(String server, int port, String url, String method, String data) {
    	
        BufferedReader br = null;
        String[] resultList = new String[3];
        HttpURLConnection con = null;
        String address; 
        if (!url.startsWith("http"))
        { 
            address = "http://" + server + ":" + port + url;
        }
        else
        {
            address = url;
        }
        try {
            URL conURL = new URL(address);
            con = (HttpURLConnection) conURL.openConnection();
            con.setRequestMethod(method);
            con.setReadTimeout(10000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/xml");
            con.connect();
            //get client request
            if (!"GET".equals(method)&&!"DELETE".equals(method))
            {
                data = data + "\n";
                con.getOutputStream().write(data.toString().getBytes());
                con.getOutputStream().flush();
                con.getOutputStream().close();
            }
            //get status code
            int code = con.getResponseCode();
            //get response message
            String msg = con.getResponseMessage();
            resultList[0] = String.valueOf(code);
            resultList[1] = msg;
            
     
            if (code == 200)
            {
                InputStream urlStream = con.getInputStream();
                BufferedInputStream buff = new BufferedInputStream(urlStream);
                Reader r = new InputStreamReader(buff, "utf-8");
                br = new BufferedReader(r);
                String strLine = null;
                StringBuffer strHtml = new StringBuffer("");
                while ((strLine = br.readLine()) != null)
                {
                    strHtml.append(strLine + "\r\n");
                }

                if (strHtml.toString().length() > 0)
                {
                    resultList[2] = strHtml.toString();
                }
            } else if (code == 404)
            {
                resultList[2] = "NOT FOUND";
            } else if (code == 500)
            {
                resultList[2] = "SERVDR ERROR";
            }

        } catch (MalformedURLException e) {
            resultList[0] = String.valueOf(1000);
            resultList[1] = e.getMessage();
            resultList[2] = "MalformedURLException";
        } catch (IOException e) {
            resultList[0] = String.valueOf(1000);
            resultList[1] = e.getMessage();
            resultList[2] = "Connection refused";
        } finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (con != null)
            {
                con.disconnect();
            }
        }
        return resultList;
    }
    
    
//    public static void main(String[] args) {
//    	
//
//    	OpsDevice device = new OpsDevice("123", "devices", "127.0.0.1", 8080, "123456", "root", "8", "2.0",
//                "NE5000E");
//        OpsDeviceManager opsDeviceManager = new OpsDeviceManager(device);
//        opsDeviceManager.addDevice("{\"device\":{\"username\": \"isoftstone\",\"devicename\": \"10.136.13.124\",\"passwd\": \"huaweiabc\",\"ip\": \"10.136.13.124\",\"productType\": \"NE5000E\",\"version\": \"2.0\",\"id\" : 7}}");
//        opsDeviceManager.modifyDevice("{\"device\":{\"username\": \"huawei\",\"devicename\": \"10.136.13.137\",\"passwd\": \"huaweiabc\",\"ip\": \"10.136.13.137\",\"productType\": \"NE5000E\",\"version\": \"2.0\",\"id\" : 6}}");
//        opsDeviceManager.delDevice(5);
//        String [] opsManager = opsDeviceManager.getDevice(5);
//        String [] opsManager = opsDeviceManager.getDevices();
//        System.out.println(opsManager[2]);
//    }
    
    
}
