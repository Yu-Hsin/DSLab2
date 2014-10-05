import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class RMIregistry {
	
	private static int port2client; //the port used to communicate with the client
	private static int port2server; //the port used to communicate with the server
	private static int portclient2server;
	private HashMap <String, Reference> mapping; 
	
	
	public RMIregistry(int p2c, int p2s, int c2s) {
		mapping = new HashMap <String, Reference>();
		port2client = p2c;
		port2server = p2s;
		portclient2server = c2s;
	}
	
	public void launch() {
		try {
			ServerSocket socketclient = new ServerSocket(port2client);
			ServerSocket socketserver = new ServerSocket(port2server);
			
			ReceiverClient receiverClient = new ReceiverClient(socketclient);
			ReceiverServer receiverServer = new ReceiverServer(socketserver);
			
			new Thread(receiverClient).start();
			new Thread(receiverServer).start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main (String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java RMIregistr <port# to client> <port# to server> <port# client to server>");
			return;
		}
		RMIregistry rmiregistry = new RMIregistry(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		rmiregistry.launch();
	}

	
	class ReceiverServer implements Runnable {
		private ServerSocket serversocket;

		public ReceiverServer (ServerSocket serversocket) {
			this.serversocket = serversocket;
		}
		@Override
		public void run() {
			while (true) {
				Socket serverConnection;
				try {
					serverConnection = serversocket.accept();
					ServerService ss = new ServerService(serverConnection);
					new Thread(ss).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}//end of receiverserver class
	
	
	class ReceiverClient implements Runnable {
		private ServerSocket serversocket;

		public ReceiverClient(ServerSocket serversocket) {
			this.serversocket = serversocket;
		}
		@Override
		public void run() {
			while (true) {
				try {
					Socket clientConnection = serversocket.accept();// keep listening to this port
					ClientService cs = new ClientService(clientConnection);
					new Thread(cs).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}//end of Receiverclient class
	
	class ServerService implements Runnable {
		private Socket socket;
		
		public ServerService (Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			String ip = socket.getInetAddress().getHostName();
			Reference rr = new Reference(ip, portclient2server, true);
			try {
				BufferedReader str = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String objName = str.readLine();
				str.close();
				System.out.println("Registering for the object: " + objName + "......");
				mapping.put(objName, rr);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}		
	}
	
	

	
	class ClientService implements Runnable {
		private Socket socket;
		public ClientService (Socket socket) {
			this.socket = socket;
		}
		@Override
		public void run() {
			 
			try {
				BufferedReader str = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String objName = str.readLine();
				System.out.println("Searching for refernce of object: " + objName);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				Reference rr = null;
				if (!mapping.containsKey(objName)) {
					System.out.println("Haven't registered for object: " + objName);
					rr = new Reference ("", 0, false);
				} else {
					System.out.println("Reference found...");
					System.out.println("Reference sent...");
					rr = mapping.get(objName);
				}
				
				out.writeObject(rr);
				out.flush();
				out.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
