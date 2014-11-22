package cr;

import java.util.List;
import java.util.ArrayList;

import xml.XMLObject;

/**
 * Program
 *
 * Defines a program structure via its elements and courses
 * 
 * @version 0.0.1
 * @since October 6, 2014
 */
public class Program extends XMLObject{

	/**
	 * The Coursinator XML identifier tag
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public static final String SCHEMA_IDENTIFIER = "program";
	
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
	 * Stores the id of this program 
	 * 
	 * @author Matthew Maynes
	 * @since November 22, 2014
	 */
	private int id;
	
	/**
	 * Stores the year of the program 
	 * 
	 * @author Matthew Maynes
	 * @since November 22, 2014
	 */
	private int year;
	
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
	 * Returns the year of this program 
	 * 
	 * @return The year that this program started
	 */
	public int getYear(){
		return this.year;
	}
	
	/**
	 * Returns the id of this program
	 * 
	 * @return This programs unique identifier
	 */
	public int getId(){
		return this.id;
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
	 * Sets the year for this program
	 * 
	 * @param year This program year
	 */
	public void setYear(int year){
		this.year = year;
	}
	
	/**
	 * Sets the id of this program
	 * 
	 * @param id The new unique identifier for this programs
	 */
	public void setId(int id){
		this.id = id;
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
	

}
