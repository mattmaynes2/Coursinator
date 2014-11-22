package cr;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cr.factory.CourseBuilder;
import cr.factory.CourseOfferingBuilder;
import cr.factory.ProgramBuilder;


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
	 * Defines the root location on the crServer where the api can be found
	 * 
	 * @since November 9, 2014
	 */
	private static final String crRoot = "/server";

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
	 * Pings the server and returns anything that is pent back
	 *
	 * Routes to /api/ping.php
	 *
	 * @return A buffered response from the server if the ping was successful
	 *
	 * @throws MalformedURLException If there is an issue with the end point for this server
	 * @throws IOException If the HTTP connection is closed or hangs
	 * @throws SAXException If there is an issue with the XML input stream
	 * @throws ParserConfigurationException If the XML data is malformed
	 *
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	public String ping() throws MalformedURLException, IOException, ParserConfigurationException, SAXException{
		String route = crRoot +  "/api/ping.php";
		return this.bufferStream(this.sendGetRequest(route, ""));
	}
	
	/**
	 * Sends a POST request to the server to create a prerequisite. Multiple prerequisites can be sent at time.
	 * The return is a buffered response from the server.
	 * 
	 * Routes to /api/prerequisites.php
	 * 
	 * @param prereqs The prerequisites to send to be added to the global database
	 * 
	 * @return A buffered response from the server
	 * 
	 * @throws IOException If the HTTP connection is closed or hangs
	 * @throws MalformedURLException If there is an issue with the end point for this server
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	public String addPrerequisites(Prerequisite[] prereqs) throws MalformedURLException, IOException{
		String route = crRoot +  "/api/prerequisites.php";
		StringBuffer xml = new StringBuffer("<prerequisites>");
		HashMap<String, String> params = new HashMap<String, String>();
		
		for(Prerequisite p : prereqs){
			xml.append(p.serialize());
		}
		xml.append("</prerequisites>");
		params.put("prerequisite", xml.toString());
		
		return this.bufferStream(this.sendPostRequest(route, params));
	}
	
	
	/**
	 * Returns a the programs on the server that match the given string
	 *
	 * Routes to /api/programs.php
	 *
	 * @param program The program name to get from the server
	 *
	 * @return Program structures that matches the given program identifier
	 *
	 * @throws MalformedURLException If there is an issue with the end point for this server
	 * @throws IOException If the HTTP connection is closed or hangs
	 * @throws SAXException If there is an issue with the XML input stream
	 * @throws ParserConfigurationException If the XML data is malformed
	 *
	 * @author Matthew Maynes
	 * @since November 22, 2014
	 */
	public Program[] getPrograms(String program) throws MalformedURLException, ParserConfigurationException, SAXException, IOException{
		String route = crRoot + "/api/programs.php";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("program", program);
		ProgramBuilder builder = new ProgramBuilder();
		return  builder.read(this.sendGetRequest(route, params));
	}
	
	/**
	 * Returns all of the programs on the server
	 *
	 * Routes to /api/programs.php
	 *
	 * @param program The program name to get from the server
	 *
	 * @return Program structures that matches the given program identifier
	 *
	 * @throws MalformedURLException If there is an issue with the end point for this server
	 * @throws IOException If the HTTP connection is closed or hangs
	 * @throws SAXException If there is an issue with the XML input stream
	 * @throws ParserConfigurationException If the XML data is malformed
	 *
	 * @author Matthew Maynes
	 * @since November 22, 2014
	 */
	public Program[] getPrograms() throws MalformedURLException, ParserConfigurationException, SAXException, IOException{
		String route = crRoot + "/api/programs.php";
		ProgramBuilder builder = new ProgramBuilder();
		return  builder.read(this.sendGetRequest(route, ""));
	}
	
	/**
	 * Returns a program element structure for each program that matches the given program ID
	 *
	 * Routes to /api/programelements.php
	 *
	 * @param program The program elements to get from the server
	 *
	 * @return An array of program elements related to the given program
	 *
	 * @throws MalformedURLException If there is an issue with the end point for this server
	 * @throws IOException If the HTTP connection is closed or hangs
	 * @throws SAXException If there is an issue with the XML input stream
	 * @throws ParserConfigurationException If the XML data is malformed
	 *
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	public ProgramElement[] getProgramElements(String program) throws MalformedURLException, ParserConfigurationException, SAXException, IOException{
		String route = crRoot + "/api/programelements.php";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("program", program);
		ProgramBuilder builder = new ProgramBuilder();
		Program[] programs =  builder.read(this.sendGetRequest(route, params));
		if(programs.length > 0)
				return programs[0].getElements().toArray(new ProgramElement[0]);
		return new ProgramElement[0];
	}

	
	/**
	 * Returns a list of courses that match the given pattern
	 *
	 * Routes to /api/courses.php
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
		String route = crRoot +  "/api/courses.php";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("code", pattern);
		CourseBuilder builder = new CourseBuilder();
		return builder.read(this.sendGetRequest(route, params));
	}

	/**
	 * Returns a list of courses that can be taken given a list of completed courses and courses in progress
	 *
	 * Routes to /api/courses.php
	 *
	 * @param complete The courses that have been completed
	 * @param taking The courses that are currently being taken
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
	public Course[] getCourses(Course[] complete, Course[] taking) throws MalformedURLException, IOException, ParserConfigurationException, SAXException{
		String route = crRoot +  "/api/courses.php";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("qualified", "");
		params.putAll(this.encodeArrayParameter("completed", complete));
		params.putAll(this.encodeArrayParameter("taking", complete));

		CourseBuilder builder = new CourseBuilder();
		return builder.read(this.sendGetRequest(route, params));
	}

	
	/**
	 * Returns a list of courses that match the given pattern
	 *
	 * Routes to /api/courses.php
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
	public CourseOffering[] getCourseOfferings(String pattern) throws MalformedURLException, IOException, ParserConfigurationException, SAXException{
		String route = crRoot + "/api/courseofferings.php";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("code", pattern);
		CourseOfferingBuilder builder = new CourseOfferingBuilder();
		return builder.read(this.sendGetRequest(route, params));
	}
	
}
