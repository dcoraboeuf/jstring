package net.sf.jstring.support;

import net.sf.jstring.Strings;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class JDKFormatterTest {

    @Test
    public void format() {
        String value = new JDKFormatter().format(Locale.FRENCH, "Montant de {0,number} euros", Collections.singletonMap("0", new BigDecimal("1002.35")));
        assertEquals ("Montant de 1\u00A0002,35 euros", value);
    }

    @Test
    public void integration() {
        Strings strings = StringsLoader.basic().withLocale(Locale.FRENCH).withFormatter(new JDKFormatter()).load();
        String value = strings.get(Locale.FRENCH, "parameterized.0", "Test");
        assertEquals ("Avec paramètre \"Test\"", value);
    }

}
