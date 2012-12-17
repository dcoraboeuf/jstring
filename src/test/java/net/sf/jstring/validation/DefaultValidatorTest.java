package net.sf.jstring.validation;

import net.sf.jstring.Strings;
import net.sf.jstring.ValidationResult;
import net.sf.jstring.support.StringsLoader;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

public class DefaultValidatorTest {

    @Test
    public void sample() {
        Strings strings = new StringsLoader().withPaths("test/ls/sample.ls").withLocale(Locale.FRENCH).load();
        ValidationResult result = new DefaultValidator().validate(strings);
        assertNotNull (result);
    }

}
