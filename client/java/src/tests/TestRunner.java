/**
 * TestRunner
 * 
 * Runs all of the tests for this program 
 *
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 */
package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;



//import java.util.ArrayList;
import cr.*;

public class TestRunner{

	

	public static void main(String[] args){
//		ArrayList<Test> tests = new ArrayList<Test>();
//		tests.add(new TestCRXML());
//	
//		for(Test t: tests){
//			if(!t.runTests()){
//				System.out.println("Test Failed: " + t.getName() );
//				System.exit(0);
//			}
//		}
//		System.out.println("All tests passed");
		CRRequest req = new CRRequest();
		try {
			Course[] courses = req.getCourses("SYSC");
			for(Course c : courses){
				System.out.println(c);
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
//		
//		try{
//			Course[] courses = Course.read(new FileInputStream(new File("test.xml")));
//			for(Course c : courses){
//				System.out.println(c);
//			}
//		} catch (IOException | ParserConfigurationException | SAXException e) {
//			e.printStackTrace();
//		}
		
		
	}

}