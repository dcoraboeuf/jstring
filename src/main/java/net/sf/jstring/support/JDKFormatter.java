package net.sf.jstring.support;

import net.sf.jstring.Formatter;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation of the {@link Formatter} that uses the {@link java.text.MessageFormat}
 * from the JDK.
 */
public class JDKFormatter implements Formatter {

    @Override
    public String format(Locale locale, String pattern, Map<String, ?> parameters) {
        MessageFormat messageFormat = new MessageFormat(pattern, locale);
        Object[] objects;
        if (parameters != null && !parameters.isEmpty()) {
            objects = parameters.values().toArray();
        } else {
            objects = new Object[0];
        }
        return messageFormat.format(objects);
    }

}
