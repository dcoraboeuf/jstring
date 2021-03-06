package net.sf.jstring.io;

import net.sf.jstring.builder.BundleBuilder;
import net.sf.jstring.builder.BundleKeyBuilder;
import net.sf.jstring.builder.BundleSectionBuilder;
import net.sf.jstring.builder.BundleValueBuilder;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.support.DefaultSupportedLocales;
import org.junit.Test;

import java.net.URL;
import java.util.Locale;

import static net.sf.jstring.builder.BundleValueBuilder.text;
import static org.junit.Assert.assertEquals;

public abstract class AbstractParserTest {

    protected abstract String getTestFileResourcePath();

    protected abstract Parser<?> createParser();

	@Test
	public void parse_ok() {
		// Parsing
		URL url = getClass().getResource(getTestFileResourcePath());
		Bundle actualBundle = createParser().parse(new DefaultSupportedLocales().withLocale(Locale.FRENCH), url);

		// Builds the expected bundle
		Bundle expectedBundle = BundleBuilder.create("sample")
				.comment("Sample localized strings for unit tests")
				.section(BundleSectionBuilder.create("default")
						.key(BundleKeyBuilder.create("list.separator.prefix")
                                .value(Locale.ENGLISH, text("- "))
                                .value(Locale.FRENCH, text("- ")))
						.key(BundleKeyBuilder.create("list.separator.suffix")
                                .value(Locale.ENGLISH, text("\n"))
                                .value(Locale.FRENCH, text("\n")))
						)
				.section(BundleSectionBuilder.create("general")
						.comment("General section")
						.key(BundleKeyBuilder.create("app.title")
                                .comment("Application title")
                                .value(Locale.ENGLISH, text("My favourite application"))
                                .value(Locale.FRENCH, text("Mon application préférée")))
						)
				.section(BundleSectionBuilder.create("home")
						.comment("Home page")
						.key(BundleKeyBuilder.create("home.title")
								.comment("Home page title")
								.value(Locale.ENGLISH, text("Home page"))
								.value(Locale.FRENCH, text("Page d'accueil")))
						.key(BundleKeyBuilder.create("home.message")
								.comment("Message to display on the home page")
								.comment("This is a very long message")
								.value(Locale.ENGLISH, BundleValueBuilder.create()
                                        .value("This is a very long message that spans on several lines.")
                                        .value("Carriage returns are introduced using the normal \n Java escaping.")
                                        .value("Other ones are ignored. # The # character must be escaped at the beginning.")
                                        .comment("Comments are supported in the middle of the text"))
								.value(Locale.FRENCH, BundleValueBuilder.create()
                                        .value("Ceci est un très long message qui s'étend sur plusieurs lignes.")
                                        .value("Les retours chariot sont notifiés grâce au \n Java.")
                                        .value("Les autres sont ignorés. # Le caractère # doit être doublé au début d'une ligne."))
								)
						.key(BundleKeyBuilder.create("home.info")
                                .comment("Placeholders are supported by name")
                                .value(Locale.ENGLISH, text("Application version: {version}, build: {build}, {{not a parameter}}"))
                                .value(Locale.FRENCH, text("Version de l'application: {version}, build: {build}, {{pas un paramètre}}"))
                        )
						)
				.build();

		// Compares the bundles (at string level)
		assertEquals(expectedBundle.toString(), actualBundle.toString());
	}

}
