package cr;
/**
 * ProgramElement
 *
 * Defines a program element 
 * 
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 */

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ProgramElement{

	/**
	 * Stores the course data for this program element
	 */
	private Course course;
	
	/**
	 * Indicates the term in which this course is offered
	 */
	private int term;
	
	/**
	 * The year that this course is offered
	 */
	private int year;
	
	
	public ProgramElement(){
		this.term = 0;
		this.year = 0;
		this.course = new Course();
	}
	
	
	public Course getCourse(){
		return this.course;
	}
	
	public int getTerm(){
		return this.term;
	}
	
	public int getYear(){
		return this.year;
	}
	
	public void setCourse(Course course){
		this.course = course;
	}
	
	public void setTerm(int term){
		this.term = term;
	}
	
	public void setYear(int year){
		this.year = year;
	}
	
	public String serialize(){
		StringBuffer buffer = new StringBuffer();
		
		
		return buffer.toString();
	}
	
	@Override
	public String toString(){
		return "<ProgramElement> Year: " + this.year + " Term: " + this.term + "{Course: " + this.course.toString() + "}";
	}
	
	/**
	 * Builds a program element using the data contained in 
	 * the given xml node
	 *
	 * @param node - An XML node that contains the required information about this program
	 *
	 * @return a new program element representing the data in the given node
	 */
	public static ProgramElement buildElement(Element node){
		ProgramElement elem = new ProgramElement();
		NodeList nodes;
		
		nodes = node.getElementsByTagName("course");
		if(nodes.getLength() > 0){
			Course[] courses = Course.read(nodes);
			if(courses.length > 0) elem.setCourse(courses[0]);
		}
		
		nodes = node.getElementsByTagName("term");
		if(nodes.getLength() > 0) elem.setTerm(Integer.parseInt(nodes.item(0).getNodeValue()));
		
		nodes = node.getElementsByTagName("year");
		if(nodes.getLength() > 0) elem.setYear(Integer.parseInt(nodes.item(0).getNodeValue()));
		
		return elem;
	}

}
