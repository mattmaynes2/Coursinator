/**
 * TestCRXML
 *
 * Tests the CR package for its XML parsing abilities
 *
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 *
 */
 
package tests;
 
import cr.*;
import java.io.File;

public class TestCRXML implements Test{
	
	
	private boolean testValidation(){
		try{
			File xml = new File("cr_test_1.xml");
			File xsd = new File("program.xsd");
			Program p = new Program();
			p.validateXML(xsd, xml);
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
		return true;
	}	
						
	public boolean runTests(){
		
		
		return testValidation();	
		
	}	
	
	public String getName(){
		return "TestCRXML.java";
	}

}