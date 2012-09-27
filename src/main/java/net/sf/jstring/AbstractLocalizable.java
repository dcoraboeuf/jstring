package net.sf.jstring;

import java.util.Locale;

/**
 * Utility class to build a localizable implementation.
 */
public abstract class AbstractLocalizable implements Localizable {

    /**
     * Calls the {@link #getLocalizedMessage(Locale)} method
     * with the default locale.
     */
    @Override
    public String getLocalizedMessage() {
	return getLocalizedMessage(Locale.getDefault());
    }

    /**
     * Calls the {@link #getLocalizedMessage(Strings, Locale)} method.
     * @see net.sf.jstring.JStrings#getStaticCollection()
     */
    @Override
    public String getLocalizedMessage(Locale locale) {
        return getLocalizedMessage(JStrings.getStaticCollection(), locale);
    }

    /**
     * Returns the localized message for the default locale
     */
    @Override
    public String toString() {
        return getLocalizedMessage(null);
    }
}
