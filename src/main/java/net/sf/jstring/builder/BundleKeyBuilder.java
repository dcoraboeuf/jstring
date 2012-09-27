package net.sf.jstring.builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleValue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class BundleKeyBuilder extends Builder<BundleKey> {

	public static BundleKeyBuilder create(String name) {
		return new BundleKeyBuilder(name);
	}

	private final String name;
	private final List<String> comments = new ArrayList<String>();
	private final Map<String, BundleValue> values = new LinkedHashMap<String, BundleValue>();

	private BundleKeyBuilder(String name) {
		this.name = name;
	}

	public BundleKeyBuilder comment(String comment) {
		comments.add(comment);
		return this;
	}

	public BundleKeyBuilder value(String language, BundleValue value) {
		values.put(language, value);
		return this;
	}

	@Override
	public BundleKey build() {
		return new BundleKey(name, ImmutableList.copyOf(comments), ImmutableMap.copyOf(values));
	}

}
