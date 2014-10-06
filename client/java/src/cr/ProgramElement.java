/**
 * ProgramElement
 *
 * Defines a program element 
 * 
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 */
 
import org.w3c.dom.Node;


public class ProgramElement{

// 	private Course course;
	
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
		//this.course = new Course();
	}
	
	
	
	/**
	 * Builds a program element using the data contained in 
	 * the given xml node
	 *
	 * @param node - An XML node that contains the required information about this program
	 *
	 * @return a new program element representing the data in the given node
	 */
	public static ProgramElement buildElement(Node node){
		return null;
	}

}