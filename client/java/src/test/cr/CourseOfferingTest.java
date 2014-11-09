package test.cr;

import static org.junit.Assert.*;

import org.junit.Test;

import cr.CourseOffering;

public class CourseOfferingTest {

	@Test
	public void testId(){
		String expected = "<course-offering><id>123</id></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setId(123);		
		assertEquals(expected, CourseOffering.serialize());
	}
	
	@Test
	public void testCode(){
		String expected = "<course-offering><code>ABCD1234</code></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setCode("ABCD 1234");		
		assertEquals(expected, CourseOffering.serialize());
	}
	
	@Test
	public void testYear(){
		String expected = "<course-offering><year>2</year></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setYear(2);
		assertEquals(expected, CourseOffering.serialize());
	}
	
	@Test
	public void testSection(){
		String expected = "<course-offering><section>A</section></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setSection("A");
		assertEquals(expected, CourseOffering.serialize());	
	}
	
	@Test
	public void testTerm(){
		String expected = "<course-offering><term>1</term></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setTerm(1);
		assertEquals(expected, CourseOffering.serialize());			
	}
	
	@Test
	public void testDays(){
		String expected = "<course-offering><days>MW</days></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setDays("MW");
		assertEquals(expected, CourseOffering.serialize());			
	}
	
	@Test
	public void testEnrolled(){
		String expected = "<course-offering><enrolled>20</enrolled></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setEnrolled(20);
		assertEquals(expected, CourseOffering.serialize());			
	}
	
	@Test
	public void testCapacity(){
		String expected = "<course-offering><capacity>20</capacity></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setCapacity(20);
		assertEquals(expected, CourseOffering.serialize());			
	}
	
	@Test
	public void testType(){
		String expected = "<course-offering><type>1</type></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setType(1);
		assertEquals(expected, CourseOffering.serialize());			
	}
	
	@Test
	public void testRoom(){
		String expected = "<course-offering><room>UC123</room></course-offering>";
		CourseOffering CourseOffering = new CourseOffering();
		CourseOffering.setRoom("UC123");
		assertEquals(expected, CourseOffering.serialize());			
	}

}
