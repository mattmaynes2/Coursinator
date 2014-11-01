package cr;

import java.util.HashMap;
import java.util.Entry;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Request
 * 
 * A request can be used to send data to the coursinator server and will return the output.
 * The server API has a defualt mapping but can be overridden by setting the api routes for 
 * each function.
 *
 * @since October 31, 2014
 * @version 0.0.1
 */
public class Request{
	
	/**
	 * The URL of the server root.
	 *
	 * @author Matthew Maynes
	 * @since October 30, 2014
	 */
	private URL server;
	
	/**
	 * Constructs a request object that points to the given server
	 *
	 * @param server The server URL
	 * 
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public Request(URL server){
		this.server = server;
	}
	
	/**
	 * Sets the server URL for this objects primary server
	 *
	 * @param server The server URL
	 * 
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public void setServer(URL server){
		this.server = server;
	}
	
	/**
	 * Returns the server URL for this objects primary server
	 *
	 * @return The server URL
	 * 
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public URL getServer(){
		return this.server;
	}
	
	/**
	 * Sends a GET request to this objects server using the given API route
	 * and parameters
	 *
	 * @param route The path to the function call on this server 
	 * @param params The GET parameters for this request
	 *
	 * @return Returns the result of the GET request or null
	 *
	 * @throws MalformedURLException If the URL is invalid
	 * @throws IOException If the connection could not be made or there was an error in the communication
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public String sendGetRequest(String route, HashMap<String, String> params) throws MalformedURLException, IOException{
		this.sendGetRequest(route, this.joinParameters(params));
	}
	
	/**
	 * Sends a GET request to this objects server using the given API route
	 * and parameters
	 *
	 * @param route The path to the function call on this server 
	 * @param params The GET parameters for this request in a URL format
	 *
	 * @return Returns the result of the GET request or null
	 *
	 * @throws MalformedURLException If the URL is invalid
	 * @throws IOException If the connection could not be made or there was an error in the communication
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public String sendGetRequest(String route, String params) throws MalformedURLException, IOException{
		String buffer = route + '?' + params;
		URL request = new URL(this.server, buffer);
		HttpURLConnection connection = (HttpURLConnection)request.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		
		return bufferResponse(connection.getInputStream());
		
	}

	/**
	 * Sends a POST request to this objects server using the given API route
	 * and parameters.
	 *
	 * @param route The path to the function call on this server
	 * @param params The POST parameters for this request
	 *
	 * @return Returns the result of the POST request or null
	 *
	 * @throws MalformedURLException If the URL is invalid
	 * @throws IOException If the connection could not be made or there was an error in the communication
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public String sendPostRequest(String route, HashMap<String, String> params) throws MalformedURLException, IOException{
		this.sendPostRequest(route, this.joinParameters(params));
	}

	/**
	 * Sends a POST request to this objects server using the given API route
	 * and parameters
	 * 
	 * @param route The path to the function call on this server
	 * @param params The POST parameters for this request in a URL format
	 *
	 * @return Returns the result of the POST request or null
	 *
	 * @throws MalformedURLException If the URL is invalid
	 * @throws IOException If the connection could not be made or there was an error in the communication
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public String sendPostRequest(String route, String params) throws MalformedURLException, IOException{
		URL request = new URL(this.server, route);
		HttpURLConnection connection = (HttpURLConnection)request.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		// Send the request
		DataOutputStream stream = new DataOutputStream (connection.getOutputStream());
		wr.writeBytes (params);
		wr.flush ();
		wr.close ();
		
		return bufferResponse(connection.getInputStream());
	}

	/**
	 * Given a parameter list, join the parameters into a single string 
	 * seperated by &amp; in key=value pairs.
	 *
	 * @param params The URL parameters than need to be flattened
	 *
	 * @return A string version of the flattened parameters 
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public String joinParameters(HashMap<String, String> params){
		StringBuffer list = new StringBuffer();
		for(Entry<String, String> entry : params.entrySet()){
			list.append(entry.getKey() + (params.getValue() == null ? '' : '=' + params.getValue());
			list.append('&');
		}
		return list.length > 0 ? list.deleteCharAt(list.length - 1).toString() : "";
	}

	/**
	 * Given an input stream from a HTTP response, buffer the input into a single
	 * string and return. Throws an IO exception if the response stream is invalid
	 * or closed.
	 *
	 * @param responseStream The input stream from the HTTP response
	 * 
	 * @return A buffered string from the response message
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	private String bufferResponse(InputStream responseStream) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
		StringBuffer response = new StringBuffer();
		String line;
		
		while((line = reader.readLine()) != null){
			response.append(line);
			response.append('\n');
		}
		reader.close();
		return response;
	}


}
