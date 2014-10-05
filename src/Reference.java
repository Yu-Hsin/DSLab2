import java.io.Serializable;

public class Reference implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ip;
	private int port;
	private boolean find;
	
	
	public Reference(String ip, int port, boolean find) {
		this.ip = ip;
		this.port = port;
		this.find = find;
	}

	public boolean getfind () {
		return find;
	}
	public String getIP() {
		return ip;
	}

	public int getport() {
		return port;
	}
}