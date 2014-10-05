
public class RemoteObjectReference {

	private String ipAddr;
	private int port;
	private String remoteInterfaceName;
	
	public RemoteObjectReference(String ip, int p, String riName) {
		ipAddr = ip;
		port = p;
		remoteInterfaceName = riName;
	}
	
	public String getIP() { return ipAddr; }
	public int getPort() { return port; }
	public String getInterfaceName() { return remoteInterfaceName; }
	
	
	public Object localise() {
	
		String stubName = remoteInterfaceName + "_stub";
		
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
