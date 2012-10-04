package net.sf.jstring.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleCollection;
import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleSection;
import net.sf.jstring.model.BundleValue;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.Validate;

public class DefaultIndexedBundleCollection implements IndexedBundleCollection {

	private final ReadWriteLock indexLock = new ReentrantReadWriteLock();

	private final Map<String, Map<String, String>> index = new HashMap<String, Map<String, String>>();

	private final Locale defaultLocale;

	public DefaultIndexedBundleCollection(Locale defaultLocale) {
		Validate.notNull(defaultLocale, "Default locale is required");
		this.defaultLocale = defaultLocale;
	}

	@Override
	public void index(BundleCollection bundleCollection) {
		Lock lock = indexLock.writeLock();
		lock.lock();
		try {
			index.clear();
			for (Bundle bundle : bundleCollection.getBundles()) {
				for (BundleSection section : bundle.getSections()) {
					for (BundleKey bundleKey : section.getKeys()) {
						String key = bundleKey.getName();
						for (Map.Entry<String, BundleValue> bundleValueEntry : bundleKey.getValues().entrySet()) {
							String language = bundleValueEntry.getKey();
							String value = bundleValueEntry.getValue().getValue();
							Map<String, String> languageIndex = index.get(language);
							if (languageIndex == null) {
								languageIndex = new HashMap<String, String>();
								index.put(language, languageIndex);
							}
							languageIndex.put(key, value);
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public Map<String, String> getValues(Locale locale) {
		Lock lock = indexLock.readLock();
		lock.lock();
		try {
			// Result
			Map<String, String> result = new HashMap<String, String>();
			// List of locales
			List<Locale> locales = new ArrayList<Locale>(LocaleUtils.localeLookupList(locale, defaultLocale));
			// Loops in reverse order
			Collections.reverse(locales);
			for (Locale currentLocale : locales) {
				// Gets the string out of the locale
				String language = currentLocale.toString();
				// Gets the language map
				Map<String, String> map = index.get(language);
				// If map is defined, includes it
				if (map != null) {
					result.putAll(map);
				}
			}
			// OK
			return result;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String getValue(final Locale locale, String key) {
		Lock lock = indexLock.readLock();
		lock.lock();
		try {
			// List of locales
			List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
			for (Locale currentLocale : locales) {
				// Gets the string out of the locale
				String language = currentLocale.toString();
				// Gets the language map
				Map<String, String> map = index.get(language);
				// If map is defined and contains the key
				if (map != null && map.containsKey(key)) {
					return map.get(key);
				}
			}
			// Nothing found
			return null;
		} finally {
			lock.unlock();
		}
	}

}
