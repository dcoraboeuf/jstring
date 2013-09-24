package net.sf.jstring.model;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import lombok.Data;
import net.sf.jstring.support.KeyIdentifier;

@Data
public class BundleSection implements Commented {

    private final String name;
    private final ImmutableList<String> comments;
    private final ImmutableList<BundleKey> keys;

    public BundleSection filter(final String bundle, final Predicate<KeyIdentifier> predicate) {
        return new BundleSection(
                name,
                comments,
                ImmutableList.copyOf(
                        Iterables.filter(
                                keys,
                                new Predicate<BundleKey>() {
                                    @Override
                                    public boolean apply(BundleKey key) {
                                        return predicate.apply(
                                                new KeyIdentifier(
                                                        bundle,
                                                        name,
                                                        key.getName()
                                                )
                                        );
                                    }
                                }
                        )
                )
        );
    }
}
