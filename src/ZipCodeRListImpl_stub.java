import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ZipCodeRListImpl_stub implements ZipCodeRList, Remote440Stub {

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
	public String find(String city) throws Remote440Exception {

		RMIMessage msg = new RMIMessage(ref.getObjName(), "find",
				new Object[] { city });

		String ans = (String) this.invoke(msg);
		return ans;
	}

	@Override
	public ZipCodeRList add(String city, String zipcode) throws Remote440Exception {

		RMIMessage msg = new RMIMessage(ref.getObjName(), "add",
				new Object[] {city, zipcode});

		ZipCodeRList ans = (ZipCodeRList) this.invoke(msg);
		return ans;
	}

	@Override
	public ZipCodeRList next() throws Remote440Exception {

		RMIMessage msg = new RMIMessage(ref.getObjName(), "next", null);
		ZipCodeRList ans = (ZipCodeRList) this.invoke(msg);
		return ans;

	}

	private Object invoke(RMIMessage msg) throws Remote440Exception {
	
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

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new Remote440Exception("Failed to invoke function!!");
		}

		return null;
	}



}
