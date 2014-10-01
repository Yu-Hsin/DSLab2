package stepfour;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private Socket mSocket;
    
    private Object toBeTransmitt;

    public Client(Socket s, Object o) {
    	mSocket = s;
    	toBeTransmitt = o;
    }
    
    
    @Override
    public void run() {
    	
    	try {
    	    ObjectOutputStream out = new ObjectOutputStream(mSocket.getOutputStream());

    	    
    	    out.writeObject(toBeTransmitt);
	    	out.flush();
	    	System.out.println("Finish transmission");
	    	
	    	//wait for response from other nodes
	    	ObjectInputStream in = new ObjectInputStream(mSocket.getInputStream());
	    	
	    	while(true){
	    		Object response = in.readObject();
	    	}
    	    

    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    }
}
