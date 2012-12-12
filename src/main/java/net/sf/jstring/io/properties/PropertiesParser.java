package net.sf.jstring.io.properties;

import net.sf.jstring.SupportedLocales;
import net.sf.jstring.io.AbstractParser;
import net.sf.jstring.io.CannotOpenException;
import net.sf.jstring.io.CannotParseException;
import net.sf.jstring.model.Bundle;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

public class PropertiesParser extends AbstractParser<PropertiesParser> {

    public static final String KEY_VALUE_SEPARATOR = "=";
    public static final String COMMENT_PREFIX = "#";
    public static final String VALUE_CONTINUATOR = "\\";

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
        List<Token> tokens = loadTokens(url);
        return null;
    }

    private List<Token> loadTokens(URL url) {
        try {
            InputStream in = url.openStream();
            if (in == null) {
                throw new CannotOpenException(url);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
                try {
                    List<Token> tokens = new ArrayList<Token>();
                    String line;
                    Token previousToken = Blank.INSTANCE;
                    int lineno = 0;
                    while ((line = reader.readLine()) != null) {
                        Token token = parseToken (previousToken, line, lineno++);
                        tokens.add(token);
                        previousToken = token;
                    }
                    return tokens;
                } finally {
                    reader.close();
                }
            }
        } catch (IOException ex) {
            throw new CannotParseException(url, ex);
        }
    }

    protected Token parseToken(Token previousToken, String line, int lineno) {
        String value = trim(line);
        if (StringUtils.isEmpty(value)) {
            // Blank line
            return Blank.INSTANCE;
        } else if (startsWith(value, COMMENT_PREFIX)) {
            String comment = trim(substring(value, 1));
            // Escaped comment for continued value
            if (previousToken.expectValues()) {
                if (startsWith(comment, COMMENT_PREFIX)) {
                    // Escaped comment
                    return newValue(comment);
                } else if (endsWith(comment, VALUE_CONTINUATOR)) {
                    // This comment allows the values to go on
                    return new Comment (comment, true);
                } else {
                    // This comment ends the values
                    return new Comment (comment);
                }
            } else {
                return new Comment (comment);
            }
        } else if (startsWith(value, "[") && endsWith(value, "]")) {
            String name = substring(value, 0, -1);
            return new Section (name);
        } else if (contains(value, KEY_VALUE_SEPARATOR)) {
            String key = trim(substringBefore(value, KEY_VALUE_SEPARATOR));
            String text = trim(substringAfter(value, KEY_VALUE_SEPARATOR));
            if (endsWith(text, VALUE_CONTINUATOR)) {
                return new Key (key, substring(text, 0, -1), true);
            } else {
                return new Key (key, text);
            }
        } else if (previousToken.expectValues()) {
            return newValue(value);
        } else {
            // Just a value - outside of any key definition
            throw new PropertiesParsingException (lineno, line, "Cannot parse standalone value");
        }
    }

    private Token newValue(String value) {
        if (endsWith(value, VALUE_CONTINUATOR)) {
            return new Value(substring(value, 0, -1), true);
        } else {
            return new Value (value);
        }
    }
}
