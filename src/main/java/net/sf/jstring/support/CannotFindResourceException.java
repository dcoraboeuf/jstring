package net.sf.jstring.support;

public class CannotFindResourceException extends JSException {

	public CannotFindResourceException(String path) {
		super("Cannot find resource at %s", path);
	}

}
