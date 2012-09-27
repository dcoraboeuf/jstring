package net.sf.jstring;

import java.util.Locale;

/**
 * Interface implemented by any object that can gives a string representation
 * different according to a given locale.
 * 
 * @author Damien Coraboeuf
 */
public interface Localizable {

	/**
	 * Returns a message that is suitable for the given locale
	 *
     * @param strings Collection of bundles to use
	 * @param locale
	 *            Locale to get the message for.
	 * @return Message for the given locale
	 */
	String getLocalizedMessage(Strings strings, Locale locale);

}
