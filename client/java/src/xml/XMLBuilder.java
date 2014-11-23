package xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * An XMLBuilder is a factory class that constructs an object using XML data from an XMLElement
 * that corresponds to this objects schema. Objects are built using the template method pattern 
 * which relies on {@link #buildObject(Element)} to return a properly constructed version of 
 * object of type E.
 * 
 * @param E The type of class this builder will be constructing
 * 
 * @version 0.1.0
 * @since November 9, 2014
 */
public abstract class XMLBuilder<E extends XMLObject>{

	/**
	 * Given an XML list of nodes that comply to objects in the Coursinator schema, 
	 * parse each node and build an array of objects of type E
	 * 
	 * @param nodes A list of xml objects that represent this object
	 * 
	 * @return An array of E objects that represent the given XML data
	 * 
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public E[] read(NodeList nodes){
		ArrayList<E> objects = new ArrayList<E>();
		for(int i = 0; i < nodes.getLength(); i++){
			objects.add(this.buildObject((Element)nodes.item(i)));
		}
		return objects.toArray(this.getEmptyArray());
	}
	
	/**
	 * Given XML data in an input stream, buffer and deserialize it to create an array of E objects.
	 * 
	 * @param stream The stream to read form and deserialize
	 * 
	 * @return An array of E objects parsed from the XML data
	 * 
	 * @throws ParserConfigurationException If the input XML is malformed
	 * @throws SAXException If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 	If there is an error with the input stream. 
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public E[] read(InputStream stream) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc 			= builder.parse(new InputSource(stream));
		NodeList nodes 			= doc.getElementsByTagName(this.getSchemaIdentifier());
		if(nodes.getLength() == 0)
			nodes = doc.getChildNodes();
		
		return read(nodes);
	}
	
	/**
	 * Reads the given file and parses it using the Coursinator XML schema to generate an array of
	 * E objects.
	 * 
	 * @param file The XML file to read
	 * 
	 * @return An array of E objects parsed from the XML data
	 * 
	 * @throws ParserConfigurationException If the input XML is malformed
	 * @throws SAXException If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 	If there is an error with the input stream. 
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public E[] read(File file) throws ParserConfigurationException, SAXException, IOException{
		return read(new FileInputStream(file));
	}
	
	/**
	 * Reads the given string and parses it using the Coursinator XML schema to generate an array of
	 * E objects.
	 * 
	 * @param data An XML string of data that complies with the Coursinator XML schema
	 * 
	 * @return An array of E object parsed from the XML data
	 * 
	 * @throws ParserConfigurationException If the input XML is malformed
	 * @throws SAXException If the ErrorHandler throws a SAXException or if a fatal error is found and the ErrorHandler returns normally.	
	 * @throws IOException 	If there is an error with the input stream. 
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public E[] read(String data) throws ParserConfigurationException, SAXException, IOException{
		return read(new InputSource(new StringReader(data)).getByteStream());
	}
	
	
	/**
	 * Builds an object given the data in the passed node. This function should be overridden in
	 * subclasses and should return the correct class type.
	 *
	 * @param node The node to parse
	 *
	 * @return A new XMLObject that is represented by the node data
	 *
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	public abstract E buildObject(Element node);
	
	/**
	 * Returns the schema identifier for this object for parsing
	 * 
	 * @return This objects XML schema identifier tag name
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	public abstract String getSchemaIdentifier();

	
	/**
	 * Returns an empty array of the E type for this class so that the read functions can return the correct type
	 * 
	 * @return An empty array of this classes generic type
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	public abstract E[] getEmptyArray();
}
