package net.sf.jstring.model;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class BundleValue implements Commented {

	private final ImmutableList<String> comments;
	private final String value;

}
