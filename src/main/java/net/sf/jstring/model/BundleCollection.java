package net.sf.jstring.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Data;
import net.sf.jstring.support.KeyIdentifier;

@Data
public class BundleCollection {

    private final ImmutableList<Bundle> bundles;

    public BundleCollection filter(final Predicate<KeyIdentifier> predicate) {
        return new BundleCollection(
                ImmutableList.copyOf(
                        Lists.transform(
                                bundles,
                                new Function<Bundle, Bundle>() {
                                    @Override
                                    public Bundle apply(Bundle bundle) {
                                        return bundle.filter(predicate);
                                    }
                                }
                        )
                )
        );
    }

}
