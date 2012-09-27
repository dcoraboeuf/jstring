package net.sf.jstring.io.ls;

import java.io.IOException;
import java.net.URL;

import net.sf.jstring.support.CoreException;

public class CannotParseLSException extends CoreException {

	public CannotParseLSException(URL url, IOException ex) {
		super(ex, url, ex);
	}

}
