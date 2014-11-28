package cr.factory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cr.CourseOffering;
import xml.XMLBuilder;

/**
 * CourseOfferingBuilder is a factory class that builds CourseOfferings out of XML data that follows the Coursinator schema
 * 
 * @since November 9, 2014
 * @version 0.1.0
 */
public class CourseOfferingBuilder extends XMLBuilder<CourseOffering> {

	/**
	 * Builds this course offering given the data in the passed node
	 *
	 * @param node The node to parse
	 *
	 * @return This course offering that represents the data in the given node
	 *
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	@Override
	public CourseOffering buildObject(Element node){
		CourseOffering offering = new CourseOffering();
		NodeList nodes;
	
		nodes = node.getElementsByTagName("id");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setId(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("code");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals(""))
			offering.setCode(nodes.item(0).getTextContent());
		
		nodes = node.getElementsByTagName("year");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setYear(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("section");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setSection(nodes.item(0).getTextContent());
		
		nodes = node.getElementsByTagName("term");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setTerm(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("days");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setDays(nodes.item(0).getTextContent());
		
		nodes = node.getElementsByTagName("enrolled");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setEnrolled(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("capacity");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setCapacity(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("room");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setRoom(nodes.item(0).getTextContent());
		
		nodes = node.getElementsByTagName("type");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setType(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("start_time");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setStartTime(Integer.parseInt(nodes.item(0).getTextContent()));
		
		nodes = node.getElementsByTagName("end_time");
		if(nodes.getLength() > 0 && nodes.item(0).getTextContent() != null && !nodes.item(0).getTextContent().equals("")) 
			offering.setEndTime(Integer.parseInt(nodes.item(0).getTextContent()));
		
		return offering;	
	}

	/**
	 * Returns the target object schema identifier for proper deserialization
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public String getSchemaIdentifier() {
		return CourseOffering.SCHEMA_IDENTIFIER;
	}

	/**
	 * Returns an empty array of courseOfferings for proper casting in XMLBuilder
	 *
	 * @see #read(NodeList)
	 * 
	 * @author Matthew Maynes
	 * @since November 9, 2014
	 */
	@Override
	public CourseOffering[] getEmptyArray(){
		return new CourseOffering[0];
	}
	
}
