package net.sf.jstring.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.sf.jstring.model.Bundle;

import java.util.ArrayList;
import java.util.List;

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
            defaultSectionBuilder = BundleSectionBuilder.create("default");
        }
        return defaultSectionBuilder;
    }

    public void merge(BundleBuilder builder) {
        merge(builder.build());
    }

    protected void merge(Bundle bundle) {
        // TODO Comments
        // TODO Top keys
        // TODO Sections
    }
}
