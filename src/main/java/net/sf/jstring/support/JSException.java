package net.sf.jstring.support;

import static java.lang.String.format;

public abstract class JSException extends RuntimeException {

	public JSException(String message, Object... parameters) {
		super(format(message, parameters));
	}

	public JSException(Exception ex, String message, Object... parameters) {
		super(format(message, parameters), ex);
	}

}
