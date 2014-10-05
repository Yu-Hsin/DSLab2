
public class HelloClient {

	
	public static void main(String[] args) {
		
		try {
			String ip = null;
			int port = 0;
			
			if (args.length != 2) {
				System.out.println("Usage: java HelloClient <IP of RMI registry> <port#>");
				return;
			}
			
			ip = args[0];
			port = Integer.valueOf(args[1]);
			
			
			RMIRegistryClient registry = RMIRegistryClient.getRegistry(ip, port);
			HelloInterface h = (HelloInterface) registry.lookup("Hello");
			
			String msg = h.sayHello("Jason");
			System.out.println(msg);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
