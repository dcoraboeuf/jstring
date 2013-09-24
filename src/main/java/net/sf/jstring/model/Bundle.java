package net.sf.jstring.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Data;
import net.sf.jstring.support.KeyIdentifier;

@Data
public class Bundle implements Commented {

    public static final String DEFAULT_SECTION = "default";
    public static final Function<Bundle, String> bundleNameFn = new Function<Bundle, String>() {
        @Override
        public String apply(Bundle bundle) {
            return bundle.getName();
        }
    };
    private final String name;
    private final ImmutableList<String> comments;
    private final ImmutableList<BundleSection> sections;

    public Bundle filter(final Predicate<KeyIdentifier> predicate) {
        return new Bundle(
                name,
                comments,
                ImmutableList.copyOf(
                        Lists.transform(
                                sections,
                                new Function<BundleSection, BundleSection>() {
                                    @Override
                                    public BundleSection apply(BundleSection section) {
                                        return section.filter(name, predicate);
                                    }
                                }
                        )
                )
        );
    }

}