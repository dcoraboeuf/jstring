package net.sf.jstring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import net.sf.jstring.support.StringsLoader;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of {@link Strings}.
 *
 * @author Damien Coraboeuf
 */
public class StringsTest {

    private Strings strings;

    /**
     * Composite pattern where one argument is containing ${...}
     */
    @Test
    public void testComposite() {
        String value = strings.get(null, "sample.composite");
        assertEquals("This is version 2.0", value);
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
     * Test a mandatory string
     */
    @Test
    public void testNotDefinedFallback() {
        String value = strings.get(null, "NoCode");
        assertNotNull(value);
        assertEquals("##en#NoCode##", value);
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
     * Escaping
     */
    @Test
    public void testEscaping() {
    	assertEquals("Start: ", strings.get(Locale.ENGLISH, "sample.prefix"));
    	assertEquals("\n", strings.get(Locale.ENGLISH, "sample.suffix"));
    }

    /**
     * Loads the strings.
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws IOException {
        strings = new StringsLoader().withLocale(Locale.ENGLISH).withPaths("test/sampleStrings.ls").load();
    }

    /**
     * Keys values
     */
    @Test
    public void getKeyValues() {
        Map<String, String> map = strings.getKeyValues(Locale.FRENCH);
        assertNotNull(map);
        assertEquals("Un param\u00E8tre : {0}", map.get("sample.one"));
        assertEquals("Ceci est la version @[sample.version]", map.get("sample.composite"));
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