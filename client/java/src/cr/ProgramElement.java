package cr;

import java.util.HashMap;
import java.util.Map.Entry;

import xml.XMLObject;

/**
 * Defines a program element object and its attributes. This class is a serializable object representing a 
 * program element in the Coursinator schema
 * 
 * @version 0.0.1
 * @since October 6, 2014
 */
public class ProgramElement extends XMLObject{

	/**
	 * The Coursinator XML identifier tag
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public static final String SCHEMA_IDENTIFIER = "program";
	
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
	
	/**
	 * Serializes this object into an xml form following the Coursinator schema
	 * 
	 * @author Matthew Maynes
	 * @since October 6, 2014
	 */
	@Override
	public String serialize(){
		StringBuffer buffer = new StringBuffer();
		HashMap<String, String> schema = new HashMap<String, String>();
		schema.put("year", Integer.toString(this.getYear()));
		schema.put("term", Integer.toString(this.getTerm()));
		schema.put("course", this.getCourse().serialize());
		
		buffer.append("<" + SCHEMA_IDENTIFIER + ">");
		for(Entry<String, String> element : schema.entrySet()){
			if(element.getValue() != null){
				buffer.append("<" + element.getKey() + ">" + element.getValue() + "</" + element.getKey() + ">");
			}
		}
		
		buffer.append("</" + SCHEMA_IDENTIFIER + ">");
		
		return buffer.toString();
	}
	
	
	/**
	 * Returns a String representation of this object
	 */
	@Override
	public String toString(){
		return this.course.toString();
	}

}
