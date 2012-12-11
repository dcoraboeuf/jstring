package net.sf.jstring.io.ls;

import net.sf.jstring.io.AbstractParserTest;
import net.sf.jstring.io.Parser;

public class LSParserTest extends AbstractParserTest {

    @Override
    protected String getTestFileResourcePath() {
        return "/test/ls/sample.ls";
    }

    @Override
    protected Parser createParser() {
        return new LSParser(true);
    }

}
