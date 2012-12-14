package net.sf.jstring.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.util.Locale;

@Data
public class BundleKey implements Commented {

	private final String name;
	private final ImmutableList<String> comments;
	private final ImmutableMap<Locale, BundleValue> values;

}
