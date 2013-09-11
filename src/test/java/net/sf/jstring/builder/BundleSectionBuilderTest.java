package net.sf.jstring.builder;

import net.sf.jstring.model.BundleKey;
import net.sf.jstring.model.BundleSection;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class BundleSectionBuilderTest {

    @Test
    public void inline() {
        BundleSection section = BundleSectionBuilder.create("x")
                .key(
                        BundleKeyBuilder.create("one")
                                .addValue(Locale.ENGLISH, "One")
                                .addValue(Locale.FRENCH, "Un")
                )
                .key(
                        BundleKeyBuilder.create("two")
                                .addValue(Locale.ENGLISH, "Two")
                                .addValue(Locale.FRENCH, "Deux")
                )
                .build();
        assertSection(section);
    }

    @Test
    public void async() {
        BundleSectionBuilder section = BundleSectionBuilder.create("x");
        section.key("one").addValue(Locale.ENGLISH, "One");
        section.key("two").addValue(Locale.ENGLISH, "Two");
        section.key("one").addValue(Locale.FRENCH, "Un");
        section.key("two").addValue(Locale.FRENCH, "Deux");
        assertSection(section.build());
    }

    private void assertSection(BundleSection section) {
        assertEquals("x", section.getName());
        List<BundleKey> keys = section.getKeys();
        assertEquals(2, keys.size());
        assertEquals("one", keys.get(0).getName());
        assertEquals("One", keys.get(0).getValues().get(Locale.ENGLISH).getValue());
        assertEquals("Un", keys.get(0).getValues().get(Locale.FRENCH).getValue());
        assertEquals("two", keys.get(1).getName());
        assertEquals("Two", keys.get(1).getValues().get(Locale.ENGLISH).getValue());
        assertEquals("Deux", keys.get(1).getValues().get(Locale.FRENCH).getValue());
    }

}
