package cr.factory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cr.Program;
import xml.XMLBuilder;

/**
 * ProgramBuilder is a factory class that builds Programs out of XML data that follows the Coursinator schema
 * 
 * @since November 9, 2014
 * @version 0.1.0
 */
public class ProgramBuilder extends XMLBuilder<Program> {

	/**
	 * Builds a program using the data contained in the given xml node
	 *
	 * @param node An XML node that contains the required information about this program
	 *
	 * @return a new program representing the data in the given node
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public Program buildObject(Element node) {
		Program program = new Program();
		ProgramElementBuilder builder = new ProgramElementBuilder();
		NodeList nodes = node.getElementsByTagName("element");;
		
		for(int i = 0; i < nodes.getLength(); i++){
			program.addElement(builder.buildObject((Element)nodes.item(i)));
		}
		return null;
	}

	/**
	 * Returns the target object schema identifier for proper deserialization
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public String getSchemaIdentifier() {
		return Program.SCHEMA_IDENTIFIER;
	}

	/**
	 * Returns an empty array of Programs for proper casting in XMLBuilder
	 *
	 * @see #read(NodeList)
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public Program[] getEmptyArray() {
		return new Program[0];
	}
	

}
