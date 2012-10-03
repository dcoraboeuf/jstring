package net.sf.jstring.index;

import java.util.Locale;

import net.sf.jstring.model.BundleCollection;

public interface IndexedBundleCollection {
	
	void index (BundleCollection bundleCollection);
	
	String getValue (Locale locale, String key);

}
