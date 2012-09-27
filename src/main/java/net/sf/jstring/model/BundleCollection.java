package net.sf.jstring.model;

import lombok.Data;

import com.google.common.collect.ImmutableList;

@Data
public class BundleCollection {

	private final ImmutableList<Bundle> bundles;
	
}
