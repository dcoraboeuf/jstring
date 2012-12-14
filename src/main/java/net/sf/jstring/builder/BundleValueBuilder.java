package net.sf.jstring.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import net.sf.jstring.model.BundleValue;
import org.apache.commons.lang3.StringUtils;

public class BundleValueBuilder extends AbstractBuilderCommented<BundleValue, BundleValueBuilder> {

    public static final Function<? super BundleValueBuilder, BundleValue> buildFn = new Function<BundleValueBuilder, BundleValue>() {
        @Override
        public BundleValue apply(BundleValueBuilder bundleValueBuilder) {
            return bundleValueBuilder.build();
        }
    };

    public static BundleValueBuilder create() {
		return new BundleValueBuilder();
	}

    public static BundleValueBuilder text (String text) {
        return create().value(text);
    }

	private final StringBuilder value = new StringBuilder();

	private BundleValueBuilder() {
	}
	
	public BundleValueBuilder value (String text) {
		if (value.length() > 0) {
			value.append(" ");
		}
		value.append(text);
		return this;
	}

	@Override
	public BundleValue build() {
		return new BundleValue(
                ImmutableList.copyOf(getComments()),
                value.toString());
	}

    @Override
    public void merge(BundleValue source) {
        super.merge(source);
        String sourceValue = source.getValue();
        if (StringUtils.isNotBlank(sourceValue)) {
            value.append(sourceValue);
        }
    }
}
