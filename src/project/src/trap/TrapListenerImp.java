package trap;
import java.net.URI;



public class TrapListenerImp implements IHuaweiTrapListener {

	public boolean receive(String msg) {
		// TODO Auto-generated method stub
		return false;
	}

	 
	/**
	 * 创建客户端连接
	 * @param args
	 */
		public static void main(String[] args){
			String uri ="http://localhost:9000/channels/2/data";
			URI uri2  = URI.create(uri);
			SocketClient socketTest = new SocketClient(uri2);
			
			TrapListenerImp imp = new TrapListenerImp();
			socketTest.addListener(imp);
			socketTest.connect();
			
		}

}
