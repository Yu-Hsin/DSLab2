public interface ZipCodeRList extends Remote440// extends YourRemote or whatever
{
    public String find(String city) throws Remote440Exception;
    public ZipCodeRList add(String city, String zipcode) throws Remote440Exception;
    public ZipCodeRList next() throws Remote440Exception;   

}
