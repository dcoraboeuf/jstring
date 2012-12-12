package net.sf.jstring.io.properties;

import net.sf.jstring.support.CoreException;

public class PropertiesParsingException extends CoreException {
    public PropertiesParsingException(int lineno, String line, String message) {
        super(lineno, line, message);
    }
}
