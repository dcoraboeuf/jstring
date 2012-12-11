package net.sf.jstring.io;

import net.sf.jstring.support.CoreException;

import java.net.URL;

public class CannotParseException extends CoreException {

	public CannotParseException(URL url, Exception ex) {
		super(ex, url, ex);
	}

}
