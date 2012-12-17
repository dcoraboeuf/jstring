package net.sf.jstring.validation;

import net.sf.jstring.*;
import net.sf.jstring.index.DefaultIndexedBundleCollection;
import net.sf.jstring.index.IndexedBundleCollection;
import net.sf.jstring.model.BundleCollection;

import java.util.*;

public class DefaultValidator implements Validator {

    /**
     * @see #validate(net.sf.jstring.model.BundleCollection, net.sf.jstring.SupportedLocales)
     */
    @Override
    public ValidationResult validate(Strings strings) {
        return validate(strings.getBundleCollection(), strings.getSupportedLocales());
    }

    @Override
    public ValidationResult validate(BundleCollection bundleCollection, SupportedLocales supportedLocales) {
        // Creates the result
        DefaultValidationResult result = new DefaultValidationResult();

        // Local indexation
        IndexedBundleCollection indexedBundleCollection = new DefaultIndexedBundleCollection(supportedLocales, null);
        indexedBundleCollection.index(bundleCollection);

        // Get an inverted index: key -> locale -> value
        Map<String,Map<Locale,String>> indexByKeys = new TreeMap<String, Map<Locale, String>>();
        for (Locale locale : supportedLocales.getSupportedLocales()) {
            Map<String,String> keyValuesForLocale = indexedBundleCollection.getValues(locale);
            if (keyValuesForLocale == null) {
                result.add (ValidationMessageCategory.MISSING_LOCALE, locale);
            } else {
                for (Map.Entry<String, String> keyValue : keyValuesForLocale.entrySet()) {
                    String key = keyValue.getKey();
                    String value = keyValue.getValue();
                    Map<Locale, String> localeValues = indexByKeys.get(key);
                    if (localeValues == null) {
                        localeValues = new HashMap<Locale, String>();
                        indexByKeys.put(key, localeValues);
                    }
                    localeValues.put(locale, value);
                }
            }
        }

        // Missing keys in languages
        for (Map.Entry<String, Map<Locale, String>> keyLocaleValue : indexByKeys.entrySet()) {
            String key = keyLocaleValue.getKey();
            Map<Locale, String> localeValues = keyLocaleValue.getValue();
            for (Locale locale : supportedLocales.getSupportedLocales()) {
                String value = localeValues.get(locale);
                if (value == null) {
                    result.add(ValidationMessageCategory.MISSING_KEY_IN_LANGUAGE, key, locale);
                }
            }
        }

        // OK
        return result;

    }
}
