package net.sf.jstring.io;

import java.net.URL;

import net.sf.jstring.support.CoreException;

public class CannotOpenException extends CoreException {

	public CannotOpenException(URL url) {
		super(url);
	}

}
