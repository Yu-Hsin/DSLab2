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

  //variable declaration
	private static int port2client, port2RMI;
	private static String RMIaddress;
	private static int timestamp = 0;
	private HashMap <String, Object> mapping;
	private HashMap <Object, String> invertedMap;
	
	public Server(String IP, int p2R, int p2c) {
		RMIaddress = IP;
		port2client = p2c;
		port2RMI = p2R;
		mapping = new HashMap <String, Object>();
		invertedMap = new HashMap <Object, String>();
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
		
			/* Here is for Zip Code test */
			ZipCodeServerImpl zipcode = new ZipCodeServerImpl();
			server.mapping.put("ZipCodeServer", zipcode);
			server.invertedMap.put(zipcode, "ZipCodeServer");
			server.bind("ZipCodeServer", RMIaddress, port2RMI);
			
			/* Here is for Zip Code RList test */
			ZipCodeRListImpl zcr = new ZipCodeRListImpl();
			server.mapping.put("ZCR", zcr);
			server.invertedMap.put(zcr, "ZCR");
			server.bind("ZCR", RMIaddress, port2RMI);
			
	}

	//bind the object name to the RMI registry server
	public void bind(String objName, String ip, int port){
		try {
			Socket socket = new Socket(ip, port);
			OutputStreamWriter dOut = new OutputStreamWriter(socket.getOutputStream());
			dOut.write(objName + "\n");
			dOut.write(String.valueOf(port2client) + "\n");
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
				
				if (!(RMIMessageObj instanceof RMIMessage)) { //check if the message is a RMIMessageObj object
					System.out.println("Not a RMIMessage object!");
					return;
				}
				
				/* Now we are sure RMIMessageObj is a RMIMessage */
				RMIMessage mMsg = ((RMIMessage) RMIMessageObj);
				String objName = mMsg.getObjectName();
				mMsg.invoke(mapping.get(objName));
				
				/* If the return value is also a remote object, return its STUB */
				if (mMsg.getReturnVal() instanceof Remote440) {
					InetAddress addr = InetAddress.getLocalHost();
					Object returnObj = mMsg.getReturnVal();
					System.out.println("Returned object is a romote object: pass by referecne ...... sent ......");
					
					/* Check if we need to create a new name for this return object*/
					if (invertedMap.containsKey(returnObj)) {
						RemoteObjectReference ror = new RemoteObjectReference(addr.getHostAddress(), 
																			  port2client, 
																			  mMsg.getClassName(), 
																			  invertedMap.get(returnObj));
						mMsg.setReturnVal((Remote440)ror.localise());					
					} else {
						mapping.put(objName + timestamp, returnObj);
						invertedMap.put(returnObj, objName + timestamp);
						RemoteObjectReference ror = new RemoteObjectReference(addr.getHostAddress(),
																			  port2client, 
																			  mMsg.getClassName(), 
																			  objName + timestamp);
						mMsg.setReturnVal((Remote440)ror.localise());
						timestamp++;
					}
				}
				else
				  System.out.println("Returned object is not a remote object: pass by value ...... sent ......");
				
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(RMIMessageObj);
							
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}//end of ReceiverService class
}
