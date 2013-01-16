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
        assertEquals(LocalePolicy.ERROR, locales.getLocaleParsingPolicy());
    }

    @Test
    public void constructor_locale_param_one() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(ENGLISH);
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.ERROR, locales.getLocaleParsingPolicy());
    }

    @Test
    public void constructor_locale_param_two() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(ENGLISH, FRENCH);
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH, FRENCH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.ERROR, locales.getLocaleParsingPolicy());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_locale_no_parsing_policy() {
        new DefaultSupportedLocales(null, LocalePolicy.EXTENDS, asList(ENGLISH, FRENCH));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_locale_no_loookup_policy() {
        new DefaultSupportedLocales(LocalePolicy.ERROR, null, asList(ENGLISH, FRENCH));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_empty_list() {
        new DefaultSupportedLocales(LocalePolicy.ERROR, LocalePolicy.EXTENDS, Collections.<Locale>emptyList());
    }

    @Test
    public void with_locale() {
        SupportedLocales locales = new DefaultSupportedLocales();
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.ERROR, locales.getLocaleParsingPolicy());
        locales = locales.withLocale(FRENCH);
        assertEquals(ENGLISH, locales.getDefaultLocale());
        assertEquals(asList(ENGLISH, FRENCH), locales.getSupportedLocales());
        assertEquals(LocalePolicy.ERROR, locales.getLocaleParsingPolicy());
    }

    @Test
    public void with_parsing_policy() {
        SupportedLocales locales = new DefaultSupportedLocales();
        assertEquals(LocalePolicy.ERROR, locales.getLocaleParsingPolicy());
        locales = locales.withLocaleParsingPolicy(LocalePolicy.EXTENDS);
        assertEquals(LocalePolicy.EXTENDS, locales.getLocaleParsingPolicy());
    }

    @Test
    public void with_lookup_policy() {
        SupportedLocales locales = new DefaultSupportedLocales();
        assertEquals(LocalePolicy.EXTENDS, locales.getLocaleLookupPolicy());
        locales = locales.withLocaleLookupPolicy(LocalePolicy.ERROR);
        assertEquals(LocalePolicy.ERROR, locales.getLocaleLookupPolicy());
    }

    @Test
    public void filter_parsing_default() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.DEFAULT, LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        assertEquals(ENGLISH, locales.filterForParsing(Locale.FRANCE));
        assertEquals(FRENCH, locales.filterForParsing(Locale.FRENCH));
        assertEquals(ENGLISH, locales.filterForParsing(Locale.ENGLISH));
        assertEquals(ENGLISH, locales.filterForParsing(Locale.ITALIAN));
    }

    @Test
    public void filter_parsing_extends() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.EXTENDS, LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        assertEquals(FRENCH, locales.filterForParsing(Locale.FRANCE));
        assertEquals(FRENCH, locales.filterForParsing(Locale.FRENCH));
        assertEquals(ENGLISH, locales.filterForParsing(Locale.ENGLISH));
        assertEquals(ENGLISH, locales.filterForParsing(Locale.ITALIAN));
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void filter_parsing_error_country() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.ERROR, LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        locales.filterForParsing(Locale.FRANCE);
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void filter_parsing_error_language() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.ERROR, LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        locales.filterForParsing(Locale.ITALIAN);
    }

    @Test
    public void filter_lookup_default() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.ERROR, LocalePolicy.DEFAULT, asList(ENGLISH, FRENCH));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.FRANCE));
        assertEquals(FRENCH, locales.filterForLookup(Locale.FRENCH));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.ENGLISH));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.UK));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.ITALIAN));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.ITALY));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.GERMANY));
    }

    @Test
    public void filter_lookup_extends() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.ERROR, LocalePolicy.EXTENDS, asList(ENGLISH, FRENCH));
        assertEquals(FRENCH, locales.filterForLookup(Locale.FRANCE));
        assertEquals(FRENCH, locales.filterForLookup(Locale.FRENCH));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.ENGLISH));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.UK));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.ITALIAN));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.ITALY));
        assertEquals(ENGLISH, locales.filterForLookup(Locale.GERMANY));
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void filter_lookup_error_country() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.DEFAULT, LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        locales.filterForLookup(Locale.FRANCE);
    }

    @Test(expected = UnsupportedLocaleException.class)
    public void filter_lookup_error_language() {
        DefaultSupportedLocales locales = new DefaultSupportedLocales(LocalePolicy.DEFAULT, LocalePolicy.ERROR, asList(ENGLISH, FRENCH));
        locales.filterForLookup(Locale.ITALIAN);
    }

}
