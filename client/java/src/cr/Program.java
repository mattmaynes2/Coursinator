package cr;

import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Program
 *
 * Defines a program structure via its elements and courses
 * 
 * @version 0.0.1
 * @since October 6, 2014
 */
public class Program{

	/**
	 * The Coursinator XML identifier tag
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	private static final String SCHEMA_IDENTIFIER = "program";
	
	/**
	 * Stores all of the elements that are part of this program
	 * 
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private List<ProgramElement> elements;

	/**
	 * Holds the title for this program
	 * 
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private String title;
	
	/**
	 * Constructs an empty program object
	 * 
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public Program (){
		this("");
	}
	
	/**
	 * Constructs a program with the given title
	 *
	 * @param title - The title of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public Program (String title){
		this.elements = new ArrayList<ProgramElement>();
		this.title = title;
	}
	
	/**
	 * Adds an element to this program
	 *
	 * @param elem The element to add
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */ 
	public void addElement(ProgramElement elem){
		this.elements.add(elem);
	}	
	
	/**
	 * Removes the given element from this program
	 *
	 * @param elem The element to remove
	 * 
	 * @return If the operation was successful
	 * 
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public boolean removeElement(ProgramElement elem){
		return this.elements.remove(elem);
	}
	
	/**
	 * Returns all of the elements in this program
	 * 
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public List<ProgramElement> getElements(){
		return this.elements;
	}
	
	/**
	 * Returns the title of this program 
	 * 
	 * @return This programs title
	 * 
	 * @author Matthew Maynes
	 * @since November 1, 2014
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * Sets the title of this program
	 * 
	 * @param title - The new title of this program
	 * 
	 * @author Matthew Maynes
	 * @since November 1, 2014
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * Serializes this object into xml form following the Coursinator program in the XML schema.
	 *
	 * @return A valid XML serialized string representing this object
	 *
	 * @since Novmeber 1, 2014
	 * @author Matthew Maynes
	 */
	public String serialize(){
		StringBuffer buffer = new StringBuffer();
		String attrs = this.getTitle() != null && this.getTitle().length() > 0 ? " name=" + this.getTitle(): "";
		buffer.append("<" + SCHEMA_IDENTIFIER + attrs + ">");
		
		for(ProgramElement elem : this.elements){
			buffer.append(elem.serialize());
		}
		
		buffer.append("</" + SCHEMA_IDENTIFIER + ">");
		
		return buffer.toString();
	}
	
	/**
	 * Returns a serialized version of this program object following the Coursinator schema.
	 * 
	 * @return A XML serialized version of this program
	 *
	 * @see #serialize()
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	@Override
	public String toString(){
		return this.serialize();
	}
	
	/**
	 * Reads and parses the given input stream to create a program object. The input stream must 
	 * be in the XML format as defined by the Coursinator schema
	 *
	 * @param stream The input stream to parse
	 *
	 * @throws ParserConfigurationException If the input XML is malformed
	 * @throws IllegalArgumentException 	If the Source is an XML artifact that the implementation cannot validate (for example, a processing instruction).
	 * @throws SAXException 				If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 					If the validator is processing a SAXSource and the underlying XMLReader throws an IOException.
	 */
	public static Program read(InputStream stream) throws IllegalArgumentException, SAXException, IOException, ParserConfigurationException {
		Program program = new Program();
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(stream);
		NodeList nodes = doc.getElementsByTagName("element");
		
		
		for(int i = 0; i < nodes.getLength(); i++){
			program.addElement(ProgramElement.buildElement((Element)nodes.item(i)));
		}
		return program;
	}
	
	/**
	 * Reads an entire file into memory to be parsed. The input file must be in the XML format as 
	 * defined by the Coursinator schema
	 *
	 * @param file The input file to read into a buffer and parse
	 *
	 * @throws ParserConfigurationException If the input XML is malformed
	 * @throws IllegalArgumentException 	If the Source is an XML artifact that the implementation cannot validate (for example, a processing instruction).
	 * @throws SAXException 				If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 					If the validator is processing a SAXSource and the underlying XMLReader throws an IOException.
	 */
	public static Program read(File file) throws IllegalArgumentException, SAXException, IOException, ParserConfigurationException {
		return Program.read(new FileInputStream(file));
	}
		
	/**
	 * Reads an entire string into memory to be parsed. The input string must be in the XML format as 
	 * defined by the Coursinator schema
	 *
	 * @param string The input string to parse
	 *
	 * @throws ParserConfigurationException If the input XML is malformed
	 * @throws IllegalArgumentException 	If the Source is an XML artifact that the implementation cannot validate (for example, a processing instruction).
	 * @throws SAXException 				If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 					If the validator is processing a SAXSource and the underlying XMLReader throws an IOException.
	 */
	public static Program read(String string) throws IllegalArgumentException, SAXException, IOException, ParserConfigurationException {
		return Program.read(new InputSource(new StringReader(string)).getByteStream());
	}

}
