package net.sf.jstring.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleValue;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class BundleKeyBuilder extends AbstractBuilderCommented<BundleKey, BundleKeyBuilder> {

    public static final Function<? super BundleKeyBuilder, BundleKey> buildFn = new Function<BundleKeyBuilder, BundleKey>() {
        @Override
        public BundleKey apply(BundleKeyBuilder bundleKeyBuilder) {
            return bundleKeyBuilder.build();
        }
    };

    public static BundleKeyBuilder create(String name) {
		return new BundleKeyBuilder(name);
	}

	private final String name;
	private final Map<Locale, BundleValueBuilder> values = new LinkedHashMap<Locale, BundleValueBuilder>();

	private BundleKeyBuilder(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public BundleKeyBuilder value(Locale language, BundleValueBuilder value) {
		values.put(language, value);
		return this;
	}

	@Override
	public BundleKey build() {
		return new BundleKey(
                name,
                ImmutableList.copyOf(getComments()),
                ImmutableMap.copyOf(
                    Maps.transformValues(
                        values,
                        BundleValueBuilder.buildFn
                )
        ));
	}

    @Override
    public void merge(BundleKey source) {
        super.merge(source);
        ImmutableMap<Locale,BundleValue> sourceValues = source.getValues();
        for (Map.Entry<Locale,BundleValue> sourceEntry: sourceValues.entrySet()) {
            Locale locale = sourceEntry.getKey();
            BundleValue sourceBundleValue = sourceEntry.getValue();
            BundleValueBuilder value = values.get(locale);
            if (value != null) {
                value.merge(sourceBundleValue);
            } else {
                BundleValueBuilder valueBuilder = BundleValueBuilder.create();
                valueBuilder.merge(sourceBundleValue);
                value(locale, valueBuilder);
            }
        }
    }
}
