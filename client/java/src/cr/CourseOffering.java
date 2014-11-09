package cr;

import java.util.HashMap;
import java.util.Map.Entry;

import xml.XMLObject;

/**
 * Course
 *
 * Defines a course offering object following the Coursinator schema
 * 
 * @version 0.0.1
 * @since November 9, 2014
 */
public class CourseOffering extends XMLObject{

	/**
	 * The Coursinator XML identifier tag
	 *
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	public static final String SCHEMA_IDENTIFIER = "course-offering";
	
	
	/**
	 * The ID of the offering. This integer does not correspond to the code of the course
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private int id;
	
	/**
	 * This is the course code. This can be used when referencing the courses
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private String code;
	
	/**
	 * The year this course is offered for (i.e. 1 for 1st, 2 for 2nd etc)
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private int year;
	
	/**
	 * The course section of this offering 
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private String section;
	
	/**
	 * The term for this course, either fall, winter or summer
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private int term;
	
	/**
	 * The days of the week that this course is being offered on
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private String days;
	
	/**
	 * The number of students that are already enrolled in this course
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private int enrolled;
	
	/**
	 * The maximum capacity of this course. A student should not be able to enroll in this course
	 * if the enrolled value is equal to the capacity
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private int capacity;
	
	/**
	 * A room identifier for where this course will be held
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private String room;
	
	/**
	 * The type of this course offering, either a lecture, laboratory, discussion group, etc.
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	private int type;

	
	/**
	 * Constructs an empty course offering 
	 * 
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	public CourseOffering(){
		this.id = -1;
		this.year = -1;
		this.term = -1;
		this.enrolled = -1;
		this.capacity = -1;
		this.type = -1;
		this.code = "";
		this.days = "";
		this.room = "";		
	}
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @return the term
	 */
	public int getTerm() {
		return term;
	}

	/**
	 * @return the days
	 */
	public String getDays() {
		return days;
	}

	/**
	 * @return the enrolled
	 */
	public int getEnrolled() {
		return enrolled;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the course code for this course and normalizes any invalid whitespace
	 *
	 * @param code The new code for this course
	 *
	 * @since November 9, 2014
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
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @param section the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(int term) {
		this.term = term;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(String days) {
		this.days = days;
	}

	/**
	 * @param enrolled the enrolled to set
	 */
	public void setEnrolled(int enrolled) {
		this.enrolled = enrolled;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	
	@Override
	public String serialize() {
		StringBuffer buffer = new StringBuffer();
		HashMap<String, String> schema = new HashMap<String, String>();
		schema.put("code", this.getCode());
		schema.put("section", this.getSection());
		schema.put("days", this.getDays());
		schema.put("room", this.getRoom());
		
		if(this.getId() >= 0) schema.put("id", Integer.toString(this.getId()));
		if(this.getYear() >= 0) schema.put("year", Integer.toString(this.getYear()));
		if(this.getTerm() >= 0) schema.put("term", Integer.toString(this.getTerm()));
		if(this.getType() >= 0) schema.put("type",  Integer.toString(this.getType()));
		if(this.getEnrolled() >= 0) schema.put("enrolled",  Integer.toString(this.getEnrolled()));
		if(this.getCapacity() >= 0) schema.put("capacity",  Integer.toString(this.getCapacity()));
		
		// Build the XML output
		buffer.append("<" + SCHEMA_IDENTIFIER + ">");
		for(Entry<String, String> element : schema.entrySet()){
			if(element.getValue() != null && element.getValue() != ""){
				buffer.append("<" + element.getKey() + ">" + element.getValue() + "</" + element.getKey() + ">");
			}
		}
		
		buffer.append("</" + SCHEMA_IDENTIFIER + ">");
		return buffer.toString();
	}
		
	
}
