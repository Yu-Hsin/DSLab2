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
			
			
			System.out.println("AAAAA");
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			System.out.println("BBBBB");
			Object response = in.readObject();
			System.out.println("DDDDD");
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}