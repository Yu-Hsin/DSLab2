package rmimessage;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RMIMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int objectID;
	
	private String methodName;
	private Object[] params;
	private Object returnVal;
	
	public RMIMessage(int id, String m, Object[] p) {
		objectID = id;
		methodName = m;
		params = p;
	}
	
	/* Get Information */
	public int getObjectID() { return objectID; }
	public String getMethodName() { return methodName; }
	public Object[] getParams() { return params; }
	public Object getReturnVal() { return returnVal; }
	
	/* Invoke method in the object */
	public void invoke(Object remoteObj) {
			
		Method method = null;
		try {
			if (params == null) {
				method = remoteObj.getClass().getMethod(methodName);
			}
			else {
				Class<?>[] paramsClass = new Class<?>[params.length];
				for (int i = 0; i < params.length; i++) {
					paramsClass[i] = params[i].getClass();
				}
				
				method = remoteObj.getClass().getMethod(methodName, paramsClass);
			}
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("No Such Method!!!!!!");
			e.printStackTrace();
		}
	
		try {
			returnVal = method.invoke(remoteObj, params);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}	
}
