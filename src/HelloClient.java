
public class HelloClient {

	
	public static void main(String[] args) {
		
		try {
			String ip = null;
			int port = 1099;
			
			if (args.length == 0) {
				System.out.println("ERROR: Insert host ip address!!!!");
				return;
			}
			else if (args.length == 1) {
				ip = args[0];
			}
			else if (args.length == 2) {
				ip = args[0];
				port = Integer.valueOf(args[1]);
			}
			
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
