package net.sf.jstring.io.properties;

import net.sf.jstring.io.AbstractParserTest;
import net.sf.jstring.io.Parser;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class PropertiesParserTest extends AbstractParserTest {

    @Override
    protected String getTestFileResourcePath() {
        return "/test/properties/sample_en.properties";
    }

    @Override
    protected Parser<?> createParser() {
        return new PropertiesParser().withTraces();
    }

    @Test
    public void getLocaleURL_suffix_for_default() throws MalformedURLException {
        URL url = new PropertiesParser().getLocaleURL(new URL("file:///my/path/sample_en.properties"), Locale.ENGLISH, Locale.ENGLISH);
        assertEquals (new URL("file:///my/path/sample_en.properties"), url);
    }

    @Test
    public void getLocaleURL_no_suffix_for_default() throws MalformedURLException {
        URL url = new PropertiesParser().getLocaleURL(new URL("file:///my/path/sample.properties"), Locale.ENGLISH, Locale.ENGLISH);
        assertEquals (new URL("file:///my/path/sample.properties"), url);
    }

    @Test
    public void getLocaleURL_suffix() throws MalformedURLException {
        URL url = new PropertiesParser().getLocaleURL(new URL("file:///my/path/sample_en.properties"), Locale.FRENCH, Locale.ENGLISH);
        assertEquals (new URL("file:///my/path/sample_fr.properties"), url);
    }

    @Test
    public void getLocaleURL_no_suffix() throws MalformedURLException {
        URL url = new PropertiesParser().getLocaleURL(new URL("file:///my/path/sample.properties"), Locale.FRENCH, Locale.ENGLISH);
        assertEquals (new URL("file:///my/path/sample_fr.properties"), url);
    }

}
