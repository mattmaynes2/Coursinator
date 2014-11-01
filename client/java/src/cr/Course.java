package cr;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Course
 *
 * Defines a course object following the Coursinator schema
 * 
 * @version 0.0.0
 * @since October 6, 2014
 */
public class Course{

	/**
	 * The Coursinator XML identifier tag
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	private static final String SCHEMA_IDENTIFIER = "course";

	/**
	 * The code of this course in normalized form (ex. SYSC4504)
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private String code;
	
	/**
	 * The title of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private String title;
	
	/**
	 * The level that this course is offered at
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	private String level;
	
	/**
	 * A short description of this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	private String description;
	
	/**
	 * The terms that this course spans
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private int span;
	
	/**
	 *  Initializes an empty Course object
	 *  
	 *  @author Matthew Maynes
	 *  @since November 1, 2014
	 */
	public Course(){
		this.code = "";
		this.title = "";
		this.level = "";
		this.description = "";
		this.span = 0;
	}
	
	/**
	 * Returns the code of this course
	 *
	 * @return the code of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public String getCode(){
		return this.code;
	}
	
	/**
	 * Returns the title of this course
	 *
	 * @return the title of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * Returns the level of this course
	 *
	 * @return the level of this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public String getLevel(){
		return this.level;
	}
	
	/**
	 * Returns the description of this course
	 *
	 * @return the description of this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * Returns the number of terms this course spans
	 * 
	 * @return the term span of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public int getSpan(){
		return this.span;
	}
	
	/**
	 * Sets the course code for this course and normalizes any invalid whitespace
	 *
	 * @param code The new code for this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public void setCode(String code){
		if(code != null && code.contains(" ")){
			this.code = code.replace(" ", "");	
		}
		else { 
			this.code = code;
		}
	}
	
	/**
	 * Sets the title for this course
	 *
	 * @param title The new title for this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * Sets the level for this course
	 *
	 * @param level The new level for this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public void setLevel(String level){
		this.level = level;
	}
	
	/**
	 * Sets the description for this course
	 *
	 * @param description The new description for this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public void setDescription(String description){
		this.description = description;
	}	
	
	/**
	 * Sets the term span for this course
	 *
	 * @param span The number of terms this course spans
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public void setSpan(int span){
		this.span = span;
	}
		
	/**
	 * Serializes this object into xml form following the Coursinator course in the XML schema.
	 *
	 * @return A valid XML serialized string representing this object
	 *
	 * @since October 31, 2014
	 * @author Matthew Maynes
	 */	
	public String serialize(){
		StringBuffer buffer = new StringBuffer();
		HashMap<String, String> schema = new HashMap<String, String>();
		schema.put("code", this.getCode());
		schema.put("title", this.getTitle());
		schema.put("term-span", Integer.toString(this.getSpan()));
		schema.put("level", this.getLevel());
		schema.put("desc", this.getDescription());
		
		// Build the XML output
		buffer.append("<" + SCHEMA_IDENTIFIER + ">");
		for(Entry<String, String> element : schema.entrySet()){
			buffer.append("<" + element.getKey() + ">" + element.getValue() + "</" + element.getKey() + ">");
		}
		
		
		buffer.append("</" + SCHEMA_IDENTIFIER + ">");
		return buffer.toString();
	}	
		
	/**
	 * Returns a serialized version of this course object following the Coursinator schema.
	 * 
	 * @return A XML serialized version of this course
	 *
	 * @see #serialize()
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	@Override
	public String toString(){
		return this.serialize();
	}
	
	/**
	 * Given XML data in an input stream, buffer and deserialize it to create a course objects.
	 * 
	 * @throws ParserConfigurationException If the input XML is malformed
	 * @throws SAXException 				If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 					If there is an error with the input stream. 
	 *
	 */
	public static Course[] read(InputStream stream) throws ParserConfigurationException, SAXException, IOException{
		ArrayList<Course> courses = new ArrayList<Course>();
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc 			= builder.parse(stream);
		NodeList nodes 			= doc.getElementsByTagName(SCHEMA_IDENTIFIER);
		
		
		for(int i = 0; i < nodes.getLength(); i++){
			System.out.println(nodes.item(i));
			courses.add(Course.buildCourse((Element)nodes.item(i)));
		}
		return courses.toArray(new Course[0]);
	}
	
	/**
	 * Builds a course given the data in the passed node
	 *
	 * @param node - The node to parse
	 *
	 * @return A new course that represents the data in the given node
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public static Course buildCourse(Element node){
		Course course = new Course();
		NodeList nodes;
	
		nodes = node.getElementsByTagName("code");
		if(nodes.getLength() > 0 && nodes.item(0).getNodeValue() != null) course.setCode(nodes.item(0).getNodeValue());
		
		nodes = node.getElementsByTagName("title");
		if(nodes.getLength() > 0 && nodes.item(0).getNodeValue() != null) course.setTitle(nodes.item(0).getNodeValue());
		
		nodes = node.getElementsByTagName("term-span");
		if(nodes.getLength() > 0 && nodes.item(0).getNodeValue() != null) course.setSpan(Integer.parseInt(nodes.item(0).getNodeValue()));
		
		nodes = node.getElementsByTagName("level");
		if(nodes.getLength() > 0 && nodes.item(0).getNodeValue() != null) course.setLevel(nodes.item(0).getNodeValue());
		
		nodes = node.getElementsByTagName("desc");
		if(nodes.getLength() > 0 && nodes.item(0).getNodeValue() != null) course.setDescription(nodes.item(0).getNodeValue());
		
		return course;	
	}
	
	
}
