package net.sf.jstring;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeysTest {

    private Strings strings;

    @Before
    public void before() {
        Locale.setDefault(Locale.ENGLISH);
        strings = new Strings(true, "test.Keys");
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
