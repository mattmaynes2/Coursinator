package test.cr;

import static org.junit.Assert.*;

import org.junit.Test;

import cr.Course;

/**
 * Tests the Course class to ensure serialization is working properly and the object is functioning as expected
 * 
 * @since Novermber 9, 2014
 * @version 0.1.0
 */
public class CourseTest {


	@Test
	public void testCode(){
		String expected = "<course><code>ABCD1234</code></course>";
		Course course = new Course();
		course.setCode("ABCD 1234");		
		assertEquals(expected, course.serialize());
	}
	
	@Test
	public void testTitle(){
		String expected = "<course><title>ABC</title></course>";
		Course course = new Course();
		course.setTitle("ABC");
		assertEquals(expected, course.serialize());
	}
	
	@Test
	public void testLevel(){
		String expected = "<course><level>undergrad</level></course>";
		Course course = new Course();
		course.setLevel("undergrad");
		assertEquals(expected, course.serialize());	
	}
	
	@Test
	public void testDescription(){
		String expected = "<course><desc>test</desc></course>";
		Course course = new Course();
		course.setDescription("test");
		assertEquals(expected, course.serialize());			
	}
	
	@Test
	public void testSpan(){
		String expected = "<course><term-span>2</term-span></course>";
		Course course = new Course();
		course.setSpan(2);
		assertEquals(expected, course.serialize());			
	}
		
}
