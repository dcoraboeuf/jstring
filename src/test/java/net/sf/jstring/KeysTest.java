package net.sf.jstring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import net.sf.jstring.support.DefaultStrings;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class KeysTest {

    private Strings strings;

    @Before
    public void before() {
        Locale.setDefault(Locale.ENGLISH);
        strings = new DefaultStrings(true, "test.Keys");
    }

    @Test
    public void enumToString() {
        for (Keys key : Keys.values()) {
            String ts = key.toString();
            assertEquals("jstring.test.keys." + key.name(), ts);
        }
    }

    @Test
    public void get() {
        for (Keys key : Keys.values()) {
            String value = strings.get(Locale.ENGLISH, key, "Test file");
            assertTrue(StringUtils.isNotBlank(value));
            assertTrue(!StringUtils.contains(value, key.name()));
        }
    }

}
