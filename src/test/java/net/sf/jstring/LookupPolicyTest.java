package net.sf.jstring;

import net.sf.jstring.support.StringsLoader;
import net.sf.jstring.support.UnsupportedLocaleException;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class LookupPolicyTest {

    @Test
    public void extends_() {
        Strings strings = load(LocalePolicy.EXTENDS);
        assertEquals("Mon application préférée", strings.get(Locale.CANADA_FRENCH, "app.title"));
        assertEquals("Mon application préférée", strings.get(Locale.FRENCH, "app.title"));
        assertEquals("My favourite application", strings.get(Locale.ENGLISH, "app.title"));
        assertEquals("My favourite application", strings.get(Locale.ITALIAN, "app.title"));
    }

    @Test
    public void default_() {
        Strings strings = load(LocalePolicy.DEFAULT);
        assertEquals("My favourite application", strings.get(Locale.CANADA_FRENCH, "app.title"));
        assertEquals("Mon application préférée", strings.get(Locale.FRENCH, "app.title"));
        assertEquals("My favourite application", strings.get(Locale.ENGLISH, "app.title"));
        assertEquals("My favourite application", strings.get(Locale.ITALIAN, "app.title"));
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void error_country() {
        Strings strings = load(LocalePolicy.ERROR);
        strings.get(Locale.CANADA_FRENCH, "app.title");
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void error_language() {
        Strings strings = load(LocalePolicy.ERROR);
        strings.get(Locale.ITALIAN, "app.title");
    }

    @Test
    public void error_ok() {
        Strings strings = load(LocalePolicy.ERROR);
        assertEquals("Mon application préférée", strings.get(Locale.FRENCH, "app.title"));
        assertEquals("My favourite application", strings.get(Locale.ENGLISH, "app.title"));
    }

    private Strings load(LocalePolicy policy) {
        return StringsLoader.basic().withLocale(Locale.FRENCH).withLookupPolicy(policy).withPaths("test/ls/sample.ls").load();
    }

}
