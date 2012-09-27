package net.sf.jstring;

import net.sf.jstring.impl.DefaultStrings;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test of {@link Strings}.
 *
 * @author Damien Coraboeuf
 */
public class StringsTest {

    private DefaultStrings strings;

    /**
     * Composite pattern where one argument is containing ${...}
     */
    @Test
    public void testComposite() {
        String value = strings.get(null, "sample.composite");
        assertEquals("This is version ${my.version}", value);
    }

    /**
     * Test a defined key
     */
    @Test
    public void testDefined() {
        boolean defined = strings.isDefined(null, "sample.two");
        assertTrue(defined);
    }

    /**
     * Test of dollar escape
     */
    @Test
    public void testDollar() {
        String value = strings.get(null, "sample.dollar", "500");
        assertEquals("$500", value);
    }

    /**
     * Test a mandatory string
     */
    @Test
    public void testMandatory() {
        String value = strings.get(null, "NoCode", true);
        assertNotNull(value);
        assertEquals("NoCode", value);
    }

    /**
     * Test a non-defined key
     */
    @Test
    public void testNotDefined() {
        boolean defined = strings.isDefined(null, "notDefined");
        assertFalse(defined);
    }

    /**
     * Test a non mandatory string
     */
    @Test
    public void testNotMandatory() {
        String value = strings.get(null, "NoCode", false);
        assertNull(value);
    }

    /**
     * One parameter
     */
    @Test
    public void testOne() {
        String value = strings.get(null, "sample.one", "One");
        assertEquals("One parameter: One", value);
    }

    /**
     * No parameter.
     */
    @Test
    public void testSimple() {
        String value = strings.get(null, "sample.simple");
        assertEquals("Simple", value);
    }

    /**
     * Two parameters
     */
    @Test
    public void testTwo() {
        String value = strings.get(null, "sample.two", "One", "Two");
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
        strings = new DefaultStrings("test.SampleStrings");
    }

    /**
     * Lock test
     */
    @Test(expected = IllegalStateException.class)
    public void lock() {
        assertFalse(strings.isLocked());
        strings.lock();
        assertTrue(strings.isLocked());
        // Tries to add
        strings.add("any.thing");
    }

    /**
     * Keys values
     */
    @Test
    public void getKeyValues() {
        Map<String, String> map = strings.getKeyValues(Locale.FRENCH);
        assertNotNull(map);
        assertEquals("Un param\u00E8tre : {0}", map.get("sample.one"));
        assertEquals("Ceci est la version ${my.version}", map.get("sample.composite"));
    }
    
    /**
     * {@link Localizable} in the parameters.
     */
    @Test
    public void testLocalizable() {
    	Localizable parameter = new LocalizableMessage("sample.two", "A", "B");
    	Localizable localizable = new LocalizableMessage("sample.one", parameter);
    	String value = localizable.getLocalizedMessage(strings, Locale.ENGLISH);
    	assertEquals("One parameter: Two parameters: A, B", value);
    }

}
