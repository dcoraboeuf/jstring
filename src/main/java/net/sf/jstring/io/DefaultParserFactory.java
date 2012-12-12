package net.sf.jstring.io;

import net.sf.jstring.io.ls.LSParser;
import net.sf.jstring.io.properties.PropertiesParser;
import net.sf.jstring.io.xml.XMLParser;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DefaultParserFactory implements ParserFactory {

    private static Map<String,Parser<?>> defaultParsers() {
        Map<String, Parser<?>> parsers = new HashMap<String, Parser<?>>();
        parsers.put("properties", new PropertiesParser());
        parsers.put("xml", new XMLParser());
        parsers.put("ls", new LSParser());
        return parsers;
    }

    private final Map<String, Parser<?>> parsers;

    public DefaultParserFactory() {
        this(defaultParsers());
    }

    public DefaultParserFactory(Map<String, Parser<?>> parsers) {
        this.parsers = parsers;
    }

    @Override
    public Parser<?> getParser(URL path) {
        String extension = StringUtils.substringAfterLast(path.toString(), ".");
        return getParserFromExtension(extension);
    }

    protected Parser<?> getParserFromExtension(String extension) {
        Parser<?> parser = parsers.get(extension);
        if (parser != null) {
            return parser;
        } else {
            throw new CannotFindParserException (extension);
        }
    }
}
