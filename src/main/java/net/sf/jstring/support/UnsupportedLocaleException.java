package net.sf.jstring.support;

import java.util.Collection;
import java.util.Locale;

public class UnsupportedLocaleException extends JSException {

    public UnsupportedLocaleException(Locale locale, Collection<Locale> supportedLocales) {
        super("Locale %s is unsupported. It should be one of %s", locale, supportedLocales);
    }
}
