package net.sf.jstring.builder;

import net.sf.jstring.model.Bundle;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BundleBuilder}.
 */
public class BundleBuilderTest {

    @Test
    public void merge_append() {
        Bundle bundle1 = BundleBuilder
                .create("common")
                .section(
                        BundleSectionBuilder.create("default")
                                .key(
                                        BundleKeyBuilder
                                                .create("one")
                                                .addValue(Locale.ENGLISH, "One")
                                )
                )
                .build();
        Bundle bundle2 = BundleBuilder
                .create("common")
                .section(BundleSectionBuilder.create("default")
                        .key(
                                BundleKeyBuilder
                                        .create("one")
                                        .addValue(Locale.ENGLISH, "First")
                        )
                )
                .build();
        // Merge
        BundleBuilder builder = BundleBuilder.create("common");
        builder.merge(bundle1, BundleValueMergeMode.APPEND);
        builder.merge(bundle2, BundleValueMergeMode.APPEND);
        // Check
        assertEquals(
                BundleBuilder
                        .create("common")
                        .section(BundleSectionBuilder.create("default")
                                .key(
                                        BundleKeyBuilder
                                                .create("one")
                                                .addValue(Locale.ENGLISH, "OneFirst")
                                )
                        )
                        .build(),
                builder.build()
        );
    }

    @Test
    public void merge_replace() {
        Bundle bundle1 = BundleBuilder
                .create("common")
                .section(
                        BundleSectionBuilder.create("default")
                                .key(
                                        BundleKeyBuilder
                                                .create("one")
                                                .addValue(Locale.ENGLISH, "One")
                                )
                )
                .build();
        Bundle bundle2 = BundleBuilder
                .create("common")
                .section(BundleSectionBuilder.create("default")
                        .key(
                                BundleKeyBuilder
                                        .create("one")
                                        .addValue(Locale.ENGLISH, "First")
                        )
                )
                .build();
        // Merge
        BundleBuilder builder = BundleBuilder.create("common");
        builder.merge(bundle1, BundleValueMergeMode.REPLACE);
        builder.merge(bundle2, BundleValueMergeMode.REPLACE);
        // Check
        assertEquals(
                BundleBuilder
                        .create("common")
                        .section(BundleSectionBuilder.create("default")
                                .key(
                                        BundleKeyBuilder
                                                .create("one")
                                                .addValue(Locale.ENGLISH, "First")
                                )
                        )
                        .build(),
                builder.build()
        );
    }

    @Test
    public void merge_ignore() {
        Bundle bundle1 = BundleBuilder
                .create("common")
                .section(
                        BundleSectionBuilder.create("default")
                                .key(
                                        BundleKeyBuilder
                                                .create("one")
                                                .addValue(Locale.ENGLISH, "One")
                                )
                )
                .build();
        Bundle bundle2 = BundleBuilder
                .create("common")
                .section(BundleSectionBuilder.create("default")
                        .key(
                                BundleKeyBuilder
                                        .create("one")
                                        .addValue(Locale.ENGLISH, "First")
                        )
                )
                .build();
        // Merge
        BundleBuilder builder = BundleBuilder.create("common");
        builder.merge(bundle1, BundleValueMergeMode.IGNORE);
        builder.merge(bundle2, BundleValueMergeMode.IGNORE);
        // Check
        assertEquals(
                BundleBuilder
                        .create("common")
                        .section(BundleSectionBuilder.create("default")
                                .key(
                                        BundleKeyBuilder
                                                .create("one")
                                                .addValue(Locale.ENGLISH, "One")
                                )
                        )
                        .build(),
                builder.build()
        );
    }

    @Test(expected = BundleValueCannotMergeException.class)
    public void merge_error() {
        Bundle bundle1 = BundleBuilder
                .create("common")
                .section(
                        BundleSectionBuilder.create("default")
                                .key(
                                        BundleKeyBuilder
                                                .create("one")
                                                .addValue(Locale.ENGLISH, "One")
                                )
                )
                .build();
        Bundle bundle2 = BundleBuilder
                .create("common")
                .section(BundleSectionBuilder.create("default")
                        .key(
                                BundleKeyBuilder
                                        .create("one")
                                        .addValue(Locale.ENGLISH, "First")
                        )
                )
                .build();
        // Merge
        BundleBuilder builder = BundleBuilder.create("common");
        builder.merge(bundle1, BundleValueMergeMode.ERROR);
        builder.merge(bundle2, BundleValueMergeMode.ERROR);
    }

}
