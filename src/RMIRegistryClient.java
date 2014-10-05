import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class RMIRegistryClient {
	private String registryIP = null;
	private int port = 0;
	
	public RMIRegistryClient(String ip, int p) {
		registryIP = ip;
		port = p;
	}
	
	public static RMIRegistryClient getRegistry(String host, int p){
		return new RMIRegistryClient(host, p);
	}
	
	public Remote440 lookup(String serviceName) {
		
		try {
			RemoteObjectReference objRef = null;
			Socket s = new Socket(registryIP, port);
			
			OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
			out.write(serviceName+"\n");
			out.flush();
			
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			Object response = in.readObject();
			
			if (response instanceof Reference) {
				Reference ref = (Reference) response;
				objRef = new RemoteObjectReference(ref.getIP(), ref.getport(), serviceName);
			}
			
			s.close();
			
			if (objRef == null) return null;
			else {
				return (Remote440) objRef.localise();
			}
			
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
