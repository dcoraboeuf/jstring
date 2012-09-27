package net.sf.jstring;

import net.sf.jstring.impl.DefaultStrings;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for the {@link }MultiLocalizable} class.
 */
public class MultiLocalizableTest {

    private Strings strings;

    @Before
    public void before() {
        Locale.setDefault(Locale.ENGLISH);
        strings = new DefaultStrings("test.SampleStrings");
    }

    @Test
    public void empty() {
        MultiLocalizable m = new MultiLocalizable(Collections.<Localizable>emptyList());
        String text = m.getLocalizedMessage(strings, Locale.UK);
        assertEquals("", text);
    }

    @Test
    public void one() {
        MultiLocalizable m = new MultiLocalizable(Arrays.asList(new LocalizableMessage("sample.one", "Test")));
        String text = m.getLocalizedMessage(strings, Locale.UK);
        assertEquals(" - One parameter: Test\n", text);
    }

    @Test
    public void two() {
        MultiLocalizable m = new MultiLocalizable(Arrays.asList(
                new LocalizableMessage("sample.one", "Test"),
                new LocalizableMessage("sample.one", "Test2")));
        String text = m.getLocalizedMessage(strings, Locale.UK);
        assertEquals(" - One parameter: Test\n - One parameter: Test2\n", text);
    }

    @Test
    public void two_with_custom_prefixes() {
        MultiLocalizable m = new MultiLocalizable(Arrays.asList(
                new LocalizableMessage("sample.one", "Test"),
                new LocalizableMessage("sample.one", "Test2")),
                "<li>", "</li>");
        String text = m.getLocalizedMessage(strings, Locale.UK);
        assertEquals("<li>One parameter: Test</li><li>One parameter: Test2</li>", text);
    }

    @Test
    public void two_with_custom_localizable_prefixes() {
        MultiLocalizable m = new MultiLocalizable(Arrays.asList(
                new LocalizableMessage("sample.one", "Test"),
                new LocalizableMessage("sample.one", "Test2")),
                new LocalizableMessage("sample.prefix"), new LocalizableMessage("sample.suffix"));
        String text = m.getLocalizedMessage(strings, Locale.UK);
        assertEquals("Start: One parameter: Test\nStart: One parameter: Test2\n", text);
    }

}
