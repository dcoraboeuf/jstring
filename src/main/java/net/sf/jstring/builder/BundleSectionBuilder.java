package net.sf.jstring.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleSection;

import java.util.LinkedHashMap;
import java.util.Map;

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
	private final Map<String,BundleKeyBuilder> keys = new LinkedHashMap<String, BundleKeyBuilder>();

	private BundleSectionBuilder(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public BundleSectionBuilder key (BundleKeyBuilder key) {
		keys.put(key.getName(), key);
		return this;
	}

	@Override
	public BundleSection build() {
		return new BundleSection(
                name,
                ImmutableList.copyOf(getComments()),
                ImmutableList.copyOf(
                    Iterables.transform(keys.values(), BundleKeyBuilder.buildFn)
                ));
	}

    @Override
	public void merge(BundleSection section) {
        super.merge(section);
		// Keys
		mergeKeys(section.getKeys());
	}

    private void mergeKeys(ImmutableList<BundleKey> sourceKeys) {
        for (BundleKey sourceKey : sourceKeys) {
            String name = sourceKey.getName();
            BundleKeyBuilder key = keys.get(name);
            if (key != null) {
                key.merge(sourceKey);
            } else {
                BundleKeyBuilder keyBuilder = BundleKeyBuilder.create(name);
                keyBuilder.merge(sourceKey);
                key(keyBuilder);
            }
        }
    }

    @Override
    public String toString() {
        return "BundleSectionBuilder{" +
                "name='" + name + '\'' +
                ", keys=" + keys +
                '}';
    }
}
