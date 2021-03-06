import java.io.Serializable;

/*
 * Remote Object Reference of the remote object.
 * The information stored here includes:
 * 		1. ip, port to remote host
 * 		2. class name, and object name of the target remote object
 */
public class RemoteObjectReference implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ipAddr;
	private int port;
	private String className;
	private String objName;
	
	public RemoteObjectReference(String ip, int p, String cName, String oName) {
		ipAddr = ip;
		port = p;
		objName = oName;
		className = cName;
	}
	
	public String getIP() { return ipAddr; }
	public int getPort() { return port; }

	public String getClassName() { return className; }
	public String getObjName() { return objName; }
	
	/* Construct the stub of remote object */
	public Object localise() {

		String stubName = className + "_stub";
		
		try {
			Class<?> c = Class.forName(stubName);
			Object o = c.newInstance();
			
			((Remote440Stub) o).setReference(this);
			return o;
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
