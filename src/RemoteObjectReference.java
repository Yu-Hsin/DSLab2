
public class RemoteObjectReference {

	private String ipAddr;
	private int port;
	private String remoteInterfaceName;
	private int objID;
	
	public RemoteObjectReference(String ip, int p, String riName, int objid) {
		ipAddr = ip;
		port = p;
		remoteInterfaceName = riName;
		objID = objid;
	}
	
	public String getIP() { return ipAddr; }
	public int getPort() { return port; }
	public String getInterfaceName() { return remoteInterfaceName; }
	public int getID() { return objID; }
	
	
	public Object localise() {
	
		String stubName = remoteInterfaceName + "_stub";
		
		try {
			Class<?> c = Class.forName(stubName);
			Object o = c.newInstance();
			
			return o;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
