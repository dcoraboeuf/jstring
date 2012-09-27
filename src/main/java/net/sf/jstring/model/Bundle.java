package net.sf.jstring.model;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class Bundle {
	
	private final String name;
	private final ImmutableList<String> comments;
	private final ImmutableList<BundleSection> sections;

}
