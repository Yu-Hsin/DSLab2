import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;


public class Server {

	private static int port2client, port2RMI;
	private static String RMIaddress;
	private static int timestamp = 0;
	private HashMap <String, Object> mapping;
	
	public Server(String IP, int p2R, int p2c) {
		RMIaddress = IP;
		port2client = p2c;
		port2RMI = p2R;
		mapping = new HashMap <String, Object>();
	}
	public void launch() { //creating a port to listen to incoming RMIMessage
		try {
			ServerSocket socket = new ServerSocket(port2client); 
			Receiver receiver = new Receiver(socket);
			Thread t = new Thread(receiver);
			t.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java Server <RMIaddress> <Port# to RMI> <Port# to Client>");
			return;
		}
		
		Server server = new Server(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		server.launch();
		//instantiate objects
		try {
			Hello a = new Hello();
			server.mapping.put("Hello", a);
			server.bind("Hello", RMIaddress, port2RMI);
			
			/* Here is for Zip Code test */
			ZipCodeServerImpl zipcode = new ZipCodeServerImpl();
			server.mapping.put("ZipCodeServer", zipcode);
			server.bind("ZipCodeServer", RMIaddress, port2RMI);
			
			/* Here is for Zip Code RList test */
			ZipCodeRListImpl zcr = new ZipCodeRListImpl();
			server.mapping.put("ZCR", zcr);
			server.bind("ZCR", RMIaddress, port2RMI);
			
			
		} catch (Remote440Exception e) {
			e.printStackTrace();
		}
		
	}

	public void bind(String objName, String ip, int port){
		try {
			System.out.println(ip + " " + port);
			Socket socket = new Socket(ip, port);
			OutputStreamWriter dOut = new OutputStreamWriter(socket.getOutputStream());
			dOut.write(objName);
			dOut.flush();
			dOut.close();
			System.out.println("Binding object name: " + objName + " to RMIregistry");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class Receiver implements Runnable {
		private ServerSocket serversocket;

		public Receiver(ServerSocket serversocket) {
			this.serversocket = serversocket;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					Socket clientConnection = serversocket.accept();// keep listening to this port
					System.out.println("Received RMIMessage......");
					ReceiverService rs = new ReceiverService(clientConnection);
					Thread connectionThread = new Thread(rs);
					connectionThread.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}//end of Receiver class
		
	class ReceiverService implements Runnable {
		private Socket socket;
		
		public ReceiverService (Socket socket) {
			this.socket = socket;
		}
		@Override
		public void run() {
			
			try {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); //read object stream
				Object RMIMessageObj = in.readObject();
				if (RMIMessageObj == null) return; //didn't send anything to the server
				if (!(RMIMessageObj instanceof RMIMessage)) {
					System.out.println("Not a RMIMessage object!");
					return;
				}
				String objName = ((RMIMessage) RMIMessageObj).getObjectName();
				((RMIMessage) RMIMessageObj).invoke(mapping.get(objName));
				
				
				if (((RMIMessage) RMIMessageObj).getReturnVal() instanceof Remote440) {
					InetAddress addr = InetAddress.getLocalHost();
					System.out.println("return a stub" + " from " + addr.getHostAddress());

					mapping.put(objName + timestamp, ((RMIMessage) RMIMessageObj).getReturnVal());
					//mapping.put(objName, ((RMIMessage) RMIMessageObj).getReturnVal());
					RemoteObjectReference ror = new RemoteObjectReference(addr.getHostAddress(),port2client, ((RMIMessage) RMIMessageObj).getReturnVal().getClass().toString(), objName + timestamp);
					
					//RemoteObjectReference ror = new RemoteObjectReference(addr.getHostAddress(),port2client,((RMIMessage) RMIMessageObj).getClassName());

					((RMIMessage) RMIMessageObj).setReturnVal((Remote440)ror.localise());
					timestamp++;
				}
				
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				System.out.println("Sending the result back to the client ......");
				out.writeObject(RMIMessageObj);
							
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}//end of ReceiverService class
}
