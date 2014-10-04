package stepfour;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;


/*Start trying step3*/

public class StepThree implements Serializable{
	
	private int value, id;
	public StepThree(int value, int id) {
		this.value = value;
		this.id = id;
	}
	public int getValue () {
		return value;
	}
	
	public int getId () {
		return id;
	}
	
	public static void main (String [] args) {
		

		try {
			//writing the object to a file
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("test.out"));
			StepThree object = new StepThree(10, 3);
			output.writeObject(object);
			output.close();
			System.out.println(object.getValue());
			HashSet <Integer> a = new HashSet <Integer>();
			
			//recreating it
			
			ObjectInputStream input = new ObjectInputStream(new FileInputStream("test.out"));
			Object recreatingObj = input.readObject();
			System.out.println(((StepThree) recreatingObj).getValue());
			input.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
