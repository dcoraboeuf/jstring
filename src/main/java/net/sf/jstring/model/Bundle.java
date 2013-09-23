package net.sf.jstring.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import lombok.Data;

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

}
