/*
 * Every function with remote functions must catch 
 * Remote440Exception
 */
public class Remote440Exception extends Exception {

	private static final long serialVersionUID = 1L;

	public Remote440Exception(String msg) {
		super(msg);
	}
	
	public Remote440Exception() {
		super();
	}
	
}
