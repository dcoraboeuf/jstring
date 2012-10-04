package net.sf.jstring.support;

import java.io.IOException;

public class CannotDiscoverStringsException extends CoreException {

	public CannotDiscoverStringsException(IOException ex) {
		super(ex, ex);
	}

}
