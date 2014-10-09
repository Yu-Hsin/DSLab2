import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ZipCodeServerImpl_stub implements ZipCodeServer, Remote440Stub{
	RemoteObjectReference ref;
	
	@Override
	public void setReference(RemoteObjectReference r) {
		ref = r;
	}

	@Override
	public void initialise(ZipCodeList newlist) throws Remote440Exception {
		RMIMessage msg = new RMIMessage(ref.getObjName(), "initialise", new Object[]{newlist});
		
		this.invoke(msg);
	}

	@Override
	public String find(String city)  throws Remote440Exception {
		RMIMessage msg = new RMIMessage(ref.getObjName(), "find", new Object[]{city});
		
		String ans = (String) this.invoke(msg);
		return ans;
	}

	@Override
	public ZipCodeList findAll() throws Remote440Exception {
		RMIMessage msg = new RMIMessage(ref.getObjName(), "findAll", null);
		
		ZipCodeList ans = (ZipCodeList) this.invoke(msg);
		return ans;
	}

	@Override
	public void printAll() throws Remote440Exception {
		RMIMessage msg = new RMIMessage(ref.getObjName(), "printAll", null);
		
		this.invoke(msg);
	}

	public Object invoke(RMIMessage msg) throws Remote440Exception {
		
		
		try {
			Socket s = new Socket(ref.getIP(), ref.getPort());
			
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			
			out.writeObject(msg);
			out.flush();
			
			//wait for response from other nodes
	    	ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	    	Object response = in.readObject();
			
	    	if (response instanceof RMIMessage) return ((RMIMessage)response).getReturnVal();
	    	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new Remote440Exception("Failed to invoke function!!");
		}
		
		return null;
	}
	
}
