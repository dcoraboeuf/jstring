package net.sf.jstring;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class LocalizablePatternTest {

    @Test(expected = NullPointerException.class)
    public void pattern_null() {
        new LocalizablePattern(null).getLocalizedMessage(Locale.UK);
    }

    @Test
    public void pattern_simple() {
        String text = new LocalizablePattern("Pattern for %s", "Test").getLocalizedMessage(Locale.UK);
        assertEquals("Pattern for Test", text);
        text = new LocalizablePattern("Pattern for %s", "Test").getLocalizedMessage(Locale.FRANCE);
        assertEquals("Pattern for Test", text);
    }

    @Test
    public void pattern_uk() {
        String text = new LocalizablePattern("Pattern for %,d", 123456).getLocalizedMessage(Locale.UK);
        assertEquals("Pattern for 123,456", text);
    }

    @Test
    public void pattern_fr() {
        String text = new LocalizablePattern("Pattern for %,d", 123456).getLocalizedMessage(Locale.FRANCE);
        assertEquals("Pattern for 123\u00A0456", text);
    }

}
