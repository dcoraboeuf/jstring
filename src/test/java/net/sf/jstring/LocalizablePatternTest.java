package net.sf.jstring;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import net.sf.jstring.support.DefaultStrings;

import org.junit.Test;

public class LocalizablePatternTest {
	
	private final Strings strings = new DefaultStrings();

    @Test(expected = NullPointerException.class)
    public void pattern_null() {
        new LocalizablePattern(null).getLocalizedMessage(strings, Locale.UK);
    }

    @Test
    public void pattern_simple() {
        String text = new LocalizablePattern("Pattern for %s", "Test").getLocalizedMessage(strings, Locale.UK);
        assertEquals("Pattern for Test", text);
        text = new LocalizablePattern("Pattern for %s", "Test").getLocalizedMessage(strings, Locale.FRANCE);
        assertEquals("Pattern for Test", text);
    }

    @Test
    public void pattern_uk() {
        String text = new LocalizablePattern("Pattern for %,d", 123456).getLocalizedMessage(strings, Locale.UK);
        assertEquals("Pattern for 123,456", text);
    }

    @Test
    public void pattern_fr() {
        String text = new LocalizablePattern("Pattern for %,d", 123456).getLocalizedMessage(strings, Locale.FRANCE);
        assertEquals("Pattern for 123\u00A0456", text);
    }

}
