package cr;

import java.util.ArrayList;

import xml.XMLObject;

public class Schedule extends XMLObject{
	
	/**
	 * The Coursinator XML identifier tag
	 */
	public static final String SCHEMA_IDENTIFIER = "sections";
	
	/**
	 * Stores the offerings for this schedule
	 */
	private ArrayList<CourseOffering> offerings;
	
	public Schedule(){
		offerings = new ArrayList<CourseOffering>();
	}
	
	/**
	 * @param offering The offering to add to this schedule
	 */
	public void addCourseOffering(CourseOffering offering){
		this.offerings.add(offering);
	}
	
	public void addCourseOfferings(CourseOffering[] offerings){
		for(CourseOffering c: offerings){
			this.offerings.add(c);
		}
	}
	
	
	/**
	 * @return All of the offerings in this schedule
	 */
	public CourseOffering[] getCourseOfferings(){
		return offerings.toArray(new CourseOffering[0]);
	}

	@Override
	public String serialize() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<sections>");
		for(CourseOffering off : offerings){
			buffer.append(off.serialize());
		}
		return buffer.toString() + "</sections>";
	}
	
	

}
