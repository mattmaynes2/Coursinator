package cr.factory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cr.Course;
import xml.XMLBuilder;

/**
 * CourseBuilder is a factory class that builds Courses out of XML data that follows the Coursinator schema
 * 
 * @since November 9, 2014
 * @version 0.1.0
 */
public class CourseBuilder extends XMLBuilder<Course>{

	/**
	 * Builds a course given the data in the passed node
	 *
	 * @param node The node to parse
	 *
	 * @return A new course that represents the data in the given node
	 *
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	public Course buildObject(Element node){
		Course course = new Course();
		NodeList nodes;
	
		nodes = node.getElementsByTagName("code");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null)
			course.setCode(nodes.item(0).getTextContent());
		
		nodes = node.getElementsByTagName("title");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null) 
			course.setTitle(nodes.item(0).getTextContent());
		
		nodes = node.getElementsByTagName("term-span");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null) 
			course.setSpan(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("level");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null) 
			course.setLevel(nodes.item(0).getTextContent());
		
		nodes = node.getElementsByTagName("desc");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null) 
			course.setDescription(nodes.item(0).getTextContent());
		
		if(course.getCode() == null || course.getCode().length() == 0){
			course.setCode(node.getTextContent());
		}
		
		return course;	
	}
	
	
	/**
	 * Returns the target object schema identifier for proper deserialization
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public String getSchemaIdentifier() {
		return Course.SCHEMA_IDENTIFIER;
	}

	
	/**
	 * Returns an empty array of Courses for proper casting in XMLBuilder
	 *
	 * @see #read(NodeList)
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public Course[] getEmptyArray(){
		return new Course[0];
	}
	
}
