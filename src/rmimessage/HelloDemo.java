package rmimessage;

import java.util.HashMap;

public class HelloDemo {

	private HashMap<Integer, Hello> objectMap = null;
	
	public HelloDemo() {
		objectMap = new HashMap<Integer, Hello>();
	}
	
	public void registryHello(Hello h) {
		objectMap.put(h.id, h);
	}
	
	public static class Hello {
		public int id;
		
		public Hello(int i) {
			this.id = i;
		}
		
		public void sayHello(String s) {
			System.out.println("Hello " + s);
		}
		
		public void sayHello() {
			System.out.println("Hello");
		}
		
	}
	
	public static void main(String[] args) {
		
		HelloDemo demo = new HelloDemo();
		
		int demoID = 10;
		Hello demoObj = new Hello(demoID);
		
		demo.registryHello(demoObj);
		
		/* Create RMIMessage */
		Object[] param = new Object[1];
		param[0] = "Jason";
		RMIMessage msg = new RMIMessage(demoID, "sayHello", param);
		RMIMessage msg2 = new RMIMessage(demoID, "sayHello", null);
		
		/* invoke message */
		msg.invoke(demoObj);
		msg2.invoke(demoObj);
	}
	
	
	
}
