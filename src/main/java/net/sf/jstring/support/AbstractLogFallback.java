package net.sf.jstring.support;

import java.util.Locale;
import java.util.Map;

import net.sf.jstring.Fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLogFallback implements Fallback {
	
	private final Logger logger = LoggerFactory.getLogger(Fallback.class);

	@Override
	public String whenNotFound(Locale locale, Object code, Map<String, ?> params) {
		log(locale, code, params);
		return valueWhenNotFound(locale, code, params);
	}

	protected void log(Locale locale, Object code, Map<String, ?> params) {
		logger.warn("[strings] Could not find key [{}] for locale [{}]", code, locale);
	}

	protected abstract String valueWhenNotFound(Locale locale, Object code, Map<String, ?> params);

}
