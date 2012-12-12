package net.sf.jstring.io;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.sf.jstring.SupportedLocales;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

public abstract class AbstractParser<P extends AbstractParser<P>> implements Parser<P> {

    public static final String LANGUAGE_SEPARATOR = ",";

    protected final Logger logger = LoggerFactory.getLogger(Parser.class);

    private final boolean trace;

    public AbstractParser() {
        this(false);
    }

    protected AbstractParser(boolean trace) {
        this.trace = trace;
    }

    protected void trace (String pattern, Object... parameters) {
        if (trace) {
            logger.debug(pattern, parameters);
        }
    }

    protected String getBundleName(URL url) {
        String path = url.getFile();
        path = StringUtils.replace(path, "\\", "/");
        path = StringUtils.substringAfterLast(path, "/");
        path = StringUtils.substringBeforeLast(path, ".");
        return path;
    }

    protected List<Locale> getLanguages(SupportedLocales supportedLocales, String languageValue) {
        // Gets the list of languages
        List<Locale> locales = Lists.transform(
                Arrays.asList(StringUtils.split(languageValue, LANGUAGE_SEPARATOR)),
                new Function<String, Locale>() {
                    @Override
                    public Locale apply(String name) {
                        return new Locale(name);
                    }
                }
            );
        // Final list
        Set<Locale> filteredLocales = new LinkedHashSet<Locale>();
        for (Locale locale : locales) {
            // Filtering
            Locale filteredLocale = supportedLocales.filter (locale);
            // OK for this locale
            if (filteredLocale != null) {
                filteredLocales.add(locale);
            }
        }
        // OK
        return new ArrayList<Locale>(filteredLocales);
    }

}
