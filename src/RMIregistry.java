import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;


public class RMIregistry {
	
	private static final int port2client = 4040;
	private static final int port2server = 2020;
	private HashMap <String, RemoteRef> mapping;
	
	
	public class RemoteRef implements Serializable{
	
		private static final long serialVersionUID = 1L;
		private SocketAddress ip;
		private int port;
		
		public RemoteRef(SocketAddress ip, int port) {
			this.ip = ip;
			this.port = port;
		}
		
		public SocketAddress getIP () {return ip;}
		public int getport () {return port;}
	}
	
	
	public RMIregistry() {
		mapping = new HashMap <String, RemoteRef>();
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
		RMIregistry rmiregistry = new RMIregistry();
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
					ServerService rs = new ServerService(serverConnection);
					new Thread(rs).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	class ServerService implements Runnable {
		private Socket socket;
		
		public ServerService (Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			int remoteport = socket.getPort();
			SocketAddress sa = socket.getRemoteSocketAddress();
			RemoteRef rr = new RemoteRef(sa, remoteport);
			try {
				BufferedReader str = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String objName = str.readLine();
				mapping.put(objName, rr);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}		
	}
	
	
	
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
	}//end of Receiver class
	
	class ClientService implements Runnable {
		private Socket socket;
		public ClientService (Socket socket) {
			this.socket = socket;
		}
		@Override
		public void run() {
			BufferedReader str;
			try {
				str = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String objName = str.readLine();
				RemoteRef rr = mapping.get(objName);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(rr);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
