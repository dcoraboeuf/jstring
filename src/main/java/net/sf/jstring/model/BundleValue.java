package net.sf.jstring.model;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class BundleValue {

	public static BundleValue value(String value) {
		return new BundleValue(ImmutableList.<String> of(), value);
	}

	private final ImmutableList<String> comments;
	private final String value;

}
