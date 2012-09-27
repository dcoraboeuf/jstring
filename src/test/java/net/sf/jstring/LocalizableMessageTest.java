package net.sf.jstring;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class LocalizableMessageTest {

	/**
	 * Loads the strings.
	 */
	@BeforeClass
	public static void beforeClass() {
        Locale.setDefault(Locale.ENGLISH);
		JStrings.add("test.SampleStrings");
	}

    @Test(expected = IllegalArgumentException.class)
    public void code_null() {
        new LocalizableMessage(null);
    }

    @Test
    public void code_simple() {
        String text = new LocalizableMessage("sample.simple").getLocalizedMessage(Locale.UK);
        assertEquals("Simple", text);
    }

    @Test
    public void code_one() {
        String text = new LocalizableMessage("sample.one", "My").getLocalizedMessage(Locale.UK);
        assertEquals("One parameter: My", text);
    }

    @Test
    public void code_two() {
        String text = new LocalizableMessage("sample.two", "My", "Ours").getLocalizedMessage(Locale.UK);
        assertEquals("Two parameters: My, Ours", text);
    }
    
}
