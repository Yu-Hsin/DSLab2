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
	public void initialise(ZipCodeList newlist) {
		RMIMessage msg = new RMIMessage(ref.getClassName(), "initialise", new Object[]{newlist});
		
		this.invoke(msg);
	}

	@Override
	public String find(String city) {
		RMIMessage msg = new RMIMessage(ref.getClassName(), "find", new Object[]{city});
		
		String ans = (String) this.invoke(msg);
		return ans;
	}

	@Override
	public ZipCodeList findAll() {
		RMIMessage msg = new RMIMessage(ref.getClassName(), "findAll", null);
		
		ZipCodeList ans = (ZipCodeList) this.invoke(msg);
		return ans;
	}

	@Override
	public void printAll() {
		RMIMessage msg = new RMIMessage(ref.getClassName(), "printAll", null);
		
		this.invoke(msg);
	}

	public Object invoke(RMIMessage msg) {
		System.out.println("ZipCodeStub:  start invoke  " + msg.getMethodName() + "  from " + msg.getObjectName());
		
		try {
			Socket s = new Socket(ref.getIP(), ref.getPort());
			
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			
			out.writeObject(msg);
			out.flush();
			
			//wait for response from other nodes
	    	ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	    	Object response = in.readObject();
			
	    	if (response instanceof RMIMessage) return ((RMIMessage)response).getReturnVal();
	    	
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