package net.sf.jstring;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import java.util.Locale;

/**
 * Test of {@link JStrings}.
 * 
 * @author Damien Coraboeuf
 */
public class JStringsTest {

	/**
	 * Composite pattern where one argument is containing ${...}
	 */
	 @Test
	public void testComposite() {
		String value = JStrings.get(null, "sample.composite");
		assertEquals("This is version ${my.version}", value);
	}

	/**
	 * Test a defined key
	 */
	 @Test
	public void testDefined() {
		boolean defined = JStrings.isDefined(null, "sample.two");
		assertTrue(defined);
	}

	/**
	 * Test of dollar escape
	 */
	@Test
	public void testDollar() {
		String value = JStrings.get(null, "sample.dollar", "500");
		assertEquals("$500", value);
	}

	/**
	 * Test a mandatory string
	 */
	 @Test
	public void testMandatory() {
		String value = JStrings.get(null, "NoCode", true);
		assertNotNull(value);
		assertEquals("NoCode", value);
	}

	/**
	 * Test a non-defined key
	 */
	 @Test
	public void testNotDefined() {
		boolean defined = JStrings.isDefined(null, "notDefined");
		assertFalse(defined);
	}

	/**
	 * Test a non mandatory string
	 */
	 @Test
	public void testNotMandatory() {
		String value = JStrings.get(null, "NoCode", false);
		assertNull(value);
	}

	/**
	 * One parameter
	 */
	 @Test
	public void testOne() {
		String value = JStrings.get(null, "sample.one", "One");
		assertEquals("One parameter: One", value);
	}

	/**
	 * No parameter.
	 */
	 @Test
	public void testSimple() {
		String value = JStrings.get(null, "sample.simple");
		assertEquals("Simple", value);
	}

	/**
	 * Two parameters
	 */
	 @Test
	public void testTwo() {
		String value = JStrings.get(null, "sample.two", "One", "Two");
		assertEquals("Two parameters: One, Two", value);
	}

	/**
	 * Loads the strings.
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() {
        Locale.setDefault(Locale.ENGLISH);
		JStrings.add("test.SampleStrings");
	}

}
