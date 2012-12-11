package net.sf.jstring.io.xml;

import net.sf.jstring.io.AbstractParserTest;
import net.sf.jstring.io.Parser;

public class XMLParserTest extends AbstractParserTest {

    @Override
    protected String getTestFileResourcePath() {
        return "/test/xml/sample.xml";
    }

    @Override
    protected Parser createParser() {
        return new XMLParser(true);
    }

}
