package net.sf.jstring.io.properties;

import net.sf.jstring.SupportedLocales;
import net.sf.jstring.io.AbstractParser;
import net.sf.jstring.io.CannotOpenException;
import net.sf.jstring.io.CannotParseException;
import net.sf.jstring.model.Bundle;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.lang.String.format;
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
        // FIXME Parse one file per language
        logger.debug("[properties] Getting bundle for {}", url);
        // Gets the list of languages
        Collection<Locale> locales = supportedLocales.getSupportedLocales();
        for (Locale locale : locales) {
            // Gets the URL for this locale
            URL localeURL = getLocaleURL(url, locale, supportedLocales.getDefaultLocale());
            logger.debug("[properties] Getting {} URL for locale {}", localeURL, locale);
            // Tries to open the URL
            try {
                InputStream in = localeURL.openStream();
                if (in != null) {
                        try {
                            List<Token> tokens = loadTokens(in);
                        } finally {
                            in.close();
                        }
                } else {
                    throw new CannotOpenException(localeURL);
                }
            } catch (IOException ex) {
                throw new CannotParseException(localeURL, ex);
            }
        }

        // FIXME Bundle
        return null;
    }

    protected URL getLocaleURL(URL url, Locale locale, Locale defaultLocale) {
        final String file = url.getFile();
        String name;
        if (Pattern.matches(format(".*_%s\\.properties$", defaultLocale), file)) {
            // Suffix mode - uses the target locale only for the non-default locale
            if (defaultLocale.equals(locale)) {
                name = file;
            } else {
                name = file.replaceFirst(format("_%s\\.properties$", defaultLocale), format("_%s.properties", locale));
            }
        } else {
            // No suffix mode - uses the target locale directly, only for the non-default locale
            if (defaultLocale.equals(locale)) {
                name = file;
            } else {
                name = file.replaceFirst("\\.properties$", format("_%s.properties", locale));
            }
        }
        try {
            return new URL(url, name);
        } catch (MalformedURLException e) {
            throw new CannotOpenException(url);
        }
    }

    private List<Token> loadTokens(InputStream in) throws IOException {
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
