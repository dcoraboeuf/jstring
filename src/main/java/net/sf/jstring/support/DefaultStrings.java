package net.sf.jstring.support;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import net.sf.jstring.Fallback;
import net.sf.jstring.Formatter;
import net.sf.jstring.Localizable;
import net.sf.jstring.Strings;
import net.sf.jstring.index.IndexedBundleCollection;
import net.sf.jstring.model.BundleCollection;
import org.apache.commons.lang3.Validate;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String server.
 *
 * @author Damien Coraboeuf
 */
public class DefaultStrings implements Strings {
	
	/**
	 * Substitution pattern
	 */
	private final Pattern substitutionPattern = Pattern.compile("@\\[(.+)\\]");  

    /**
     * Index
     */
    private final IndexedBundleCollection indexedBundleCollection;
    
    /**
     * Fallback
     */
    private final Fallback fallback;
    
    /**
     * Formatter
     */
    private final Formatter formatter;

    /**
     * No bundle associated with this instance
     */
    public DefaultStrings(IndexedBundleCollection indexedBundleCollection) {
    	this(indexedBundleCollection, new SubstituteFallback(), new DefaultFormatter());
    }

    /**
     * No bundle associated with this instance
     */
    public DefaultStrings(IndexedBundleCollection indexedBundleCollection, Fallback fallback, Formatter formatter) {
    	this.indexedBundleCollection = indexedBundleCollection;
    	this.fallback = fallback;
    	this.formatter = formatter;
    }
    
    @Override
    public boolean reload() {
    	return indexedBundleCollection.reload();
    }

    @Override
    public BundleCollection getBundleCollection() {
        return indexedBundleCollection.getBundleCollection();
    }
    
    @Override
    public String get(Locale locale, Object key, Object... params) {
    	Map<String, Object> map = new LinkedHashMap<String, Object>();
    	if (params != null) {
	    	for (int i = 0; i < params.length; i++) {
				Object param = params[i];
				map.put(String.valueOf(i), param);
			}
    	}
    	return get (locale, key, map);
    }

    /**
     * Get a string using a code and some parameters.
     *
     * @param locale Locale to be used (default locale is <code>null</code>).
     * @param code   Code as a non-null object. toString will be used.
     * @param params Parameters for the string
     * @return Corresponding string
     * @see IndexedBundleCollection#getValue(java.util.Locale, String)
     */
    @Override
    public String get(Locale locale, Object code, Map<String, ?> params) {
    	// Validation
        Validate.notNull(code, "The code cannot be null.");
        // Default locale is null
        final Locale localeToUse;
        if (locale == null) {
            localeToUse = indexedBundleCollection.getSupportedLocales().getDefaultLocale();
        } else {
        	localeToUse = locale;
        }
        // Gets the code as a string
        String key = code.toString();
        // Gets the pattern
        String pattern = indexedBundleCollection.getValue(localeToUse, key);
        // Pattern not found
        if (pattern == null) {
        	// Fallback
        	return fallback.whenNotFound (localeToUse, code, params);
        }
        // Recursivity (@[...])
        pattern = resolve (localeToUse, pattern);
        // Replace each parameter by its localized form if possible
        if (params != null) {
        	params = Maps.transformValues(params, new Function<Object, Object>() {
        		@Override
        		public Object apply (Object param) {
                    if (param instanceof Localizable) {
                        Localizable localizable = (Localizable) param;
                        String value = localizable.getLocalizedMessage(DefaultStrings.this, localeToUse);
                        return value;
                    } else {
                    	return param;
                    }
        		}
			});
        }
        // Formats the message
        return format (localeToUse, pattern, params);
    }
    
    protected String resolve(Locale locale, String pattern) {
    	Matcher matcher = substitutionPattern.matcher(pattern);
    	StringBuffer buffer = new StringBuffer();
    	while (matcher.find()) {
    		String code = matcher.group(1);
    		String replacement = get(locale, code);
    		matcher.appendReplacement(buffer, replacement);
    	}
    	matcher.appendTail(buffer);
    	return buffer.toString();
	}

	protected String format (Locale locale, String pattern, Map<String, ?> parameters) {
		return formatter.format (locale, pattern, parameters);
    }

    /**
     * Checks if the <code>key</code> is defined in the given
     * <code>locale</code>.
     *
     * @param locale Locale to check
     * @param code    Key to check
     * @return <code>true</code> if the key is defined
     */
    @Override
    public boolean isDefined(Locale locale, Object code) {
    	// Validation
        Validate.notNull(code, "The code cannot be null.");
        // Default locale is null
        if (locale == null) {
            locale = indexedBundleCollection.getSupportedLocales().getDefaultLocale();
        }
        // Gets the code as a string
        String key = code.toString();
        // Gets the pattern
        String pattern = indexedBundleCollection.getValue(locale, key);
        // Check
        return pattern != null;
    }

    /**
     * Loads a map of key/values for a locale. The returned map is sorted on keys.
     *
     * @param locale Locale to get the values for
     * @return Map of key/values for this locale
     */
    @Override
    public synchronized Map<String, String> getKeyValues(Locale locale) {
        // Default locale is null
        if (locale == null) {
            locale = indexedBundleCollection.getSupportedLocales().getDefaultLocale();
        }
        // Gets all values
        Map<String, String> map = indexedBundleCollection.getValues (locale);
        // OK
        return map;
    }

}