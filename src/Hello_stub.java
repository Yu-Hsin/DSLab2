import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Hello_stub implements HelloInterface, Remote440Stub {

	RemoteObjectReference ref;
	
	public Hello_stub() {}
	
	public void setReference(RemoteObjectReference r) {
		ref = r;
	}
	
	@Override
	public String sayHello(String name) throws Remote440Exception {
		
		
		
		RMIMessage msg = new RMIMessage(ref.getInterfaceName(), "sayHello", new Object[]{name});
		
		String ans = this.invoke(msg);
		return ans;
	}
	
	private String invoke(RMIMessage msg) {
		
		System.out.println("HELLO_STUB:  start invoke");
		
		try {
			Socket s = new Socket(ref.getIP(), ref.getPort());
			
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			
			out.writeObject(msg);
			out.flush();
			
			//wait for response from other nodes
	    	ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	    	Object response = in.readObject();
			
	    	if (response instanceof RMIMessage) return (String) ((RMIMessage)response).getReturnVal();
	    	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
