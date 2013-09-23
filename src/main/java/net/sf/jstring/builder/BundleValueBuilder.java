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
    private StringBuilder value = new StringBuilder();

    private BundleValueBuilder() {
    }

    public static BundleValueBuilder create() {
        return new BundleValueBuilder();
    }

    public static BundleValueBuilder text(String text) {
        return create().value(text);
    }

    public BundleValueBuilder value(String text) {
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
    public void merge(BundleValue source, BundleValueMergeMode mode) {
        super.merge(source, mode);
        String sourceValue = source.getValue();
        String targetValue = value.toString();
        if (sourceValue != null) {
            if (StringUtils.isNotBlank(targetValue)) {
                switch (mode) {
                    case APPEND:
                        value.append(sourceValue);
                        break;
                    case REPLACE:
                        value = new StringBuilder(sourceValue);
                        break;
                    case IGNORE:
                        break;
                    case ERROR:
                    default:
                        throw new BundleValueCannotMergeException();
                }
            } else {
                value.append(sourceValue);
            }
        }
    }

    @Override
    public String toString() {
        return "BundleValueBuilder{" +
                "value=" + value +
                '}';
    }
}
