package cr;
/**
 * Program
 *
 * Defines a program structure via its elements and courses
 * 
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 */

import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.XMLConstants;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.File; 
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;


public class Program{

	/**
	 * Stores all of the elements that are part of this program
	 */
	private List<ProgramElement> elements;

	/**
	 * Holds the title for this program
	 */
	private String title;
	
	/**
	 * The file containing the XML schema for validating input data
	 *
	 * @see read
	 */
	private File xsd;
	
	/**
	 * Constructs an empty program object
	 */
	public Program (){
		this("");
	}
	
	/**
	 * Constructs a program with the given title
	 *
	 * @param title - The title of this course
	 */
	public Program (String title){
		this.elements = new ArrayList<ProgramElement>();
		this.title = title;
		this.xsd = new File("program.xsd");
	}

	/**
	 * Constructs a program given the data from the input file. The input
	 * file needs to be in XML format as defined by Program.xsd 
	 *
	 * @throws ParserConfigurationException - If the input XML is malformed
	 * @throws IllegalArgumentException 	- If the Source is an XML artifact that the implementation cannot validate (for example, a processing instruction).
	 * @throws SAXException 				- If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 					- If the validator is processing a SAXSource and the underlying XMLReader throws an IOException.
	 * @throws NullPointerException 		- If file is null.

	 */
	public Program(File file) throws IllegalArgumentException, SAXException, IOException, NullPointerException, ParserConfigurationException{
		this();
		this.read(file);	
	}

	
	/**
	 * Adds an element to this program
	 *
	 * @param elem - The element to add
	 */ 
	public void addElement(ProgramElement elem){
		this.elements.add(elem);
	}	
	
	/**
	 * Removes the given element from this program
	 *
	 * @param elem - The element to remove
	 * @return If the operation was successful
	 */
	public boolean removeElement(ProgramElement elem){
		return this.elements.remove(elem);
	}
	
	/**
	 * Returns all of the elements in this program
	 */
	public List<ProgramElement> getElements(){
		return this.elements;
	}
	
	/**
	 * Sets the path to the xsd file
	 */
	public void setXSD(String path){
		this.xsd = new File(path);
	}
	
	
	/**
	 * Reads an entire stream into memory to be parsed. The input
	 * file must be in the XML format as defined by Program.xsd. 
	 *
	 * IMPORTANT: The path for Program.xsd must be set before running this function 
	 *
	 * @param file - The input file to read in an buffer
	 *
	 * @throws ParserConfigurationException - If the input XML is malformed
	 * @throws IllegalArgumentException 	- If the Source is an XML artifact that the implementation cannot validate (for example, a processing instruction).
	 * @throws SAXException 				- If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 					- If the validator is processing a SAXSource and the underlying XMLReader throws an IOException.
	 * @throws NullPointerException 		- If file is null.
	 */
	public void read(File file) throws IllegalArgumentException, SAXException, IOException, NullPointerException, ParserConfigurationException {
		this.validateXML(this.xsd, file);
	
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc 			= builder.parse(file);
		NodeList nodes 			= doc.getElementsByTagName("element");
		
		
		for(int i = 0; i < nodes.getLength(); i++){
			this.elements.add(ProgramElement.buildElement((Element)nodes.item(i)));
		}
	}
	
	/**
	 * Validates the input xml against the predefined Program.xsd schema
	 *
	 * @param xsd - The schema file to test
	 * @param xml - The input XML file to test
	 *
	 * @return If the given file is valid or not
	 *
	 * @throws IllegalArgumentException - If the Source is an XML artifact that the implementation cannot validate (for example, a processing instruction).
	 * @throws SAXException 			- If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 				- If the validator is processing a SAXSource and the underlying XMLReader throws an IOException.
	 * @throws NullPointerException 	- If file is null.
	 */
	public void validateXML(File xsd, File xml) throws IllegalArgumentException, SAXException, IOException, NullPointerException{
        SchemaFactory factory 	= SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema 			= factory.newSchema(new StreamSource(new FileInputStream(xsd)));
        Validator validator 	= schema.newValidator();
        
        validator.validate(new StreamSource(new FileInputStream(xml)));
	}
		
	

}
