package net.sf.jstring.index;

import net.sf.jstring.SupportedLocales;
import net.sf.jstring.model.*;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultIndexedBundleCollection implements IndexedBundleCollection {

	private final ReadWriteLock indexLock = new ReentrantReadWriteLock();

	private final Map<Locale, Map<String, String>> index = new HashMap<Locale, Map<String, String>>();

	private final SupportedLocales supportedLocales;
    private BundleCollection bundleCollection;

	public DefaultIndexedBundleCollection(SupportedLocales supportedLocales) {
		Validate.notNull(supportedLocales, "Supported locales are required");
		this.supportedLocales = supportedLocales;
	}

    @Override
    public SupportedLocales getSupportedLocales() {
        return supportedLocales;
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
						for (Map.Entry<Locale, BundleValue> bundleValueEntry : bundleKey.getValues().entrySet()) {
                            Locale language = bundleValueEntry.getKey();
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
            this.bundleCollection = bundleCollection;
		} finally {
			lock.unlock();
		}
	}

    @Override
    public BundleCollection getBundleCollection () {
        if (bundleCollection != null) {
            return bundleCollection;
        } else {
            throw new IllegalStateException("No collection was indexed yet.");
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
			List<Locale> locales = new ArrayList<Locale>(LocaleUtils.localeLookupList(locale, supportedLocales.getDefaultLocale()));
			// Loops in reverse order
			Collections.reverse(locales);
			for (Locale currentLocale : locales) {
				// Gets the language map
				Map<String, String> map = index.get(currentLocale);
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
			List<Locale> locales = LocaleUtils.localeLookupList(locale, supportedLocales.getDefaultLocale());
			for (Locale currentLocale : locales) {
				// Gets the language map
				Map<String, String> map = index.get(currentLocale);
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
