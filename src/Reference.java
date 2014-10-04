import java.io.Serializable;

public class Reference implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ip;
	private int port;

	public Reference(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIP() {
		return ip;
	}

	public int getport() {
		return port;
	}
}