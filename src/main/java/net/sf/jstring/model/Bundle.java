package net.sf.jstring.model;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class Bundle implements Commented {

	public static final String DEFAULT_SECTION = "default";
	
	private final String name;
	private final ImmutableList<String> comments;
	private final ImmutableList<BundleSection> sections;

}
