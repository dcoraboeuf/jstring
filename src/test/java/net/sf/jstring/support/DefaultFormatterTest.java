package net.sf.jstring.support;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class DefaultFormatterTest {

	private final DefaultFormatter formatter = new DefaultFormatter();

	@Test
	public void format_2() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("0", "zero");
		map.put("1", "one");
		String value = formatter.format(Locale.ENGLISH, "Two parameters: {0} and {1}.", map);
		assertEquals("Two parameters: zero and one.", value);
	}

	@Test(expected = NullPointerException.class)
	public void getDefaultReplacement_null_locale() {
		formatter.getDefaultReplacement(null, "name");
	}

	@Test(expected = NullPointerException.class)
	public void getDefaultReplacement_null_name() {
		formatter.getDefaultReplacement(Locale.ENGLISH, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDefaultReplacement_blank_name() {
		formatter.getDefaultReplacement(Locale.ENGLISH, " ");
	}

	@Test
	public void getDefaultReplacement() {
		String value = formatter.getDefaultReplacement(Locale.ENGLISH, "name");
		assertEquals("##name##", value);
	}

}
