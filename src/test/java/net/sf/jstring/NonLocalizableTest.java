package net.sf.jstring;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NonLocalizableTest {

    @Test
    public void value_null() {
        String text = new NonLocalizable(null).getLocalizedMessage(Locale.UK);
        assertNull(text);
    }

    @Test
    public void value() {
        String text = new NonLocalizable("My value").getLocalizedMessage(Locale.UK);
        assertEquals("My value", text);
        text = new NonLocalizable("My value").getLocalizedMessage(Locale.FRANCE);
        assertEquals("My value", text);
    }

}
