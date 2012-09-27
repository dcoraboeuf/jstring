package net.sf.jstring.builder;

import java.util.ArrayList;
import java.util.List;

import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleSection;

import com.google.common.collect.ImmutableList;

public class BundleSectionBuilder extends Builder<BundleSection> {

	public static BundleSectionBuilder create(String name) {
		return new BundleSectionBuilder(name);
	}

	private final String name;
	private final List<String> comments = new ArrayList<String>();
	private final List<BundleKey> keys = new ArrayList<BundleKey>();

	private BundleSectionBuilder(String name) {
		this.name = name;
	}
	
	public BundleSectionBuilder comment (String comment) {
		comments.add(comment);
		return this;
	}
	
	public BundleSectionBuilder key (BundleKey key) {
		keys.add(key);
		return this;
	}

	@Override
	public BundleSection build() {
		return new BundleSection(name, ImmutableList.copyOf(comments), ImmutableList.copyOf(keys));
	}

}
