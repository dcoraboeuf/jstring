package net.sf.jstring;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import net.sf.jstring.support.StringsLoader;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocalizableMessageTest {

	private static Strings strings;

	/**
	 * Loads the strings.
	 */
	@BeforeClass
	public static void beforeClass() {
		strings = new StringsLoader().withLocale(Locale.FRENCH).withPaths("test/ls/sampleStrings.ls").load();
	}

    @Test(expected = NullPointerException.class)
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
