package net.sf.jstring.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.sf.jstring.model.BundleSection;

import java.util.ArrayList;
import java.util.List;

public class BundleSectionBuilder extends AbstractBuilderCommented<BundleSection, BundleSectionBuilder> {

    public static Function<? super BundleSectionBuilder,BundleSection> buildFn = new Function<BundleSectionBuilder, BundleSection>() {
        @Override
        public BundleSection apply(BundleSectionBuilder bundleSectionBuilder) {
            return bundleSectionBuilder.build();
        }
    };

    public static BundleSectionBuilder create(String name) {
		return new BundleSectionBuilder(name);
	}

	private final String name;
	private final List<BundleKeyBuilder> keys = new ArrayList<BundleKeyBuilder>();

	private BundleSectionBuilder(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public BundleSectionBuilder key (BundleKeyBuilder key) {
		keys.add(key);
		return this;
	}

	@Override
	public BundleSection build() {
		return new BundleSection(
                name,
                ImmutableList.copyOf(getComments()),
                ImmutableList.copyOf(
                    Lists.transform(keys, BundleKeyBuilder.buildFn)
                ));
	}

    @Override
	public void merge(BundleSection section) {
        super.merge(section);
		// FIXME Implement BundleSectionBuilder.merge
		
	}

}
