import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import rmimessage.RMIMessage; //TODO make it into the same package


public class Server {

	private static final int port = 1234; // TODO can we hard-code this?
	
	//128.2.100.188 -> ghc55 (node 0)
	
	public Server() {

	}
	public void launch() { //creating a port to listen to incoming RMIMessage
		try {
			ServerSocket socket = new ServerSocket(port); 
			Receiver receiver = new Receiver(socket);
			Thread t = new Thread(receiver);
			t.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		Server server = new Server();
		server.launch();
		server.bind ("AAAA","128.2.100.188",2020);
		server.bind ("BBBB","128.2.100.188",2020);
		server.bind ("CCCC","128.2.100.188",2020);
		
	}

	public void bind(String objName, String ip, int port){
		try {
			Socket socket = new Socket(ip, port);
			OutputStreamWriter dOut = new OutputStreamWriter(socket.getOutputStream());
			dOut.write(objName);
			dOut.flush();
			dOut.close();
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
					System.out.println("Received RMIMessage");
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
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}//end of ReceiverService class
}
