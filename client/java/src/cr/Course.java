package cr;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Course
 *
 * Defines a course object following the Coursinator schema
 * 
 * @version 0.1.0
 * @since October 6, 2014
 */
public class Course extends XMLObject{

	/**
	 * The Coursinator XML identifier tag
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public static final String SCHEMA_IDENTIFIER = "course";

	/**
	 * The code of this course in normalized form (ex. SYSC4504)
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private String code;
	
	/**
	 * The title of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private String title;
	
	/**
	 * The level that this course is offered at
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	private String level;
	
	/**
	 * A short description of this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	private String description;
	
	/**
	 * The terms that this course spans
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	private int span;
	
	/**
	 *  Initializes an empty Course object
	 *  
	 *  @author Matthew Maynes
	 *  @since November 1, 2014
	 */
	public Course(){
		this.code = "";
		this.title = "";
		this.level = "";
		this.description = "";
		this.span = 0;
	}
	
	/**
	 * Returns the code of this course
	 *
	 * @return the code of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public String getCode(){
		return this.code;
	}
	
	/**
	 * Returns the title of this course
	 *
	 * @return the title of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * Returns the level of this course
	 *
	 * @return the level of this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public String getLevel(){
		return this.level;
	}
	
	/**
	 * Returns the description of this course
	 *
	 * @return the description of this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * Returns the number of terms this course spans
	 * 
	 * @return the term span of this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public int getSpan(){
		return this.span;
	}
	
	/**
	 * Sets the course code for this course and normalizes any invalid whitespace
	 *
	 * @param code The new code for this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public void setCode(String code){
		if(code != null && code.contains(" ")){
			this.code = code.replace(" ", "");	
		}
		else { 
			this.code = code;
		}
	}
	
	/**
	 * Sets the title for this course
	 *
	 * @param title The new title for this course
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * Sets the level for this course
	 *
	 * @param level The new level for this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public void setLevel(String level){
		this.level = level;
	}
	
	/**
	 * Sets the description for this course
	 *
	 * @param description The new description for this course
	 *
	 * @since November 1, 2014
	 * @author Matthew Maynes
	 */
	public void setDescription(String description){
		this.description = description;
	}	
	
	/**
	 * Sets the term span for this course
	 *
	 * @param span The number of terms this course spans
	 *
	 * @since October 6, 2014
	 * @author Matthew Maynes
	 */
	public void setSpan(int span){
		this.span = span;
	}
		
	/**
	 * Serializes this object into xml form following the Coursinator course in the XML schema.
	 *
	 * @return A valid XML serialized string representing this object
	 *
	 * @since October 31, 2014
	 * @author Matthew Maynes
	 */	
	public String serialize(){
		StringBuffer buffer = new StringBuffer();
		HashMap<String, String> schema = new HashMap<String, String>();
		schema.put("code", this.getCode());
		schema.put("title", this.getTitle());
		schema.put("term-span", Integer.toString(this.getSpan()));
		schema.put("level", this.getLevel());
		schema.put("desc", this.getDescription());
		
		// Build the XML output
		buffer.append("<" + SCHEMA_IDENTIFIER + ">");
		for(Entry<String, String> element : schema.entrySet()){
			if(element.getValue() != null){
				buffer.append("<" + element.getKey() + ">" + element.getValue() + "</" + element.getKey() + ">");
			}
		}
		
		buffer.append("</" + SCHEMA_IDENTIFIER + ">");
		return buffer.toString();
	}	

	
}
