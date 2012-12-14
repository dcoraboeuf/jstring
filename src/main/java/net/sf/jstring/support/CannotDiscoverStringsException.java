package net.sf.jstring.support;

import java.io.IOException;

public class CannotDiscoverStringsException extends JSException {

	public CannotDiscoverStringsException(IOException ex) {
		super(ex, "Problem when trying to discover strings: %s", ex);
	}

}
