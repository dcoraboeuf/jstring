package net.sf.jstring.io;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public abstract class AbstractParser<P extends AbstractParser<P>> implements Parser<P> {

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

}
