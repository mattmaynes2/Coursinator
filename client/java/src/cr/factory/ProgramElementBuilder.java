package cr.factory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cr.Course;
import cr.ProgramElement;
import xml.XMLBuilder;

/**
 * ProgramElementBuilder is a factory class that builds ProgramElements out of XML data that follows the Coursinator schema
 * 
 * @since November 9, 2014
 * @version 0.1.0
 */
public class ProgramElementBuilder extends XMLBuilder<ProgramElement> {
	
	/**
	 * Builds a program element using the data contained in the given xml node
	 *
	 * @param node An XML node that contains the required information about this program
	 *
	 * @return a new program element representing the data in the given node
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public ProgramElement buildObject(Element node){
		ProgramElement elem = new ProgramElement();
		CourseBuilder builder = new CourseBuilder();
		NodeList nodes;
		
		nodes = node.getElementsByTagName("course");
		if(nodes.getLength() > 0){
			Course[] courses = builder.read(nodes);
			if(courses.length > 0) elem.setCourse(courses[0]);
		}
		
		nodes = node.getElementsByTagName("term");
		if(nodes.getLength() > 0) elem.setTerm(Integer.parseInt(nodes.item(0).getNodeValue()));
		
		nodes = node.getElementsByTagName("year");
		if(nodes.getLength() > 0) elem.setYear(Integer.parseInt(nodes.item(0).getNodeValue()));
		
		return elem;
	}

	/**
	 * Returns the target object schema identifier for proper deserialization
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public String getSchemaIdentifier() {
		return ProgramElement.SCHEMA_IDENTIFIER;
	}

	/**
	 * Returns an empty array of ProgramElements for proper casting in XMLBuilder
	 *
	 * @see #read(NodeList)
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public ProgramElement[] getEmptyArray(){
		return new ProgramElement[0];
	}
	
}
