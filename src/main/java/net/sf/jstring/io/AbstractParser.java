package net.sf.jstring.io;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

    protected List<Locale> getLanguages(String languageValue) {
        return Lists.transform(
                Arrays.asList(StringUtils.split(languageValue, LANGUAGE_SEPARATOR)),
                new Function<String, Locale>() {
                    @Override
                    public Locale apply(String name) {
                        return new Locale(name);
                    }
                }
            );
    }

}
