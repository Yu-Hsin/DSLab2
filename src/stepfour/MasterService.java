package stepfour;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MasterService implements Runnable {
    private Socket mSocket;

    public MasterService(Socket s) {
    	mSocket = s;
    }

    @Override
    public void run() {
    	
	try {

	    ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
	    
	    while (true) {
	    	Object migratedObj = in.readObject();
	    	
	    	if (migratedObj instanceof StepFourMaster) {
	    		int val = ((StepFourMaster)migratedObj).getValue();
	    		int id = ((StepFourMaster)migratedObj).getId();
	    		
	    		System.out.println("Get Object:  " + id + " " + val);
	    	}
	    	
	    }

	}
	catch(IOException e) {
		System.out.print("in" + e);
	}
	catch (Exception e) {
		System.out.println("in2" + e);
	    e.printStackTrace();
	}
    }
}
