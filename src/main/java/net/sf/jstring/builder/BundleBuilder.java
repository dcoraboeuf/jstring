package net.sf.jstring.builder;

import java.util.ArrayList;
import java.util.List;

import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleSection;

import com.google.common.collect.ImmutableList;

public class BundleBuilder extends Builder<Bundle> {

	public static BundleBuilder create(String name) {
		return new BundleBuilder(name);
	}

	private final String name;
	private final List<String> comments = new ArrayList<String>();
	private final List<BundleSection> sections = new ArrayList<BundleSection>();

	private BundleBuilder(String name) {
		this.name = name;
	}
	
	public BundleBuilder comment (String comment) {
		comments.add(comment);
		return this;
	}
	
	public BundleBuilder section (BundleSection section) {
		sections.add(section);
		return this;
	}

	@Override
	public Bundle build() {
		return new Bundle(name, ImmutableList.copyOf(comments), ImmutableList.copyOf(sections));
	}

}
