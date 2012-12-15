package net.sf.jstring.support;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.sf.jstring.*;
import net.sf.jstring.Formatter;
import net.sf.jstring.builder.BundleCollectionBuilder;
import net.sf.jstring.index.DefaultIndexedBundleCollection;
import net.sf.jstring.index.IndexedBundleCollection;
import net.sf.jstring.index.IndexedBundleCollectionOwner;
import net.sf.jstring.io.DefaultParserFactory;
import net.sf.jstring.io.Parser;
import net.sf.jstring.io.ParserFactory;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleCollection;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class StringsLoader implements IndexedBundleCollectionOwner {
	
	private static final String CATALOGUE_PATH = "META-INF/strings/catalogue";

    public static StringsLoader basic() {
        return new StringsLoader();
    }

	public static Strings auto () {
		return new StringsLoader().load();
	}

	public static Strings auto (Locale... locales) {
		return new StringsLoader().withLocale(new DefaultSupportedLocales(locales)).load();
	}
	
	public static Strings empty () {
		return new StringsLoader().withPaths().load();
	}

	private final Logger logger = LoggerFactory.getLogger(StringsLoader.class);

	private final SupportedLocales supportedLocales;
	private final boolean autoDiscover;
	private final ImmutableList<String> paths;
	private final Fallback fallback;
	private final Formatter formatter;
    private final ParserFactory parserFactory;
    private final boolean traces;
	
	public StringsLoader() {
		this(new DefaultParserFactory(), new DefaultFormatter(), new SubstituteFallback(), new DefaultSupportedLocales(), true, ImmutableList.<String>of(), false);
	}
	
	public StringsLoader(ParserFactory parserFactory, Formatter formatter, Fallback fallback, SupportedLocales supportedLocales, boolean autoDiscover, ImmutableList<String> paths, boolean traces) {
        this.parserFactory = parserFactory;
		this.fallback = fallback;
		this.formatter = formatter;
		this.supportedLocales = supportedLocales;
		this.autoDiscover = autoDiscover;
		this.paths = paths;
		this.traces = traces;
	}

    public StringsLoader withParserFactory (ParserFactory parserFactory) {
        Validate.notNull(parserFactory, "Parser factory must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, supportedLocales, autoDiscover, paths, traces);
    }

    public StringsLoader withFormatter (Formatter formatter) {
        Validate.notNull(formatter, "Formatter must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, supportedLocales, autoDiscover, paths, traces);
    }
	
	public StringsLoader withFallback (Fallback fallback) {
		Validate.notNull(fallback, "Fallback must not be null");
		return new StringsLoader(parserFactory, formatter, fallback, supportedLocales, autoDiscover, paths, traces);
	}

    public StringsLoader withLocale (Locale locale) {
        Validate.notNull(locale, "Locale must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, supportedLocales.withLocale(locale), autoDiscover, paths, traces);
    }

    public StringsLoader withLocale (SupportedLocales locales) {
        Validate.notNull(locales, "Supported locales must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, locales, autoDiscover, paths, traces);
    }

    public StringsLoader withParsingPolicy(LocalePolicy policy) {
        Validate.notNull(policy, "Policy must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, supportedLocales.withLocaleParsingPolicy(policy), autoDiscover, paths, traces);
    }

    public StringsLoader withLookupPolicy(LocalePolicy policy) {
        Validate.notNull(policy, "Policy must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, supportedLocales.withLocaleLookupPolicy(policy), autoDiscover, paths, traces);
    }
	
	public StringsLoader withPaths(String... paths) {
		return new StringsLoader(parserFactory, formatter, fallback, supportedLocales, false, ImmutableList.copyOf(paths), traces);
	}
	
	public StringsLoader withTraces() {
		return new StringsLoader(parserFactory, formatter, fallback, supportedLocales, false, ImmutableList.copyOf(paths), true);
	}
	
	@Override
	public boolean reload(IndexedBundleCollection indexedBundleCollection) {
		// Loads the collection of bundles
		BundleCollection bundleCollection = loadBundleCollection();
		// Reload
		indexedBundleCollection.index(bundleCollection);
		// OK
		return true;
	}

	public Strings load() {
		
		// Default locale?
        Locale defaultLocale = supportedLocales.getDefaultLocale();
        logger.info("[strings] Setting default locale to {}", defaultLocale);
        logger.info("[strings] Supported locales are {}", supportedLocales.getSupportedLocales());
		Locale.setDefault(defaultLocale);
		
		// Loads the collection of bundles
		BundleCollection collection = loadBundleCollection();
		
		// Indexation of the collection
		IndexedBundleCollection indexedCollection = new DefaultIndexedBundleCollection(supportedLocales, this);
		indexedCollection.index(collection);

		// Returns some strings
		return new DefaultStrings(indexedCollection, fallback, formatter);
	}

	protected BundleCollection loadBundleCollection() {
		// Adds all properties in META-INF/strings/catalogue
		Collection<URL> paths;
		if (autoDiscover) {
			try {
				paths = discover();
			} catch (IOException ex) {
				throw new CannotDiscoverStringsException (ex);
			}
		} else {
			paths = Lists.transform(this.paths, new Function<String, URL>() {
				@Override
				public URL apply (String path) {
					URL url = StringsLoader.this.getClass().getClassLoader().getResource(path);
					if (url == null) {
						throw new CannotFindResourceException (path);
					} else {
						return url;
					}
				}
			});
		}
		
		// Bundle collection
		BundleCollectionBuilder collectionBuilder = BundleCollectionBuilder.create();
		
		// Parses all URL
		for (URL path: paths) {
			logger.info("[strings] Parsing path {}", path);
            // Gets the parser
            Parser<?> parser = parserFactory.getParser(path);
            // Traces
            if (traces) {
            	parser = parser.withTraces();
            }
            // Parsing
			Bundle bundle = parser.parse(supportedLocales, path);
			collectionBuilder.bundle(bundle);
		}
		
		// Gets the final bundle collection
		BundleCollection collection = collectionBuilder.build();
		return collection;
	}

	protected Set<URL> discover() throws IOException, MalformedURLException {
		logger.info("[strings] Scanning for all '{}'...", CATALOGUE_PATH);
		Set<URL> paths = new HashSet<URL>();
		Enumeration<URL> resources = getClass().getClassLoader().getResources(CATALOGUE_PATH);
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			logger.info("[strings] Trying to load URL {}", url);
			// JDK7 Reads the content
			InputStream in = url.openStream();
			try {
				List<String> lines = IOUtils.readLines(in);
				for (String line : lines) {
					if (StringUtils.isNotBlank(line)) {
						line = StringUtils.trim(line);
						URL path = new URL(url, line);
						if (paths.contains(path)) {
							logger.error("[strings] {} path has already been added", path);
							throw new IllegalStateException("Duplicated strings bundle");
						} else {
							logger.info("[strings] Adding path {}", path);
							paths.add(path);
						}
					}
				}
			} finally {
				in.close();
			}
		}
		return paths;
	}

}
