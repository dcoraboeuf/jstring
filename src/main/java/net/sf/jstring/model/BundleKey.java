package net.sf.jstring.model;

import lombok.Data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@Data
public class BundleKey {

	private final String name;
	private final ImmutableList<String> comments;
	private final ImmutableMap<String, BundleValue> values;

}
