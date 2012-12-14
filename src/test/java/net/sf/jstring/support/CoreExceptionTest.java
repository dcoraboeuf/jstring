package net.sf.jstring.support;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import net.sf.jstring.Strings;

import org.junit.Test;

public class CoreExceptionTest {
	
	@Test
	public void code() {
		MyCoreException ex = new MyCoreException(10);
		assertEquals (MyCoreException.class.getName(), ex.getCode());
	}
	
	@Test
	public void localization() {
		Strings strings = StringsLoader.basic().withLocale(Locale.FRENCH).load();
		MyCoreException ex = new MyCoreException(10);
		assertEquals ("Test exception n°10", ex.getLocalizedMessage(strings, Locale.ENGLISH));
		assertEquals ("Exception de test n°10", ex.getLocalizedMessage(strings, Locale.FRENCH));
	}

}
