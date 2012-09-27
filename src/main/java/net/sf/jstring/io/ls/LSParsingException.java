package net.sf.jstring.io.ls;

import net.sf.jstring.support.CoreException;

public class LSParsingException extends CoreException {

	public LSParsingException(String line, int lineNo, String message) {
		super (line, lineNo, message);
	}

}
