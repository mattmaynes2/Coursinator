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
 
 
import cr.*;

public class TestCRXML implements Test{
	
	
	private boolean testValidation(){
		try{
			File xml = new File("cr_test_1.xml");
			File xsd = new File("program.xsd");
			Program p = new Program();
			p.validate(xsd, xml);
		} catch(Exception e){
			System.out.println(e.message);
		}
		return true;
	}	
						
	public boolean runTest(){
		
		
		return testValidation();	
		
	}	
	
	public String getName(){
		return "TestCRXML.java";
	}

}