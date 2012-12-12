package net.sf.jstring;

import java.util.Collection;
import java.util.Locale;

/**
 * Defines the list of locales defined by the application.
 */
public interface SupportedLocales {

    Locale getDefaultLocale();

    Collection<Locale> getSupportedLocales();

    SupportedLocales withLocale(Locale locale);
}
