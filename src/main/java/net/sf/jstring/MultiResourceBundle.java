package net.sf.jstring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource bundle that scans several other resource bundles
 * 
 * @author Damien Coraboeuf
 */
public class MultiResourceBundle extends ResourceBundle {
	/**
	 * Logger
	 */
	private final static Logger log = LoggerFactory.getLogger(MultiResourceBundle.class);

	/**
	 * List of resource bundles
	 */
	private ArrayList<ResourceBundle> bundles = new ArrayList<ResourceBundle>();

	/**
	 * Locale
	 */
	private Locale locale;

	/**
	 * Constructor
	 * 
	 * @param l
	 *            Locale used for the bundle
	 * @param paths
	 *            List of paths for the different bundles
	 */
	public MultiResourceBundle(Locale l, Collection<String> paths) {
		this.locale = l;
		for (String path : paths) {
			ResourceBundle bundle = ResourceBundle.getBundle(path, l);
			log.debug("Adding path " + path + " for locale " + l);
			this.bundles.add(bundle);
		}
	}

	/**
	 * @see java.util.ResourceBundle#getKeys()
	 */
	@Override
	public Enumeration<String> getKeys() {
		Vector<String> keys = new Vector<String>();
		for (Iterator<ResourceBundle> i = this.bundles.iterator(); i.hasNext();) {
			ResourceBundle bundle = i.next();
			Enumeration<String> e = bundle.getKeys();
			while (e.hasMoreElements()) {
				keys.add(e.nextElement());
			}
		}
		return keys.elements();
	}

	/**
	 * @return Returns the locale.
	 */
	@Override
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * @see java.util.ResourceBundle#handleGetObject(String)
	 */
	@Override
	protected Object handleGetObject(String key) {
		for (Iterator<ResourceBundle> i = this.bundles.iterator(); i.hasNext();) {
			ResourceBundle bundle = i.next();
			try {
				Object value = bundle.getObject(key);
				if (value != null) {
					return value;
				}
			} catch (MissingResourceException ex) {
			}
		}
		return null;
	}

}
