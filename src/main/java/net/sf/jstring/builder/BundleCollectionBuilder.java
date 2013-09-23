package net.sf.jstring.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BundleCollectionBuilder extends Builder<BundleCollection> {

    private final List<Bundle> bundles = new ArrayList<Bundle>();

    private BundleCollectionBuilder() {
    }

    public static BundleCollectionBuilder create() {
        return new BundleCollectionBuilder();
    }

    public BundleCollectionBuilder bundle(Bundle bundle) {
        bundles.add(bundle);
        return this;
    }

    @Override
    public BundleCollection build() {
        return new BundleCollection(ImmutableList.copyOf(bundles));
    }

    @Override
    public void merge(BundleCollection source, BundleValueMergeMode mode) {
        // Indexes the source bundles
        ImmutableMap<String, Bundle> sourceBundleIndex = Maps.uniqueIndex(source.getBundles(), Bundle.bundleNameFn);
        // Indexes this collection's bundles
        ImmutableMap<String, Bundle> thisBundleIndex = Maps.uniqueIndex(bundles, Bundle.bundleNameFn);
        // Builder for the result
        BundleCollectionBuilder resultBuilder = BundleCollectionBuilder.create();
        // Set of all bundle keys
        Set<String> bundleNames = new HashSet<String>(sourceBundleIndex.keySet());
        bundleNames.addAll(thisBundleIndex.keySet());
        // For each bundle name
        for (String bundleName : bundleNames) {
            // Source & target bundles
            Bundle sourceBundle = sourceBundleIndex.get(bundleName);
            Bundle targetBundle = thisBundleIndex.get(bundleName);
            // Only source bundle
            if (targetBundle == null) {
                // Adds to the result
                resultBuilder.bundle(sourceBundle);
            }
            // Only target bundle
            else if (sourceBundle == null) {
                // Adds to the result
                resultBuilder.bundle(targetBundle);
            }
            // Both exists
            else {
                // Bundles need to be merged
                BundleBuilder bundleBuilder = BundleBuilder.create(bundleName);
                bundleBuilder.merge(targetBundle, mode);
                bundleBuilder.merge(sourceBundle, mode);
                resultBuilder.bundle(bundleBuilder.build());
            }
        }
        // Clear and resets
        bundles.clear();
        bundles.addAll(resultBuilder.build().getBundles());
    }
}
