package net.sf.jstring;

import java.util.Locale;

/**
 * Association of a pattern and some parameters.
 *
 * @see String#format(java.util.Locale, String, Object...) 
 */
public class LocalizablePattern extends AbstractLocalizable {

    private final String pattern;
    private final Object[] parameters;

    /**
     * Constructor
     * @param pattern Key as an object
     * @param parameters List of parameters
     */
    public LocalizablePattern(String pattern, Object... parameters) {
        this.pattern = pattern;
        this.parameters = parameters;
    }

    @Override
    public String getLocalizedMessage(Strings strings, Locale locale) {
        return String.format(locale, pattern, parameters);
    }
}
