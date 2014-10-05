import java.io.Serializable;


public class RemoteObjectReference implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ipAddr;
	private int port;
	private String className;
	
	public RemoteObjectReference(String ip, int p, String cName) {
		ipAddr = ip;
		port = p;
		className = cName;
	}
	
	public String getIP() { return ipAddr; }
	public int getPort() { return port; }
	public String getClassName() { return className; }
	
	
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
