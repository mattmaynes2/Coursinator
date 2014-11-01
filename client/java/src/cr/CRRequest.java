package cr;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * CRRequest
 *
 * An API wrapper for the Coursinator API. 
 *
 * @since October 31, 2014
 * @version 0.0.1
 */
public class CRRequest extends Request{
	
	/**
	 * The default URL for the coursinator server
	 *
	 * @since October 31, 2014
	 */
	private static final String crServer = "http://localhost";

	/**
	 * Creates a Coursinator server request object
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public CRRequest(){
		try {
			this.setServer(new URL(crServer));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Returns a list of courses that match the given pattern
	 *
	 * Routes to /server/api/courses.php
	 *
	 * @param pattern The course pattern to match against
	 *
	 * @return An array of courses that meet the given criteria or an empty array
	 *
	 * @throws MalformedURLException If there is an issue with the end point for this server
	 * @throws IOException If the HTTP connection is closed or hangs
	 * @throws SAXException If there is an issue with the XML input stream
	 * @throws ParserConfigurationException If the XML data is malformed
	 *
	 * @author Matthew Maynes
	 * @since October 31, 2014
	 */
	public Course[] getCourses(String pattern) throws MalformedURLException, IOException, ParserConfigurationException, SAXException{
		String route = "/server/api/courses.php";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("code", pattern);
		return Course.read(this.sendGetRequest(route, params));
	}

}
