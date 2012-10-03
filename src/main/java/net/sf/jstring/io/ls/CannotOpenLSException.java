package net.sf.jstring.io.ls;

import java.net.URL;

import net.sf.jstring.support.CoreException;

public class CannotOpenLSException extends CoreException {

	public CannotOpenLSException(URL url) {
		super(url);
	}

}
