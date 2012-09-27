package net.sf.jstring.builder;

import java.util.ArrayList;
import java.util.List;

import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleCollection;

import com.google.common.collect.ImmutableList;

public class BundleCollectionBuilder extends Builder<BundleCollection> {
	
	private final List<Bundle> bundles = new ArrayList<Bundle>();

	public static BundleCollectionBuilder create() {
		return new BundleCollectionBuilder();
	}
	
	private BundleCollectionBuilder() {
	}
	
	public BundleCollectionBuilder bundle (Bundle bundle) {
		bundles.add(bundle);
		return this;
	}

	@Override
	public BundleCollection build() {
		return new BundleCollection(ImmutableList.copyOf(bundles));
	}

}
