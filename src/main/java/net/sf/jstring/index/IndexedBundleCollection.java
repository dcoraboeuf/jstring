package net.sf.jstring.index;

import java.util.Locale;
import java.util.Map;

import net.sf.jstring.model.BundleCollection;

public interface IndexedBundleCollection {
	
	void index (BundleCollection bundleCollection);
	
	String getValue (Locale locale, String key);

	Map<String, String> getValues(Locale locale);

}
