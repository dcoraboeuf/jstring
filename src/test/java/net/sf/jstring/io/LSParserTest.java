package net.sf.jstring.io;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import net.sf.jstring.builder.BundleBuilder;
import net.sf.jstring.builder.BundleKeyBuilder;
import net.sf.jstring.builder.BundleSectionBuilder;
import net.sf.jstring.builder.BundleValueBuilder;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleValue;

import org.junit.Test;

public class LSParserTest {

	@Test
	public void parse_ok() {
		// Parsing
		URL url = getClass().getResource("/test/sample.ls");
		Bundle actualBundle = new LSParser(true).parse(url);

		// Builds the expected bundle
		Bundle expectedBundle = BundleBuilder.create("sample")
				.comment("Sample localized strings for unit tests")
				.section(BundleSectionBuilder.create("general")
						.comment("General section")
						.key(BundleKeyBuilder.create("app.title")
								.comment("Application title")
								.value("en", BundleValue.value("My favourite application"))
								.value("fr", BundleValue.value("Mon application préférée"))
								.build())
						.build())
				.section(BundleSectionBuilder.create("home")
						.comment("Home page")
						.key(BundleKeyBuilder.create("home.title")
								.comment("Home page title")
								.value("en", BundleValue.value("Home page"))
								.value("fr", BundleValue.value("Page d'accueil"))
								.build())
						.key(BundleKeyBuilder.create("home.message")
								.comment("Message to display on the home page")
								.comment("This is a very long message")
								.value("en", BundleValueBuilder.create("This is a very long message that spans on several lines. Carriage returns are introduced using the normal \n Java escaping. Other ones are ignored. # The # character must be escaped at the beginning.").comment("Comments are supported in the middle of the text").build())
								.value("fr", BundleValueBuilder.create("Ceci est un très long message qui s'étend sur plusieurs lignes. Les retours chariot sont notifiés grâce au \n Java. Les autres sont ignorés. # Le caractère # doit être doublé au début d'une ligne.").build())
								.build())
						.key(BundleKeyBuilder.create("home.info")
								.comment("Placeholders are supported by name")
								.value("en", BundleValue.value(" Application version: {version}, build: {build}, {{not a parameter}}"))
								.build())
						.build())
				.build();

		// Compares the bundles
		assertEquals(expectedBundle, actualBundle);
	}

}
