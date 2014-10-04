import java.io.Serializable;
import java.net.SocketAddress;

public class Reference implements Serializable {

	private static final long serialVersionUID = 1L;
	private SocketAddress ip;
	private int port;

	public Reference(SocketAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public SocketAddress getIP() {
		return ip;
	}

	public int getport() {
		return port;
	}
}