package net.sf.jstring.builder;

import java.util.ArrayList;
import java.util.List;

import net.sf.jstring.model.BundleValue;

import com.google.common.collect.ImmutableList;

public class BundleValueBuilder extends Builder<BundleValue> {

	public static BundleValueBuilder create(String name) {
		return new BundleValueBuilder(name);
	}

	private final String value;
	private final List<String> comments = new ArrayList<String>();

	private BundleValueBuilder(String value) {
		this.value = value;
	}

	public BundleValueBuilder comment(String comment) {
		comments.add(comment);
		return this;
	}

	@Override
	public BundleValue build() {
		return new BundleValue(ImmutableList.copyOf(comments), value);
	}

}
