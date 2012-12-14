package net.sf.jstring.io.properties;

import net.sf.jstring.SupportedLocales;
import net.sf.jstring.builder.BundleBuilder;
import net.sf.jstring.builder.BundleKeyBuilder;
import net.sf.jstring.builder.BundleSectionBuilder;
import net.sf.jstring.builder.BundleValueBuilder;
import net.sf.jstring.io.AbstractParser;
import net.sf.jstring.io.CannotOpenException;
import net.sf.jstring.io.CannotParseException;
import net.sf.jstring.model.Bundle;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
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
        logger.debug("[properties] Getting bundle for {}", url);
        // Gets the list of languages
        Collection<Locale> locales = supportedLocales.getSupportedLocales();
        // Bundle name
        String bundleName = getBundleName(getBundleURL(url, supportedLocales.getDefaultLocale()));
        // Overall bundle
        BundleBuilder general = BundleBuilder.create(bundleName);
        // For all locales
        for (Locale locale : locales) {
            // Bundle builder
            BundleBuilder builder = BundleBuilder.create(bundleName);
            // Gets the URL for this locale
            URL localeURL = getLocaleURL(url, locale, supportedLocales.getDefaultLocale());
            logger.debug("[properties] Getting {} URL for locale {}", localeURL, locale);
            // Parses the URL as tokens
            List<Token> tokens = readTokens(localeURL);
            // Parses the token for this language
            parseTokens(tokens, builder, locale, supportedLocales);
            // Merge
            general.merge(builder);
        }

        // Bundle
        return general.build();
    }

    private List<Token> readTokens(URL localeURL) {
        List<Token> tokens;
        try {
            InputStream in = localeURL.openStream();
            if (in != null) {
                    try {
                        tokens = loadTokens(in);
                    } finally {
                        in.close();
                    }
            } else {
                throw new CannotOpenException(localeURL);
            }
        } catch (IOException ex) {
            throw new CannotParseException(localeURL, ex);
        }
        return tokens;
    }

    private URL getBundleURL(URL url, Locale defaultLocale) {
        final String file = url.getFile();
        String name = file.replaceFirst(format("_%s\\.properties", defaultLocale), ".properties");
        return getURLWithNewFile(url, name);
    }

    private URL getURLWithNewFile(URL url, String name) {
        try {
            return new URL(url, name);
        } catch (MalformedURLException e) {
            throw new CannotOpenException(url);
        }
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
        return getURLWithNewFile(url, name);
    }

    private List<Token> loadTokens(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
        try {
            List<Token> tokens = new ArrayList<Token>();
            String line;
            Token previousToken = new Blank(0, "");
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
            return new Blank(lineno, line);
        } else if (startsWith(value, COMMENT_PREFIX)) {
            String comment = trim(substring(value, 1));
            // Escaped comment for continued value
            if (previousToken.expectValues()) {
                if (startsWith(comment, COMMENT_PREFIX)) {
                    // Escaped comment
                    return newValue(lineno, line, comment);
                } else if (endsWith(comment, VALUE_CONTINUATOR)) {
                    // This comment allows the values to go on
                    return new Comment (lineno, line, comment, true);
                } else {
                    // This comment ends the values
                    return new Comment (lineno, line, comment);
                }
            } else {
                return new Comment (lineno, line, comment);
            }
        } else if (startsWith(value, "[") && endsWith(value, "]")) {
            String name = substring(value, 0, -1);
            return new Section (lineno, line, name);
        } else if (contains(value, KEY_VALUE_SEPARATOR)) {
            String key = trim(substringBefore(value, KEY_VALUE_SEPARATOR));
            String text = trim(substringAfter(value, KEY_VALUE_SEPARATOR));
            if (endsWith(text, VALUE_CONTINUATOR)) {
                return new Key (lineno, line, key, substring(text, 0, -1), true);
            } else {
                return new Key (lineno, line, key, text);
            }
        } else if (previousToken.expectValues()) {
            return newValue(lineno, line, value);
        } else {
            // Just a value - outside of any key definition
            throw new PropertiesParsingException (lineno, line, "Cannot parse standalone value");
        }
    }

    private Token newValue(int lineno, String line, String value) {
        if (endsWith(value, VALUE_CONTINUATOR)) {
            return new Value(lineno, line, substring(value, 0, -1), true);
        } else {
            return new Value (lineno, line, value);
        }
    }

    private void parseTokens(List<Token> tokens, BundleBuilder builder, Locale locale, SupportedLocales supportedLocales) {
        // Initializes the parser
        TokensParser parser = new TokensParser (builder, locale, supportedLocales);
        // Loops through all the tokens
        for (Token token : tokens) {
            parser.parse(token);
        }
        // OK
        parser.close();
    }

    private interface TokenParser {

        void parse(Token token);

        void close();
    }

    private class TokensParser {
        private final BundleBuilder builder;
        private final Locale locale;
        private final SupportedLocales supportedLocales;
        private final Stack<TokenParser> parserStack;

        public TokensParser(BundleBuilder builder, Locale locale, SupportedLocales supportedLocales) {
            this.builder = builder;
            this.locale = locale;
            this.supportedLocales = supportedLocales;
            parserStack = new Stack<TokenParser>();
            parserStack.push(new BundleParser());
        }

        public void close() {
            while (!parserStack.isEmpty()) {
                parserStack.pop().close();
            }
        }

        private BundleSectionBuilder getDefaultSectionBuilder() {
            return builder.getDefaultSectionBuilder();
        }

        private PropertiesParsingException createParsingException(Token token) {
            return new PropertiesParsingException(token.getLineno(), token.getLine(), format("Unexpected token %s", token.getClass().getSimpleName()));
        }

        public void parse(Token token) {
            TokenParser currentParser = parserStack.peek();
            trace("[properties] {} ==> {}", token.getClass().getSimpleName(), currentParser.getClass().getSimpleName());
            currentParser.parse(token);
        }

        private TokenParser push (TokenParser tokenParser) {
            parserStack.push(tokenParser);
            trace("[properties]   + {}", tokenParser.getClass().getSimpleName());
            return tokenParser;
        }

        private TokenParser cleanAndPush (Class<? extends TokenParser> topTokenParserType, TokenParser tokenParser) {
            while (!topTokenParserType.isInstance(parserStack.peek())) {
                TokenParser parser = parserStack.pop();
                trace("[properties]   - {}", parser.getClass().getSimpleName());
                parser.close();
            }
            return push(tokenParser);
        }

        private String readValue(String value) {
            return StringEscapeUtils.unescapeJava(value);
        }

        private abstract class AbstractTokenParser implements TokenParser {

        }

        private class BundleParser extends AbstractTokenParser {

            @Override
            public void parse(Token token) {
                if (token instanceof Comment) {
                    builder.comment(((Comment) token).getComment());
                } else if (token instanceof Blank) {
                    push(new TopBlankParser());
                } else if (token instanceof Key) {
                    push(new TopKeyParser((Key) token));
                } else {
                    throw createParsingException (token);
                }
            }

            @Override
            public void close() {
            }
        }

        private class TopBlankParser extends AbstractTokenParser {

            @Override
            public void parse(Token token) {
            }

            @Override
            public void close() {
            }
        }

        private abstract class AbstractKeyParser extends AbstractTokenParser {

        }

        private class TopKeyParser extends AbstractKeyParser {

            private final BundleKeyBuilder keyBuilder;

            public TopKeyParser(Key key) {
                keyBuilder = BundleKeyBuilder.create(key.getName());
                keyBuilder.value(locale, BundleValueBuilder.create().value(readValue(key.getValue())));
            }

            @Override
            public void parse(Token token) {
                if (token instanceof Key) {
                    cleanAndPush(BundleParser.class, new TopKeyParser((Key) token));
                } else if (token instanceof Blank) {
                    cleanAndPush(BundleParser.class, new TopBlankParser());
                } else {
                    throw createParsingException(token);
                }
            }

            @Override
            public void close() {
                getDefaultSectionBuilder().key(keyBuilder);
            }
        }
    }

}
