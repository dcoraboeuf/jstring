package net.sf.jstring.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.sf.jstring.model.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BundleBuilder extends Builder<Bundle> {

	public static BundleBuilder create(String name) {
		return new BundleBuilder(name);
	}

	private final String name;
	private final List<String> comments = new ArrayList<String>();
	private final List<BundleSectionBuilder> sections = new ArrayList<BundleSectionBuilder>();

    private BundleSectionBuilder defaultSectionBuilder;

	private BundleBuilder(String name) {
		this.name = name;
	}
	
	public BundleBuilder comment (String comment) {
		comments.add(comment);
		return this;
	}
	
	public BundleBuilder section (BundleSectionBuilder section) {
		sections.add(section);
		return this;
	}

	@Override
	public Bundle build() {
        if (defaultSectionBuilder != null) {
            sections.add(0, defaultSectionBuilder);
        }
		return new Bundle(name, ImmutableList.copyOf(comments), ImmutableList.copyOf(Lists.transform(sections, BundleSectionBuilder.buildFn)));
	}

    public BundleSectionBuilder getDefaultSectionBuilder() {
        if (defaultSectionBuilder == null) {
            defaultSectionBuilder = BundleSectionBuilder.create(Bundle.DEFAULT_SECTION);
        }
        return defaultSectionBuilder;
    }

    public void merge(BundleBuilder builder) {
        merge(builder.build());
    }

    protected void merge(Bundle bundle) {
        // Comments
    	mergeComments(bundle.getComments());
        // TODO Top keys
        // TODO Sections
    }

    // TODO Uses an abstract BundleCommented level
	private void mergeComments(Collection<String> source) {
		// Converts the target as a set (preserving order)
		Set<String> targetSet = new LinkedHashSet<String>(comments);
		// Adds all items of the source if they are not there yet
		List<String> itemsToAdd = new ArrayList<String>();
		for (String e: source) {
			if (!targetSet.contains(e)) {
				itemsToAdd.add(e);
			}
		}
		// Adds those new items to the target
		comments.addAll(itemsToAdd);
	}
}
