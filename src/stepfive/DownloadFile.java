package stepfive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFile {

	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("java DownloadFile <Directory+filename>");
			return;
		}
		
		
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(args[0]);
			httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream input = httpConn.getInputStream();
				FileOutputStream output = new FileOutputStream("Hello_stub.class");
				byte[] buffer = new byte[4096];
				int read = -1;
				while ((read = input.read(buffer)) != -1) {
					output.write(buffer, 0, read);
				}
				output.close();
				input.close();
				System.out.println("File transfer done");
			} else {
				System.out.println("HTTP request failed: "+ responseCode);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpConn.disconnect();
		
	}
}
