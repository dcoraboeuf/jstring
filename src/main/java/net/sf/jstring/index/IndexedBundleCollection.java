package net.sf.jstring.index;

import java.util.Locale;
import java.util.Map;

import net.sf.jstring.SupportedLocales;
import net.sf.jstring.model.BundleCollection;

public interface IndexedBundleCollection {

    SupportedLocales getSupportedLocales();
	
	void index (BundleCollection bundleCollection);

    BundleCollection getBundleCollection ();
	
	String getValue (Locale locale, String key);

	Map<String, String> getValues(Locale locale);

	boolean reload();

    /**
     * Returns the last update time.
     * @return Last update time in ms (see {@link System#currentTimeMillis()}.
     * @see System#currentTimeMillis()
     */
	long getLastUpdateTime();

}
