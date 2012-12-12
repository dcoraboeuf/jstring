package net.sf.jstring.io.properties;

import net.sf.jstring.io.AbstractParserTest;
import net.sf.jstring.io.Parser;

public class PropertiesParserTest extends AbstractParserTest {

    @Override
    protected String getTestFileResourcePath() {
        return "/test/properties/sample.properties";
    }

    @Override
    protected Parser<?> createParser() {
        return new PropertiesParser().withTraces();
    }

}
