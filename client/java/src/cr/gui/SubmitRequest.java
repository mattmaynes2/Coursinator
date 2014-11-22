package cr.gui;

import cr.Course;
import cr.Program;

import java.util.ArrayList;


public class SubmitRequest {
	
	private boolean onPattern;
	
	private ArrayList<Course> completedCourses;
	
	private Program program;

	private int year;
	
	public SubmitRequest(){
		this(false, null);
	}
	
	public SubmitRequest(boolean onPattern, Program program){
		this.onPattern = onPattern;
		this.program = program;
		this.completedCourses = new ArrayList<Course>();
	}
	
	
	/**
	 * @return the onPattern
	 */
	public boolean isOnPattern() {
		return onPattern;
	}
	

	/**
	 * @param course the course to add
	 */
	public void addCompletedCourse(Course course){
		this.completedCourses.add(course);
	}
	
	/**
	 * @return the completedCourses
	 */
	public ArrayList<Course> getCompletedCourses() {
		return completedCourses;
	}

	/**
	 * @return the program
	 */
	public Program getProgram() {
		return program;
	}

	public int getYear(){
		return this.year;
	}
	
	/**
	 * @param onPattern the onPattern to set
	 */
	public void setOnPattern(boolean onPattern) {
		this.onPattern = onPattern;
	}

	/**
	 * @param completedCourses the completedCourses to set
	 */
	public void setCompletedCourses(ArrayList<Course> completedCourses) {
		this.completedCourses = completedCourses;
	}

	/**
	 * @param program the program to set
	 */
	public void setProgram(Program program) {
		this.program = program;
	}
	
	public void setYear(int year){
		this.year = year;
	}
	
	@Override
	public String toString(){
		String s = "{" + this.program.getTitle();
		s = s + ", " + this.onPattern;
		s = s + ", " + this.year;
		return s + "}";
	}
	

}
