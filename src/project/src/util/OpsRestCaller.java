package util;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

/**
 * HUAWEI restful api 
 * 
 * @author OPSDEV
 * 
 */
public class OpsRestCaller {

	private OpsDevice device = null;
	public static final String ACTION_GET = "GET";
	public static final String ACTION_POST = "POST";
	public static final String ACTION_PUT = "PUT";
	public static final String ACTION_DELETE = "DELETE";

	public OpsRestCaller(OpsDevice ops) {
		this.device = ops;
	}

	/**
	 * get,post,put,delete 
	 * @param url???data
	 * 
	 */
	public RetRpc get(String url) {
		return restcall(url, null, ACTION_GET);
	}

	public RetRpc post(String url, String data) {
		return restcall(url, data, ACTION_POST);
	}

	public RetRpc put(String url, String data) {
		return restcall(url, data, ACTION_PUT);
	}

	public RetRpc delete(String url) {
		return restcall(url, null, ACTION_DELETE);
	}

	public RetRpc restcall(String path, String data, String action) {
		if (null == this.device) {
			return null;
		}
		/**
		 * get url???ip???port
		 * 
		 */
		String urlpath = "/devices/" + this.device.getId() + path;
		String tmpserverip = this.device.getServer();
		int tmpserverport = this.device.getPort();
//============monitor code start=======================================
//============notes: enable monitor, if publish , please move it=======
        urlpath = urlpath + "/json/_repoint_?server=" + this.device.getServer() + "&port=" + this.device.getPort();
        tmpserverip = "127.0.0.1";
        tmpserverport =Integer.parseInt("8099");
//=============monitor code end========================================

		return connect(tmpserverip, tmpserverport, urlpath, action, data);
	}

	/**
	 * link http
	 * 
	 * @param server
	 * @param port
	 * @param url
	 * @param method
	 * @param data
	 * @return
	 */

	public RetRpc connect(String server, int port, String url, String method,
			String data) {

		RetRpc ret = new RetRpc();
		String address;
		if (!url.startsWith("http")) {
			address = "http://" + server + ":" + port + url;
		} else {
			address = url;
		}

	 
		// create the rest client instance
		RestClient client = new RestClient();
		// create the resource instance to interact with
		Resource resource = client.resource(address);
		// issue the request
		ClientResponse response = null;

		try {
			if (ACTION_POST.equals(method)) {
				response = resource.accept("text/plain").post(data);
			} else if (ACTION_PUT.equals(method)) {
				response = resource.accept("text/plain").put(data);

			} else if (ACTION_DELETE.equals(method)) {
				response = resource.accept("text/plain").delete();

			} else {
				response = resource.accept("text/plain").get();
			}
		} catch (Exception ex) {
			
			ex.printStackTrace();
			ret.setStatusCode(500);
			ret.setMsg(ex.getMessage());
			ret.setStatus("connected failed .");
			
			return ret;
		}  
		
		int code = response.getStatusCode();
		String msg = response.getMessage();
		ret.setStatusCode(code);
		ret.setStatus(response.getStatusType().getReasonPhrase());
		ret.setMsg(msg);

		// deserialize response
		ret.setContent(response.getEntity(String.class));

		return ret;
	}

}
