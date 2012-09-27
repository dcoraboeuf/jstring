package net.sf.jstring;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * String server.
 *
 * @author Damien Coraboeuf
 */
public class Strings {
    /**
     * Logging
     */
    private static final Logger log = LoggerFactory.getLogger(Strings.class);

    /**
     * All paths
     */
    private LinkedList<String> paths = new LinkedList<String>();

    /**
     * List of resource bundle indexed by locales.
     */
    private Map<Locale, ResourceBundle> resources = new ConcurrentHashMap<Locale, ResourceBundle>();

    /**
     * Custom properties, added at runtime
     */
    private Map<String, String> localProperties = new ConcurrentHashMap<String, String>();

    /**
     * Status of the collection of bundle.
     */
    private boolean locked;

    /**
     * No bundle associated with this instance
     */
    public Strings() {
    }

    /**
     * Pre-defined list of bundles
     */
    public Strings(String... paths) {
        for (String path : paths) {
            add(path);
        }
    }

    /**
     * Pre-defined list of bundles
     */
    public Strings(Collection<String> paths) {
        for (String path : paths) {
            add(path);
        }
    }

    /**
     * Pre-defined list of bundles and lock
     */
    public Strings(boolean locked, String... paths) {
        for (String path : paths) {
            add(path);
        }
        this.locked = locked;
    }

    /**
     * Pre-defined list of bundles and lock
     */
    public Strings(boolean locked, Collection<String> paths) {
        for (String path : paths) {
            add(path);
        }
        this.locked = locked;
    }

    /**
     * Checks the status of this collection of bundles
     *
     * @return true if the collection cannot be extended any longer
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Locks this collection of bundles, so it cannot be extended
     * any longer.
     */
    public void lock() {
        locked = true;
    }

    /**
     * Checks the lock satus and raises an IllegalStateException
     * if the collection is locked.
     */
    protected void lockCheck() {
        if (locked) {
            throw new IllegalStateException("The Strings collection is locked and cannot be extended any longer.");
        }
    }

    /**
     * Adds a supplementary resource path
     *
     * @param path Bundle's name to be added.
     */
    public synchronized void add(String path) {
        lockCheck();
        log.info("Add I18N String path " + path);
        paths.offer(path);
        resources.clear();
    }

    /**
     * Adds a custom string.
     *
     * @param code  Code of the string
     * @param value Value for the code
     */
    public synchronized void add(String code, String value) {
        lockCheck();
        localProperties.put(code, value);
    }

    /**
     * Adds a supplementary resource path so it is the first one in the queue
     *
     * @param path Bundle's name to be added.
     */
    public synchronized void addFirst(String path) {
        lockCheck();
        log.info("Add I18N String path " + path + " at beginning");
        paths.addFirst(path);
        resources.clear();
    }

    /**
     * Completes the value with other codes. It means that if the located string
     * contains references to other codes, those ones are expanded. References
     * are expressed using @[<i>code</i>] syntax.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param value  Value to expand
     * @return Expanded string
     */
    private String complete(Locale locale, String value) {
        int pos = value.indexOf("@[");
        if (pos >= 0) {
            StringBuilder buffer = new StringBuilder();
            int oldpos = 0;
            while ((pos = value.indexOf("@[", oldpos)) >= 0) {
                int endpos = value.indexOf(']', pos + 2);
                if (endpos > 0) {
                    String key = value.substring(pos + 2, endpos);
                    String systemValue = get(locale, key);
                    if (systemValue != null) {
                        buffer.append(value.substring(oldpos, pos));
                        buffer.append(systemValue);
                        oldpos = endpos + 1;
                    } else {
                        buffer.append(value.substring(oldpos, endpos + 1));
                        oldpos = endpos + 1;
                    }
                } else {
                    pos = -1;
                }
            }
            buffer.append(value.substring(oldpos));
            return buffer.toString();
        } else {
            return value;
        }
    }

    /**
     * Get a string by its code
     *
     * @param locale    Locale to use
     * @param code      Code as a non-null object. toString will be used.
     * @param mandatory If the string must be found
     * @return The string if found, <code>null</code> if not mandatory, the code
     *         itself otherwise
     */
    public String get(Locale locale, Object code, boolean mandatory) {
        Validate.notNull(code, "The code cannot be null.");
        return get(locale, code.toString(), mandatory);
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
    public String get(Locale locale, String code, boolean mandatory) {
        // Get the local property if any
        String localValue = localProperties.get(code);
        if (localValue != null) {
            return localValue;
        }
        // Get the associated bundle
        ResourceBundle bundle = getBundle(locale);
        try {
            String value = bundle.getString(code);
            // Value cannot be null
            value = complete(locale, value);
            return value;
        } catch (MissingResourceException ex) {
            if (mandatory) {
                log.warn("String key not found\t" + code);
                return code;
            } else {
                return null;
            }
        }
    }

    /**
     * Get a string using a code and some parameters.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param code   Code as a non-null object. toString will be used.
     * @param params Parameters for the string
     * @return Corresponding string
     * @see #get(java.util.Locale, String, boolean)
     */
    public String get(Locale locale, Object code, Object... params) {
        Validate.notNull(code, "The code cannot be null.");
        return get(locale, code.toString(), params);
    }

    /**
     * Get a string using a code and some parameters.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param code   Code of the string
     * @param params Parameters for the string
     * @return Corresponding string
     * @see #get(java.util.Locale, String, boolean)
     */
    public String get(Locale locale, String code, Object... params) {
        // Default locale is null
        if (locale == null) {
            locale = Locale.getDefault();
        }
        String pattern = get(locale, code, true);
        if (pattern.indexOf('{') >= 0
                && (pattern.indexOf("${") < 0 || pattern.indexOf("$${") >= 0)) {
            // Replaces $${ by ${
            pattern = pattern.replace("$${", "${");
            // Replace each occurrence by its localized form if possible
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof Localizable) {
                        Localizable localizable = (Localizable) param;
                        String value = localizable.getLocalizedMessage(this, locale);
                        params[i] = value;
                    }
                }
            }
            // Computes message
            MessageFormat messageFormat = new MessageFormat(pattern, locale);
            String message = messageFormat.format(params);
            // OK
            return message;
        } else {
            return pattern;
        }
    }

    /**
     * Get a bundle for a locale
     *
     * @param locale Locale used for the search. If <code>null</code>, default
     *               locale is used.
     * @return Resource bundle
     */
    private ResourceBundle getBundle(Locale locale) {
        // Default locale is null
        if (locale == null) {
            locale = Locale.getDefault();
        }
        // Find in cache
        ResourceBundle bundle = resources.get(locale);
        if (bundle == null) {
            return loadBundle(locale);
        } else {
            return bundle;
        }
    }

    /**
     * Checks if the <code>key</code> is defined in the given
     * <code>locale</code>.
     *
     * @param locale Locale to check
     * @param key    Key to check
     * @return <code>true</code> if the key is defined
     */
    public boolean isDefined(Locale locale, String key) {
        String value = get(locale, key, false);
        return value != null;
    }

    /**
     * Load a bundle
     *
     * @param locale Locale to be used.
     * @return An instance of <code>MultiResourceBundle</code>
     */
    private synchronized ResourceBundle loadBundle(Locale locale) {
        MultiResourceBundle bundle = new MultiResourceBundle(locale, paths);
        resources.put(locale, bundle);
        return bundle;
    }

    /**
     * Loads a map of key/values for a locale. The returned map is sorted on keys.
     *
     * @param locale Locale to get the values for
     * @return Map of key/values for this locale
     */
    public synchronized Map<String, String> getKeyValues(Locale locale) {
        // Gets the bundle for this locale
        ResourceBundle bundle = loadBundle(locale);
        // Fills a map
        Map<String, String> map = new TreeMap<String, String>();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = bundle.getString(key);
            value = complete(locale, value);
            map.put(key, value);
        }
        // Overrides with local properties
        if (localProperties != null) {
            map.putAll(localProperties);
        }
        // OK
        return map;
    }

}
