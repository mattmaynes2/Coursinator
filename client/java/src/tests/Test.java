/**
 * Test
 *
 * An interface that defines all common testing features
 *
 * @version 0.0.0
 * @date October 6, 2014
 * @author Matthew Maynes
 *
 */

public interface Test{

	public String getName();

	/**
	 * Runs all of the test in the test class
	 */
	public boolean runTests();
}