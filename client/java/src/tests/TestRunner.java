/**
 * TestRunner
 * 
 * Runs all of the tests for this program 
 *
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 */

import java.util.ArrayList;

public class TestRunner{

	

	public static void main(String[] args){
		ArrayList<Test> tests = new ArrayList<Test>();
		tests.add(new TestCRXML());
	
		for(Test t: tests){
			if(!t.runTest()){
				System.out.println("Test Failed: " + t.getName() );
				System.exit(0);
			}
		}
		System.out.println("All tests passed");
		
	}

}