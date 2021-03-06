package net.sf.jstring.builder;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleSection;

import java.util.LinkedHashMap;
import java.util.Map;

public class BundleBuilder extends AbstractBuilderCommented<Bundle, BundleBuilder> {

	public static BundleBuilder create(String name) {
		return new BundleBuilder(name);
	}

	private final String name;
	private final Map<String, BundleSectionBuilder> sections = new LinkedHashMap<String, BundleSectionBuilder>();

	private BundleBuilder(String name) {
		this.name = name;
	}
	
	public BundleBuilder section (BundleSectionBuilder section) {
		sections.put(section.getName(), section);
		return this;
	}

	@Override
	public Bundle build() {
		return new Bundle(
                name,
                ImmutableList.copyOf(getComments()),
                ImmutableList.copyOf(
                        Collections2.transform(
                                sections.values(),
                                BundleSectionBuilder.buildFn)));
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

    public void merge(BundleBuilder builder, BundleValueMergeMode mode) {
        merge(builder.build(), mode);
    }

    @Override
    public void merge(Bundle bundle, BundleValueMergeMode mode) {
        // Comments
        mergeComments(bundle.getComments());
        // Sections
    	mergeSections(bundle.getSections(), mode);
    }

    private void mergeSections(ImmutableList<BundleSection> sourceSections, BundleValueMergeMode mode) {
    	for (BundleSection sourceSection : sourceSections) {
			String name = sourceSection.getName();
			BundleSectionBuilder section = sections.get(name);
			if (section != null) {
				section.merge(sourceSection, mode);
			} else {
				BundleSectionBuilder sectionBuilder = BundleSectionBuilder.create(name);
				sectionBuilder.merge(sourceSection, mode);
				section(sectionBuilder);
			}
		}
	}

    @Override
    public String toString() {
        return "BundleBuilder{" +
                "name='" + name + '\'' +
                ", sections=" + sections +
                '}';
    }
}
