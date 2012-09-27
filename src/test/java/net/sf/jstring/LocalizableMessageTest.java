package net.sf.jstring;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocalizableMessageTest {

	private static Strings strings;

	/**
	 * Loads the strings.
	 */
	@BeforeClass
	public static void beforeClass() {
        Locale.setDefault(Locale.ENGLISH);
        strings = new Strings("test.SampleStrings");
	}

    @Test(expected = IllegalArgumentException.class)
    public void code_null() {
        new LocalizableMessage(null);
    }

    @Test
    public void code_simple() {
        String text = new LocalizableMessage("sample.simple").getLocalizedMessage(strings, Locale.UK);
        assertEquals("Simple", text);
    }

    @Test
    public void code_one() {
        String text = new LocalizableMessage("sample.one", "My").getLocalizedMessage(strings, Locale.UK);
        assertEquals("One parameter: My", text);
    }

    @Test
    public void code_two() {
        String text = new LocalizableMessage("sample.two", "My", "Ours").getLocalizedMessage(strings, Locale.UK);
        assertEquals("Two parameters: My, Ours", text);
    }
    
}
