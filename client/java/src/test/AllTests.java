package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	test.cr.CourseTest.class,
	test.cr.CourseOfferingTest.class
})

/**
 * AllTests
 *
 * Runs all of the tests that match the test pattern in this project.
 *
 * @since November 9, 2014
 * @version 0.1.0
 */
public class AllTests {}
