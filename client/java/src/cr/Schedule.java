package cr;

import java.util.ArrayList;

import xml.XMLObject;

public class Schedule extends XMLObject{
	
	/**
	 * The Coursinator XML identifier tag
	 */
	public static final String SCHEMA_IDENTIFIER = "sections";
	
	public static final int TIME_SLOTS = 26;
	public static final int START_TIME = 830;
	public static final int TIME_INC = 30;
	
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
	
	public CourseOffering[] getCourseOfferingByBlock(){
		ArrayList<CourseOffering> blocks = new ArrayList<CourseOffering>();
		CourseOffering block;
		for(CourseOffering c : this.offerings){
			for(int i = 0; i < span(c.getStartTime(), c.getEndTime()); i++){
				block = new CourseOffering(c);
				block.setStartTime(trueTime(roundStart(c.getStartTime()), i));
				block.setEndTime(trueTime(roundEnd(c.getStartTime()), i));
				blocks.add(block);
			}
		}
		
		return blocks.toArray(new CourseOffering[0]);
	}
	
	public int trueTime(int base, int offset){
		int time = base;
		String buffer = Integer.toString(base);
		for(int i = 0; i < offset; i++){
			if(buffer.charAt(buffer.length() - 2) == '3')				
				time += i % 2 == 0 ? 70 : 30;
			else
				time += i % 2 == 0 ? 30 : 70;
		}
		return time;
	}
	
	public int roundStart(int time){
		return time - 5;
	}
	
	public int roundEnd(int time){
		String buffer = Integer.toString(time);
		if(buffer.charAt(buffer.length() - 2) == '2')
			return time + 5;
		else if(buffer.charAt(buffer.length() - 2) == '3')
			return time + 65;
		else if(buffer.charAt(buffer.length() - 2) == '0')
			return time + 25;
		return time + 45;
	}
	
	public int span(int start, int end){
		int c = 0;
		int base = roundStart(start);
		int term = roundEnd(end);
		while(trueTime(base , c) < term){
			c++;
		}
		return c;
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
