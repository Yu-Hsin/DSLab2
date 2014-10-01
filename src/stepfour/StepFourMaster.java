package stepfour;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class StepFourMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int port = 5566;
	
	private int value, id;
	public StepFourMaster(int value, int id) {
		this.value = value;
		this.id = id;
	}
	public int getValue () {
		return value;
	}
	
	public int getId () {
		return id;
	}
	
	
	public static void main(String[] args) {
		
		Socket otherNodeSocket;
		try {
			otherNodeSocket = new Socket("127.0.0.1", port);
			Client mClient = new Client(otherNodeSocket, new StepFourMaster(30, 1));
			
			
			System.out.println("Start transmission");

	        Thread t = new Thread(mClient);
	        t.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
