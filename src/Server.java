import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import rmimessage.RMIMessage; //TODO make it in the same package


public class Server {

	private static final int port = 1234; // TODO can we hard-code this?

	public Server() {
		try {
			ServerSocket socket = new ServerSocket(port); // server
			Receiver receiver = new Receiver(socket);
			Thread t = new Thread(receiver);
			t.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void launch() {
		
	}
	public static void main(String[] args) {
		Server server = new Server();
		server.launch();
		//TODO should move the things in construcotr to a launch function
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
				//Do unmarshalling here??
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}//end of ReceiverService class
}
