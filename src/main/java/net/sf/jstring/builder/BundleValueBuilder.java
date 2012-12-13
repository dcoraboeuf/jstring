package net.sf.jstring.builder;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import net.sf.jstring.model.BundleValue;

import com.google.common.collect.ImmutableList;

public class BundleValueBuilder extends Builder<BundleValue> {

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
	private final List<String> comments = new ArrayList<String>();

	private BundleValueBuilder() {
	}

	public BundleValueBuilder comment(String comment) {
		comments.add(comment);
		return this;
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
		return new BundleValue(ImmutableList.copyOf(comments), value.toString());
	}

}
