package stepfour;

import java.io.IOException;
import java.net.ServerSocket;

public class StepFourClient {
	private static final int port = 5566;
	
	
	
	public static void main(String[] args) {
		
		try {
			ServerSocket mServer = new ServerSocket(port); //server
			Matser mMaster = new Matser(mServer);
			Thread t = new Thread(mMaster);  
			t.start(); //keep listening to this port

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
