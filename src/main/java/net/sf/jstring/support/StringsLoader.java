package net.sf.jstring.support;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sf.jstring.Fallback;
import net.sf.jstring.Formatter;
import net.sf.jstring.Strings;
import net.sf.jstring.builder.BundleCollectionBuilder;
import net.sf.jstring.index.DefaultIndexedBundleCollection;
import net.sf.jstring.index.IndexedBundleCollection;
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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class StringsLoader {
	
	private static final String CATALOGUE_PATH = "META-INF/strings/catalogue";

	public static Strings auto () {
		return new StringsLoader().load();
	}
	
	public static Strings empty () {
		return new StringsLoader().withPaths().load();
	}

	private final Logger logger = LoggerFactory.getLogger(StringsLoader.class);

	private final Locale defaultLocale;
	private final boolean autoDiscover;
	private final ImmutableList<String> paths;
	private final Fallback fallback;
	private final Formatter formatter;
    private final ParserFactory parserFactory;
    private final boolean traces;
	
	public StringsLoader() {
		this(new DefaultParserFactory(), new DefaultFormatter(), new SubstituteFallback(), Locale.ENGLISH, true, ImmutableList.<String>of(), false);
	}
	
	public StringsLoader(ParserFactory parserFactory, Formatter formatter, Fallback fallback, Locale defaultLocale, boolean autoDiscover, ImmutableList<String> paths, boolean traces) {
        this.parserFactory = parserFactory;
		this.fallback = fallback;
		this.formatter = formatter;
		this.defaultLocale = defaultLocale;
		this.autoDiscover = autoDiscover;
		this.paths = paths;
		this.traces = traces;
	}

    public StringsLoader withParserFactory (ParserFactory parserFactory) {
        Validate.notNull(parserFactory, "Parser factory must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, defaultLocale, autoDiscover, paths, traces);
    }

    public StringsLoader withFormatter (Formatter formatter) {
        Validate.notNull(formatter, "Formatter must not be null");
        return new StringsLoader(parserFactory, formatter, fallback, defaultLocale, autoDiscover, paths, traces);
    }
	
	public StringsLoader withFallback (Fallback fallback) {
		Validate.notNull(fallback, "Fallback must not be null");
		return new StringsLoader(parserFactory, formatter, fallback, defaultLocale, autoDiscover, paths, traces);
	}
	
	public StringsLoader withLocale (Locale locale) {
		Validate.notNull(locale, "Locale must not be null");
		return new StringsLoader(parserFactory, formatter, fallback, locale, autoDiscover, paths, traces);
	}
	
	public StringsLoader withPaths(String... paths) {
		return new StringsLoader(parserFactory, formatter, fallback, defaultLocale, false, ImmutableList.copyOf(paths), traces);
	}
	
	public StringsLoader withTraces() {
		return new StringsLoader(parserFactory, formatter, fallback, defaultLocale, false, ImmutableList.copyOf(paths), true);
	}

	public Strings load() {
		
		// Default locale?
		logger.info("[strings] Setting default locale to {}", defaultLocale);
		Locale.setDefault(defaultLocale);
		
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
			Bundle bundle = parser.parse(path);
			collectionBuilder.bundle(bundle);
		}
		
		// Gets the final bundle collection
		BundleCollection collection = collectionBuilder.build();
		
		// Indexation of the collection
		IndexedBundleCollection indexedCollection = new DefaultIndexedBundleCollection(defaultLocale);
		indexedCollection.index(collection);

		// Returns some strings
		return new DefaultStrings(indexedCollection, fallback, formatter);
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
