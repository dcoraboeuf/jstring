package net.sf.jstring.support;

import com.google.common.collect.ImmutableList;
import net.sf.jstring.LocalePolicy;
import net.sf.jstring.SupportedLocales;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DefaultSupportedLocales implements SupportedLocales {

    private final Logger logger = LoggerFactory.getLogger(SupportedLocales.class);

    private final ImmutableList<Locale> locales;
    private final LocalePolicy localeParsingPolicy;

    public DefaultSupportedLocales() {
        this(Locale.ENGLISH);
    }

    public DefaultSupportedLocales(Locale... locales) {
        this(Arrays.asList(locales));
    }

    public DefaultSupportedLocales(List<Locale> locales) {
        this(LocalePolicy.ERROR, locales);
    }

    public DefaultSupportedLocales(LocalePolicy localeParsingPolicy, List<Locale> locales) {
        Validate.notNull(localeParsingPolicy, "Locale policy must be defined");
        Validate.notEmpty(locales, "List of locales must contain at least one element");
        this.localeParsingPolicy = localeParsingPolicy;
        this.locales = ImmutableList.copyOf(locales);
    }

    @Override
    public Locale getDefaultLocale() {
        return locales.get(0);
    }

    @Override
    public Collection<Locale> getSupportedLocales() {
        return locales;
    }

    @Override
    public Locale filterForParsing(Locale locale) {
        if (locales.contains(locale)) {
            return locale;
        } else {
            logger.warn("[locales] Locale {} is not supported", locale);
            switch (localeParsingPolicy) {
                case EXTENDS:
                    List<Locale> candidates = LocaleUtils.localeLookupList(locale, getDefaultLocale());
                    if (candidates.size() > 1) {
                        return filterForParsing(candidates.get(1));
                    } else {
                        return getDefaultLocale();
                    }
                case DEFAULT:
                    return getDefaultLocale();
                case ERROR:
                    throw new UnsupportedLocaleException (locale, getSupportedLocales());
                default:
                    throw new IllegalStateException("Unknown locale policy: " + localeParsingPolicy);
            }
        }
    }

    @Override
    public SupportedLocales withLocaleParsingPolicy(LocalePolicy localeParsingPolicy) {
        return new DefaultSupportedLocales(localeParsingPolicy, locales);
    }

    @Override
    public LocalePolicy getLocaleParsingPolicy() {
        return localeParsingPolicy;
    }

    @Override
    public SupportedLocales withLocale(Locale locale) {
        if (this.locales.contains(locale)) {
            return this;
        } else {
            List<Locale> locales = new ArrayList<Locale>(this.locales);
            locales.add(locale);
            return new DefaultSupportedLocales(locales);
        }
    }
}
