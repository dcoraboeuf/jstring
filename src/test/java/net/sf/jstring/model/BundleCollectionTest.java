package net.sf.jstring.model;

import com.google.common.base.Predicate;
import net.sf.jstring.builder.BundleBuilder;
import net.sf.jstring.builder.BundleCollectionBuilder;
import net.sf.jstring.builder.BundleKeyBuilder;
import net.sf.jstring.builder.BundleSectionBuilder;
import net.sf.jstring.support.KeyIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class BundleCollectionTest {

    @Test
    public void filter() {
        BundleCollection collection = BundleCollectionBuilder
                .create()
                .bundle(
                        BundleBuilder
                                .create("common")
                                .comment("Bundle comment")
                                .section(
                                        BundleSectionBuilder
                                                .create(Bundle.DEFAULT_SECTION)
                                                .comment("Section comment")
                                                .key(
                                                        BundleKeyBuilder
                                                                .create("added.default-only")
                                                                .addValue(Locale.ENGLISH, "Added default only")
                                                )
                                                .key(
                                                        BundleKeyBuilder
                                                                .create("updated.default-only")
                                                                .addValue(Locale.ENGLISH, "Updated default only")
                                                                .addValue(Locale.FRENCH, "Valeur initiale")
                                                )
                                                .key(
                                                        BundleKeyBuilder
                                                                .create("deleted.default-only")
                                                                .addValue(Locale.FRENCH, "Valeur initiale")
                                                )
                                                .key(
                                                        BundleKeyBuilder
                                                                .create("added.both")
                                                                .addValue(Locale.ENGLISH, "Added both")
                                                                .addValue(Locale.FRENCH, "Ajout des deux")
                                                )
                                                .key(
                                                        BundleKeyBuilder
                                                                .create("updated.both")
                                                                .addValue(Locale.ENGLISH, "Updated both")
                                                                .addValue(Locale.FRENCH, "Modification des deux")
                                                )
                                )
                                .build()
                )
                .build();
        collection = collection.filter(new Predicate<KeyIdentifier>() {
            @Override
            public boolean apply(KeyIdentifier i) {
                return !StringUtils.startsWith(i.getKey(), "deleted");
            }
        });
        assertEquals(
                BundleCollectionBuilder
                        .create()
                        .bundle(
                                BundleBuilder
                                        .create("common")
                                        .comment("Bundle comment")
                                        .section(
                                                BundleSectionBuilder
                                                        .create(Bundle.DEFAULT_SECTION)
                                                        .comment("Section comment")
                                                        .key(
                                                                BundleKeyBuilder
                                                                        .create("added.default-only")
                                                                        .addValue(Locale.ENGLISH, "Added default only")
                                                        )
                                                        .key(
                                                                BundleKeyBuilder
                                                                        .create("updated.default-only")
                                                                        .addValue(Locale.ENGLISH, "Updated default only")
                                                                        .addValue(Locale.FRENCH, "Valeur initiale")
                                                        )
                                                        .key(
                                                                BundleKeyBuilder
                                                                        .create("added.both")
                                                                        .addValue(Locale.ENGLISH, "Added both")
                                                                        .addValue(Locale.FRENCH, "Ajout des deux")
                                                        )
                                                        .key(
                                                                BundleKeyBuilder
                                                                        .create("updated.both")
                                                                        .addValue(Locale.ENGLISH, "Updated both")
                                                                        .addValue(Locale.FRENCH, "Modification des deux")
                                                        )
                                        )
                                        .build()
                        )
                        .build()
                , collection
        );
    }

}
