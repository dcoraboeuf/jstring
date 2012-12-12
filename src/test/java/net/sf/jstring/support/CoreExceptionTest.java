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
		Strings strings = StringsLoader.auto();
		MyCoreException ex = new MyCoreException(10);
		assertEquals ("Test exception n°10", ex.getLocalizedMessage(strings, Locale.ENGLISH));
		assertEquals ("Exception de test n°10", ex.getLocalizedMessage(strings, Locale.FRENCH));
	}
	
	@Test
	public void standard_exception() {
		Strings strings = StringsLoader.auto();
		CannotFindResourceException ex = new CannotFindResourceException("/test");
		assertEquals ("[jstring-01] Cannot find resource at /test", ex.getLocalizedMessage(strings, Locale.ENGLISH));
	}

}