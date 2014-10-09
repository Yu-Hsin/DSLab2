
public interface ZipCodeServer extends Remote440 {
	public void initialise(ZipCodeList newlist) throws Remote440Exception;
    public String find(String city) throws Remote440Exception;
    public ZipCodeList findAll() throws Remote440Exception;
    public void printAll() throws Remote440Exception;
}
