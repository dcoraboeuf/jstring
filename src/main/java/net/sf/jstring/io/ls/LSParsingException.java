package net.sf.jstring.io.ls;

import net.sf.jstring.support.JSException;

public class LSParsingException extends JSException {

	public LSParsingException(String line, int lineNo, String message) {
		super ("Error while parsing LS file at line %d%n\tMessage: %s%n\tLine: %s", lineNo, line, message);
	}

}
