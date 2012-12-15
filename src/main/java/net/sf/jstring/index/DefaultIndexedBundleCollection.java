package net.sf.jstring.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import net.sf.jstring.SupportedLocales;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleCollection;
import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleSection;
import net.sf.jstring.model.BundleValue;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.Validate;

public class DefaultIndexedBundleCollection implements IndexedBundleCollection {

	private final AtomicReference<Map<Locale, Map<String, String>>> index = new AtomicReference<Map<Locale,Map<String,String>>>(new ConcurrentHashMap<Locale, Map<String, String>>());

	private final SupportedLocales supportedLocales;
	private final IndexedBundleCollectionOwner owner;
    private final AtomicReference<BundleCollection> bundleCollection = new AtomicReference<BundleCollection>();

	public DefaultIndexedBundleCollection(SupportedLocales supportedLocales, IndexedBundleCollectionOwner owner) {
		Validate.notNull(supportedLocales, "Supported locales are required");
		this.supportedLocales = supportedLocales;
		this.owner = owner;
	}

    @Override
    public SupportedLocales getSupportedLocales() {
        return supportedLocales;
    }
    
    @Override
    public boolean reload() {
    	return owner.reload(this);
    }

    @Override
	public void index(BundleCollection bundleCollection) {
		Map<Locale, Map<String, String>> result = new ConcurrentHashMap<Locale, Map<String,String>>();
		for (Bundle bundle : bundleCollection.getBundles()) {
			for (BundleSection section : bundle.getSections()) {
				for (BundleKey bundleKey : section.getKeys()) {
					String key = bundleKey.getName();
					for (Map.Entry<Locale, BundleValue> bundleValueEntry : bundleKey.getValues().entrySet()) {
                        Locale language = bundleValueEntry.getKey();
						String value = bundleValueEntry.getValue().getValue();
						Map<String, String> languageIndex = result.get(language);
						if (languageIndex == null) {
							languageIndex = new ConcurrentHashMap<String, String>();
							result.put(language, languageIndex);
						}
						languageIndex.put(key, value);
					}
				}
			}
		}
        this.bundleCollection.set(bundleCollection);
        this.index.set(result);
	}

    @Override
    public BundleCollection getBundleCollection () {
    	BundleCollection result = bundleCollection.get();
        if (result != null) {
            return result;
        } else {
            throw new IllegalStateException("No collection was indexed yet.");
        }
    }
	
	@Override
	public Map<String, String> getValues(Locale locale) {
		final Map<Locale, Map<String, String>> actualIndex = index.get();
		// Result
		Map<String, String> result = new HashMap<String, String>();
        // Target locale
        Locale targetLocale = supportedLocales.filterForLookup(locale);
		// List of locales
		List<Locale> locales = new ArrayList<Locale>(LocaleUtils.localeLookupList(targetLocale, supportedLocales.getDefaultLocale()));
		// Loops in reverse order
		Collections.reverse(locales);
		for (Locale currentLocale : locales) {
			// Gets the language map
			Map<String, String> map = actualIndex.get(currentLocale);
			// If map is defined, includes it
			if (map != null) {
				result.putAll(map);
			}
		}
		// OK
		return result;
	}

	@Override
	public String getValue(final Locale locale, String key) {
		final Map<Locale, Map<String, String>> actualIndex = index.get();
        // Target locale
        Locale targetLocale = supportedLocales.filterForLookup(locale);
		// List of locales
		List<Locale> locales = LocaleUtils.localeLookupList(targetLocale, supportedLocales.getDefaultLocale());
		for (Locale currentLocale : locales) {
			// Gets the language map
			Map<String, String> map = actualIndex.get(currentLocale);
			// If map is defined and contains the key
			if (map != null && map.containsKey(key)) {
				return map.get(key);
			}
		}
		// Nothing found
		return null;
	}

}
