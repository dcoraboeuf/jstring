package net.sf.jstring.io.properties;

import net.sf.jstring.SupportedLocales;
import net.sf.jstring.io.AbstractParser;
import net.sf.jstring.model.Bundle;

import java.net.URL;

public class PropertiesParser extends AbstractParser<PropertiesParser> {

    public PropertiesParser() {
    }

    protected PropertiesParser(boolean trace) {
        super(trace);
    }

    @Override
    public PropertiesParser withTraces() {
        return new PropertiesParser(true);
    }

    @Override
    public Bundle parse(SupportedLocales supportedLocales, URL url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
