import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ZipCodeRList_stub implements ZipCodeRList, Remote440Stub {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RemoteObjectReference ref;

	@Override
	public void setReference(RemoteObjectReference r) {
		// TODO Auto-generated method stub
		ref = r;
	}

	@Override
	public String find(String city) {
		RMIMessage msg = new RMIMessage(ref.getInterfaceName(), "find",
				new Object[] { city });

		String ans = (String) this.invoke(msg);
		return ans;
	}

	@Override
	public ZipCodeRList add(String city, String zipcode) {
		RMIMessage msg = new RMIMessage(ref.getInterfaceName(), "add",
				new Object[] {city, zipcode});

		ZipCodeRList ans = (ZipCodeRList) this.invoke(msg);
		return ans;
	}

	@Override
	public ZipCodeRList next() {
		RMIMessage msg = new RMIMessage(ref.getInterfaceName(), "next", null);

		ZipCodeRList ans = (ZipCodeRList) this.invoke(msg);
		return ans;

	}

	private Object invoke(RMIMessage msg) {
		System.out.println("ZipCodeRListStub:  start invoke  " + msg.getMethodName()
				+ "  from " + msg.getObjectName());

		try {
			Socket s = new Socket(ref.getIP(), ref.getPort());

			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

			out.writeObject(msg);
			out.flush();

			// wait for response from other nodes
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Object response = in.readObject();

			if (response instanceof RMIMessage)
				return ((RMIMessage) response).getReturnVal();

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
