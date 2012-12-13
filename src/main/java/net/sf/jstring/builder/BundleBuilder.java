package net.sf.jstring.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jstring.model.Bundle;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

public class BundleBuilder extends Builder<Bundle> {

	public static BundleBuilder create(String name) {
		return new BundleBuilder(name);
	}

	private final String name;
	private final List<String> comments = new ArrayList<String>();
	private final Map<String, BundleSectionBuilder> sections = new LinkedHashMap<String, BundleSectionBuilder>();

	private BundleBuilder(String name) {
		this.name = name;
	}
	
	public BundleBuilder comment (String comment) {
		comments.add(comment);
		return this;
	}
	
	public BundleBuilder section (BundleSectionBuilder section) {
		sections.put(section.getName(), section);
		return this;
	}

	@Override
	public Bundle build() {
		return new Bundle(name, ImmutableList.copyOf(comments), ImmutableList.copyOf(Collections2.transform(sections.values(), BundleSectionBuilder.buildFn)));
	}

    public BundleSectionBuilder getDefaultSectionBuilder() {
    	BundleSectionBuilder section = sections.get(Bundle.DEFAULT_SECTION);
    	if (section != null) {
    		return section;
    	} else {
    		section = BundleSectionBuilder.create(Bundle.DEFAULT_SECTION);
    		section(section);
    		return section;
    	}
    }

    public void merge(BundleBuilder builder) {
        merge(builder.build());
    }

    protected void merge(Bundle bundle) {
        // Comments
    	mergeComments(bundle.getComments());
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
