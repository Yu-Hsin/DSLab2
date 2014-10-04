
public class Hello implements HelloInterface{

	public Hello() throws Remote440Exception {
		
	}
	
	@Override
	public String sayHello(String name) throws Remote440Exception {
		return "Hello World!!  " + name;
	}

	/* Registry to Server */
	public static void main(String[] args) {
		
		try {
			
			Hello server = new Hello();
			
			/* TODO: registry, and naming......... */
			
			
		} catch(Exception e) {
			
		}
		
	}
	
	
}
