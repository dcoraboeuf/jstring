package net.sf.jstring;

import java.util.Locale;

import org.apache.commons.lang3.Validate;

/**
 * Association of a code and some parameters.
 */
public class LocalizableMessage extends AbstractLocalizable {

    private final String code;
    private final Object[] parameters;

    /**
     * Constructor
     * @param code Key as an object
     * @param parameters List of parameters
     */
    public LocalizableMessage(Object code, Object... parameters) {
        Validate.notNull(code, "Localisation key must not be null");
        this.code = code.toString();
        this.parameters = parameters;
    }

    @Override
    public String getLocalizedMessage(Strings strings, Locale locale) {
        return strings.get(locale, code, parameters);
    }
}
