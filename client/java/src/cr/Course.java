/**
 * Course
 *
 * Defines a course 
 * 
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 */
 
 

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Course{

	/**
	 * The code of this course in normalized form (ex. SYSC4504)
	 */
	private String code;
	
	/**
	 * The title of this course
	 */
	private String title;
	
	/**
	 * The terms that this course spans
	 */
	private int span;
	
	
	public String getCode(){
		return this.code;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getSpan(){
		return this.span;
	}
	
	public void setCode(String code){
		if(code.contains(" ")){
			this.code = code.replace(" ", "");	
		}
		else { 
			this.code = code;
		}
	}
	
	
	public void setTitle(String title){
		this.title = title;
	}	
	
	public void setSpan(int span){
		this.span = span;
	}
	
	@Override
	public String toString(){
		return "<Course: " + this.code + "> Span: " + this.span + " [Title: " + this.title + "]";
	}
	
	/**
	 * Builds a course given the data in the passed node
	 *
	 * @param node - The node to parse
	 *
	 * @return A new course that represents the data in the given node
	 */
	public static Course buildCourse(Element node){
		Course course = new Course();
		NodeList nodes;
		
		nodes = node.getElementsByTagName("code");
		if(nodes.getLength() > 0) course.setCode(nodes.item(0).getNodeValue());
		
		nodes = node.getElementsByTagName("title");
		if(nodes.getLength() > 0) course.setTitle(nodes.item(0).getNodeValue());
		
		nodes = node.getElementsByTagName("term_span");
		if(nodes.getLength() > 0) course.setSpan(Integer.parseInt(nodes.item(0).getNodeValue()));
		
		return course;
		
	}
	
	
}