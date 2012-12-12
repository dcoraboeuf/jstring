package net.sf.jstring.support;

import net.sf.jstring.LocalePolicy;
import net.sf.jstring.SupportedLocales;
import org.junit.Test;

import java.util.Collections;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static org.junit.Assert.assertEquals;

public class DefaultSupportedLocalesTest {

    @Test
    public void constructor_default() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales();
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.EXTENDS, locales.getLocalePolicy());
    }

    @Test
    public void constructor_locale_param_one() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(ENGLISH);
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.EXTENDS, locales.getLocalePolicy());
    }

    @Test
    public void constructor_locale_param_two() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(ENGLISH, FRENCH);
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH, FRENCH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.EXTENDS, locales.getLocalePolicy());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_locale_no_policy() {
        new DefaultSupportedLocales(null, asList(ENGLISH, FRENCH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_empty_list() {
        new DefaultSupportedLocales(LocalePolicy.EXTENDS, Collections.<Locale>emptyList());
    }

    @Test
    public void with_locale() {
        SupportedLocales locales = new DefaultSupportedLocales();
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.EXTENDS, locales.getLocalePolicy());
        locales = locales.withLocale(FRENCH);
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH, FRENCH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.EXTENDS, locales.getLocalePolicy());
    }

    @Test
    public void with_policy() {
        SupportedLocales locales = new DefaultSupportedLocales();
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.EXTENDS, locales.getLocalePolicy());
        locales = locales.withPolicy(LocalePolicy.ERROR);
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.ERROR, locales.getLocalePolicy());
    }

    @Test
    public void filter_default() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.DEFAULT, asList(ENGLISH, FRENCH));
        assertEquals(ENGLISH, locales.filter(Locale.FRANCE));
        assertEquals(FRENCH, locales.filter(Locale.FRENCH));
        assertEquals(ENGLISH, locales.filter(Locale.ENGLISH));
        assertEquals(ENGLISH, locales.filter(Locale.ITALIAN));
    }

    @Test
    public void filter_extends() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.EXTENDS, asList(ENGLISH, FRENCH));
        assertEquals(FRENCH, locales.filter(Locale.FRANCE));
        assertEquals(FRENCH, locales.filter(Locale.FRENCH));
        assertEquals(ENGLISH, locales.filter(Locale.ENGLISH));
        assertEquals(ENGLISH, locales.filter(Locale.ITALIAN));
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void filter_error_country() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        locales.filter(Locale.FRANCE);
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void filter_error_language() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        locales.filter(Locale.ITALIAN);
    }

}
