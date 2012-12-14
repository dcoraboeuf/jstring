package net.sf.jstring.io.properties;

import net.sf.jstring.support.JSException;

public class PropertiesParsingException extends JSException {
	public PropertiesParsingException(int lineNo, String line, String message) {
		super(
				"Error while parsing propertis file at line %d%n\tMessage: %s%n\tLine: %s",
				lineNo, line, message);
	}
}
