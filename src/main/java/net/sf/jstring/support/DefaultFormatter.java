package net.sf.jstring.support;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

import net.sf.jstring.Formatter;

public class DefaultFormatter implements Formatter {
	
	private final Pattern replacementPattern = Pattern.compile("\\{([^\\}]+)\\}");

	@Override
	public String format(Locale locale, String pattern, Map<String, ?> parameters) {
		Validate.notNull(locale, "Locale is required");
		Validate.notNull(pattern, "Pattern is required");
		Validate.notNull(parameters, "Parameters are required");
		StringBuffer buffer = new StringBuffer();
		Matcher matcher = replacementPattern.matcher(pattern);
		while (matcher.find()) {
			String name = matcher.group(1);
			String replacement = getReplacement (locale, parameters, name);
			matcher.appendReplacement(buffer, replacement);
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	protected String getReplacement(Locale locale, Map<String, ?> parameters, String name) {
		Validate.notNull(locale, "Locale is required");
		Validate.notNull(parameters, "Parameters are required");
		Validate.notBlank(name, "Name is required");
		Object value = parameters.get(name);
		if (value == null) {
			return getDefaultReplacement (locale, name);
		} else {
			return ObjectUtils.toString(value, "");
		}
	}

	protected String getDefaultReplacement(Locale locale, String name) {
		Validate.notNull(locale, "Locale is required");
		Validate.notBlank(name, "Name is required");
		return String.format("##%s##", name);
	}

}
