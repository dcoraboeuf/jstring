package net.sf.jstring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import net.sf.jstring.support.StringsLoader;

import org.junit.Test;

public class NonLocalizableTest {
	
	private final Strings strings = StringsLoader.empty();

    @Test
    public void value_null() {
        String text = new NonLocalizable(null).getLocalizedMessage(strings, Locale.UK);
        assertNull(text);
    }

    @Test
    public void value() {
        String text = new NonLocalizable("My value").getLocalizedMessage(strings, Locale.UK);
        assertEquals("My value", text);
        text = new NonLocalizable("My value").getLocalizedMessage(strings, Locale.FRANCE);
        assertEquals("My value", text);
    }

}
