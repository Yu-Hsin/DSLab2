import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import rmimessage.RMIMessage;


public class Hello_stub implements HelloInterface {

	RemoteObjectReference ref = null;
	
	public Hello_stub() {}
	public Hello_stub(RemoteObjectReference r) {
		ref = r;
	}
	
	@Override
	public String sayHello(String name) throws Remote440Exception {
	
		RMIMessage msg = new RMIMessage(ref.getID(), "sayHello", new Object[]{name});
		
		String ans = this.invoke(msg);
		return ans;
	}
	
	private String invoke(RMIMessage msg) {
		
		try {
			Socket s = new Socket(ref.getIP(), ref.getPort());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			
			out.writeObject(msg);
			out.flush();
			
			//wait for response from other nodes
	    	ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	    	Object response = in.readObject();
			
	    	if (response instanceof String) return (String)response;
	    	
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
