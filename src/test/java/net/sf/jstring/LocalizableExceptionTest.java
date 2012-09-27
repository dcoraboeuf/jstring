package net.sf.jstring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import net.sf.jstring.support.DefaultStrings;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of {@link LocalizableException}.
 * 
 * @author Damien Coraboeuf
 */
public class LocalizableExceptionTest {
	
	private static Strings strings;

	/**
	 * Loads the strings.
	 */
	@BeforeClass
	public static void beforeClass() {
        strings = new DefaultStrings("test.SampleStrings");
	}

	/**
	 * No parameter.
	 */
	@Test
	public void testSimple() {
		try {
			throw new LocalizableException("sample.simple");
		} catch (LocalizableException ex) {
			assertEquals("Simple", ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}

	/**
	 * One parameter
	 */
	@Test
	public void testOne() {
		try {
			throw new LocalizableException("sample.one", "One");
		} catch (LocalizableException ex) {
			assertEquals("One parameter: One", ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}

	/**
	 * Two parameters
	 */
	@Test
	public void testTwo() {
		try {
			throw new LocalizableException("sample.two", "One", "Two");
		} catch (LocalizableException ex) {
			assertEquals("Two parameters: One, Two", ex.getLocalizedMessage(strings, Locale.ENGLISH));
		}
	}

	/**
	 * One parameter and one exception.
	 */
	@Test
	public void testOneWithException() {
		try {
			Exception root = new Exception("Root");
			throw new LocalizableException("sample.exception", root, "One", root
					.getMessage());
		} catch (LocalizableException ex) {
			assertEquals("Exception for One: Root", ex.getLocalizedMessage(strings, Locale.ENGLISH));
			assertNotNull(ex.getCause());
			assertEquals("Root", ex.getCause().getMessage());
		}
	}

	/**
	 * Test of copy constructor
	 */
	@Test
	public void testCopy() {
		LocalizableException ex = new LocalizableException("sample.two", "One", "Two");
		LocalizableException cex = new LocalizableException(ex);
		assertEquals("sample.two", cex.getCode());
		Object[] parameters = cex.getParameters();
		assertNotNull(parameters);
		assertEquals(2, parameters.length);
		assertEquals("One", parameters[0]);
		assertEquals("Two", parameters[1]);
	}

}
