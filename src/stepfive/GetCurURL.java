package stepfive;

public class GetCurURL {

	
	public String foo() {
		return this.getClass().getClassLoader().getResource("").getPath();
	}
	
	public static void main(String[] args) {
		GetCurURL obj = new GetCurURL();
		System.out.println(obj.foo());
	}
}
