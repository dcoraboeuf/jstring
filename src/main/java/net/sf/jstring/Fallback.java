package net.sf.jstring;

import java.util.Locale;
import java.util.Map;

public interface Fallback {

	String whenNotFound(Locale locale, Object code, Map<String, ?> params);

}
