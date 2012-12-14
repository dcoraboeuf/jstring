package net.sf.jstring.io;

import java.net.URL;

import net.sf.jstring.support.JSException;

public class CannotParseException extends JSException {

	public CannotParseException(URL url, Exception ex) {
		super(ex, "Cannot parse URL at %s: %s", url, ex);
	}

}
