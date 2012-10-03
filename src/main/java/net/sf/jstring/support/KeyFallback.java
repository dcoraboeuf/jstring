package net.sf.jstring.support;

import java.util.Locale;
import java.util.Map;

public class KeyFallback extends AbstractLogFallback {

	@Override
	protected String valueWhenNotFound(Locale locale, Object code, Map<String, ?> params) {
		return String.valueOf(code);
	}

}
