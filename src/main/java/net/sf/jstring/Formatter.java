package net.sf.jstring;

import java.util.Locale;
import java.util.Map;

public interface Formatter {
	
	String format (Locale locale, String pattern, Map<String, ?> parameters);

}
