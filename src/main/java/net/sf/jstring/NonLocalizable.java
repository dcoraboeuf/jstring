package net.sf.jstring;

import java.util.Locale;

/**
 * Non localizable implementation of a localizable. Can be used to wrap fixed values
 * as localizable items.
 */
public class NonLocalizable extends AbstractLocalizable {

    private final String value;

    /**
     * Constructor
     * @param value Value to return in all cases
     */
    public NonLocalizable(String value) {
        this.value = value;
    }

    /**
     * Always returns a fixed value
     */
    @Override
    public String getLocalizedMessage(Strings strings, Locale locale) {
        return value;
    }
}
