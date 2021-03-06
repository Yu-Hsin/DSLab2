

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* 
 * Encapsulate the remote method invocation
 */
public class RMIMessage implements Serializable{

	private static final long serialVersionUID = 1L;

	private String objectName;
	private String className;
	
	private String methodName;
	private Object[] params;
	private Object returnVal;
	
	public RMIMessage(String c, String m, Object[] p) {
		objectName = c;
		methodName = m;
		params = p;
	}
	
	/* Get Information */
	public String getObjectName() { return objectName; }
	public String getClassName() { return className; }
	public String getMethodName() { return methodName; }
	public Object[] getParams() { return params; }
	public Object getReturnVal() { return returnVal; }
	public void setReturnVal(Object o) { returnVal = o; }
	public void setClassName(String c) { className = c; }
	
	/* Invoke method of the object */
	public void invoke(Object remoteObj) {
		Method method = null;
		try {
		  
			// There are no parameters
			if (params == null) {
				method = remoteObj.getClass().getMethod(methodName);
			}
			// There are parameters
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
			System.out.println("No Such Method!");
			e.printStackTrace();
		}
	
		try {
			returnVal = method.invoke(remoteObj, params);
			if (returnVal != null) {
				className = returnVal.getClass().toString();
				className = className.substring(className.indexOf(" ") + 1);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
