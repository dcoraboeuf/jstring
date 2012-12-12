package net.sf.jstring.support;

import com.google.common.collect.ImmutableList;
import net.sf.jstring.SupportedLocales;
import org.apache.commons.lang3.Validate;

import java.util.*;

public class DefaultSupportedLocales implements SupportedLocales {

    private final ImmutableList<Locale> locales;

    public DefaultSupportedLocales() {
        this(Locale.ENGLISH);
    }

    public DefaultSupportedLocales(Locale... locales) {
        this(Arrays.asList(locales));
    }

    public DefaultSupportedLocales(List<Locale> locales) {
        Validate.notEmpty(locales, "List of locales must contain at least one element");
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
