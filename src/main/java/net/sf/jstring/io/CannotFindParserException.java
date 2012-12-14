package net.sf.jstring.io;

import net.sf.jstring.support.JSException;

public class CannotFindParserException extends JSException {

    public CannotFindParserException(String extension) {
        super("Cannot find any parser for extension %s", extension);
    }

}
