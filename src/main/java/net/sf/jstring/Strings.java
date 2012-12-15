package net.sf.jstring;

import net.sf.jstring.model.BundleCollection;

import java.util.Locale;
import java.util.Map;

/**
 * String server.
 *
 * @author Damien Coraboeuf
 */
public interface Strings {

    /**
     * Get a string using a code and some parameters.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param key   Code of the string
     * @param params Parameters for the string
     * @return Corresponding string
     * @see #get(java.util.Locale, String, boolean)
     */
    String get(Locale locale, Object key, Object... params);

    /**
     * Get a string using a code and some parameters.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param key   Code of the string
     * @param params Parameters for the string
     * @return Corresponding string
     * @see #get(java.util.Locale, String, boolean)
     */
    String get(Locale locale, Object key, Map<String, ?> params);

    /**
     * Checks if the <code>key</code> is defined in the given
     * <code>locale</code>.
     *
     * @param locale Locale to check
     * @param key    Key to check
     * @return <code>true</code> if the key is defined
     */
    boolean isDefined(Locale locale, Object key);

    /**
     * Loads a map of key/values for a locale. The returned map is sorted on keys.
     *
     * @param locale Locale to get the values for
     * @return Map of key/values for this locale
     */
    Map<String, String> getKeyValues(Locale locale);

    /**
     * Gets the underlying bundle collection
     */
    BundleCollection getBundleCollection();
    
    /**
     * Reloads the strings from their source.
     * @return <code>true</code> if the reload was successful.
     */
    boolean reload();

}
