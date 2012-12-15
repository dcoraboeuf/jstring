package net.sf.jstring.index;

import static org.mockito.Mockito.mock;
import net.sf.jstring.SupportedLocales;

import org.junit.Test;

public class DefaultIndexedBundleCollectionTest {
	
	@Test(expected = IllegalStateException.class)
	public void no_collection_after_construction() {
		SupportedLocales supportedLocales = mock(SupportedLocales.class);
		new DefaultIndexedBundleCollection(supportedLocales, null).getBundleCollection();
	}

}
