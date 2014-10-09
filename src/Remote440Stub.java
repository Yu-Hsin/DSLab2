/*
 * Every stub must implement this interface.
 * We need to know whether this remote object is a stub.
 * If this is a stub, it must contain the remote reference 
 * to the actual remote object in the host.
 */
public interface Remote440Stub extends Remote440{

	public void setReference(RemoteObjectReference r);
	
}
