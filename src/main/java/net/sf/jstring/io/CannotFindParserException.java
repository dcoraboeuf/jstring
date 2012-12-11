package net.sf.jstring.io;

import net.sf.jstring.support.CoreException;

public class CannotFindParserException extends CoreException {

    public CannotFindParserException(String extension) {
        super(extension);
    }

}
