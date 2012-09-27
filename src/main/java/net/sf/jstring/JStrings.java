package net.sf.jstring;

import java.util.Locale;
import java.util.Map;

/**
 * String server.
 *
 * @author Damien Coraboeuf
 */
public abstract class JStrings {

    /**
     * Static collection
     */
    private static Strings STRINGS = new Strings();

    /**
     * Gets the underlying collection
     */
    public static Strings getStaticCollection() {
        return STRINGS;
    }

    /**
     * Sets the underlying collection
     *
     * @param collection The collection to use
     */
    public static synchronized void setStaticCollection(Strings collection) {
        JStrings.STRINGS = collection;
    }

    /**
     * Access to the underlying collection
     */


    private JStrings() {
    }

    /**
     * Adds a supplementary resource path
     *
     * @param path Bundle's name to be added.
     */
    public static void add(String path) {
        STRINGS.add(path);
    }

    /**
     * Adds a custom string.
     *
     * @param code  Code of the string
     * @param value Value for the code
     */
    public static void add(String code, String value) {
        STRINGS.add(code, value);
    }

    /**
     * Adds a supplementary resource path so it is the first one in the queue
     *
     * @param path Bundle's name to be added.
     */
    public static void addFirst(String path) {
        STRINGS.addFirst(path);
    }

    /**
     * Get a string by its code
     *
     * @param locale    Locale to use
     * @param code      Code using a non-null object. toString will be used.
     * @param mandatory If the string must be found
     * @return The string if found, <code>null</code> if not mandatory, the code
     *         itself otherwise
     */
    public static String get(Locale locale, Object code, boolean mandatory) {
        return STRINGS.get(locale, code, mandatory);
    }

    /**
     * Get a string by its code
     *
     * @param locale    Locale to use
     * @param code      Code
     * @param mandatory If the string must be found
     * @return The string if found, <code>null</code> if not mandatory, the code
     *         itself otherwise
     */
    public static String get(Locale locale, String code, boolean mandatory) {
        return STRINGS.get(locale, code, mandatory);
    }

    /**
     * Get a string using a code and some parameters.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param code   Code using a non-null object. toString will be used.
     * @param params Parameters for the string
     * @return Corresponding string
     * @see #get(Locale, String, boolean)
     */
    public static String get(Locale locale, Object code, Object... params) {
        return STRINGS.get(locale, code, params);
    }

    /**
     * Get a string using a code and some parameters.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param code   Code of the string
     * @param params Parameters for the string
     * @return Corresponding string
     * @see #get(Locale, String, boolean)
     */
    public static String get(Locale locale, String code, Object... params) {
        return STRINGS.get(locale, code, params);
    }

    /**
     * Checks if the <code>key</code> is defined in the given
     * <code>locale</code>.
     *
     * @param locale Locale to check
     * @param key    Key to check
     * @return <code>true</code> if the key is defined
     */
    public static boolean isDefined(Locale locale, String key) {
        return STRINGS.isDefined(locale, key);
    }

    /**
     * Loads a map of key/values for a locale. The returned map is sorted on keys.
     *
     * @param locale Locale to get the values for
     * @return Map of key/values for this locale
     * @see Strings#getKeyValues(java.util.Locale)
     */
    public synchronized Map<String, String> getKeyValues(Locale locale) {
        return STRINGS.getKeyValues(locale);
    }

}
