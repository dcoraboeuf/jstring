package net.sf.jstring.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.sf.jstring.model.BundleSection;

import java.util.ArrayList;
import java.util.List;

public class BundleSectionBuilder extends Builder<BundleSection> {

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
	private final List<String> comments = new ArrayList<String>();
	private final List<BundleKeyBuilder> keys = new ArrayList<BundleKeyBuilder>();

	private BundleSectionBuilder(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public BundleSectionBuilder comment (String comment) {
		comments.add(comment);
		return this;
	}
	
	public BundleSectionBuilder key (BundleKeyBuilder key) {
		keys.add(key);
		return this;
	}

	@Override
	public BundleSection build() {
		return new BundleSection(name, ImmutableList.copyOf(comments), ImmutableList.copyOf(
                Lists.transform(keys, BundleKeyBuilder.buildFn)
                ));
	}

}
