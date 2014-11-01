import java.net.URL;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Test{


	public static void main(String[] args){

		try{
			String urlParameters = "code=SYSC";
			URL server = new URL("http://localhost");
			URL request = new URL(server, "/server/api/courses.php?" + urlParameters);
			
			System.out.println(request);
			HttpURLConnection connection = (HttpURLConnection)request.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
		
//			connection.setDoOutput(true);
			connection.setDoInput(true);

			// Send request
//			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
//			wr.writeBytes (urlParameters);
//			wr.flush ();
//			wr.close ();


			//Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			
			String line;
			StringBuffer response = new StringBuffer();
			while((line = rd.readLine()) != null){
				response.append(line);
				response.append('\n');
			}
			rd.close();
			System.out.println(response);

		}
		catch(Exception e){
			e.printStackTrace();
		}


	}

}
