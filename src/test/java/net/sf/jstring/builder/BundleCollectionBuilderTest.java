package net.sf.jstring.builder;

import net.sf.jstring.model.Bundle;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class BundleCollectionBuilderTest {

    @Test
    public void merge() {
        BundleCollectionBuilder collectionBuilder =
                BundleCollectionBuilder.create().bundle(
                        BundleBuilder
                                .create("common")
                                .section(
                                        BundleSectionBuilder.create(Bundle.DEFAULT_SECTION)
                                                .key(
                                                        BundleKeyBuilder.create("one")
                                                                .addValue(Locale.ENGLISH, "One")
                                                )
                                )
                                .build()
                );
        collectionBuilder.merge(
                BundleCollectionBuilder.create()
                        .bundle(
                                BundleBuilder
                                        .create("common")
                                        .section(
                                                BundleSectionBuilder.create(Bundle.DEFAULT_SECTION)
                                                        .key(
                                                                BundleKeyBuilder.create("one")
                                                                        .addValue(Locale.ENGLISH, "First")
                                                        )
                                        )
                                        .build()
                        )
                        .build(),
                BundleValueMergeMode.REPLACE
        );
        assertEquals(
                BundleCollectionBuilder.create()
                        .bundle(
                                BundleBuilder
                                        .create("common")
                                        .section(
                                                BundleSectionBuilder.create(Bundle.DEFAULT_SECTION)
                                                        .key(
                                                                BundleKeyBuilder.create("one")
                                                                        .addValue(Locale.ENGLISH, "First")
                                                        )
                                        )
                                        .build()
                        )
                        .build(),
                collectionBuilder.build()
        );
    }

}
