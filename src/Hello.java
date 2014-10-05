
public class Hello implements HelloInterface{

	public Hello() throws Remote440Exception {}
	
	@Override
	public String sayHello(String name) throws Remote440Exception {
		return "Hello World!!  " + name;
	}
	
	
}
