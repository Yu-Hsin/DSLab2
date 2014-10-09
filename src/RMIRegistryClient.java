import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class RMIRegistryClient {
	private String registryIP = null;
	private int registryport = 0;
	
	
	public RMIRegistryClient(String ip, int p) {
		registryIP = ip;
		registryport = p;
	}
	
	public static RMIRegistryClient getRegistry(String host, int p){
		return new RMIRegistryClient(host, p);
	}
	
	public Remote440 lookup(String objName, String className) throws Remote440Exception{
		
		try {
			RemoteObjectReference objRef = null;
			Socket s = new Socket(registryIP, registryport);
			
			OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
			out.write(objName+"\n");
			out.flush();
			
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Object response = in.readObject();
			
			
			if (response instanceof Reference) {
				Reference ref = (Reference) response;
				if (!ref.getfind()) {
					System.out.println(objName + " hasn't been registered yet!");
					throw new Remote440Exception("Remote Object not found!!");
				}
				objRef = new RemoteObjectReference(ref.getIP(), ref.getport(), className, objName);
			}
			
			s.close();			
			return objRef == null? null: (Remote440) objRef.localise();
	
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
