package net.sf.jstring.support;

import java.util.Collection;
import java.util.Locale;

public class UnsupportedLocaleException extends CoreException {

    public UnsupportedLocaleException(Locale locale, Collection<Locale> supportedLocales) {
        super(locale, supportedLocales);
    }
}
