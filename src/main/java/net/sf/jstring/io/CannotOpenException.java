package net.sf.jstring.io;

import java.net.URL;

import net.sf.jstring.support.JSException;

public class CannotOpenException extends JSException {

	public CannotOpenException(URL url) {
		super("Cannot open URL at %s", url);
	}

}
