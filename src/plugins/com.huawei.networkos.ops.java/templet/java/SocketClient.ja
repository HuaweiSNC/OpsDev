package trap;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;



public class SocketClient extends WebSocketClient {
	
	private static SocketClient  clent = null;
	private List<IHuaweiTrapListener> listeners =  new ArrayList<IHuaweiTrapListener>();
	
	public void addListener(IHuaweiTrapListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeListener(IHuaweiTrapListener listener)
	{
		listeners.remove(listener);
	}
	
	public synchronized  static SocketClient  getInstance(URI serverURI) {
		if (clent == null) {  
			clent = new SocketClient (serverURI);
			clent.connect();
		}  
		return clent;
	}

	public SocketClient (URI serverURI) {
		super(serverURI);

	}

	
	public void onClose(int arg0, String arg1, boolean arg2) {
		// TODO Auto-generated method stub
		
	}


	public void onError(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 获取告警信息
	 */
	public void onMessage(String arg0) {
		String message = this.currentAlarmMessage(arg0);
		for (IHuaweiTrapListener lis : listeners)
		{
			lis.receive(message);
		}
	}
/**
 * 解析告警信息
 * @param str
 * @return
 */
	public String currentAlarmMessage(String str){
		String message = "";
		JSONObject object = JSONObject.fromObject(str);
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xml = xmlSerializer.write(JSONSerializer.toJSON(object));
		String xml2 =xml.replace("<o>","").replace("</o>","").replace("<e class=\"object\">","").replace("</e>",""); 
		Alarm alarm = new Alarm(xml2);
		message = alarm.getOpsMessage3();
		return message;
	}
	


	public void onOpen(ServerHandshake arg0) {
		System.out.println("open");
		
	}

	
}
