package net.sf.jstring.io;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.apache.commons.lang3.StringUtils.trim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jstring.builder.BundleBuilder;
import net.sf.jstring.builder.BundleKeyBuilder;
import net.sf.jstring.builder.BundleSectionBuilder;
import net.sf.jstring.model.Bundle;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LSParser {

	private static final String ENCODING = "UTF-8";
	
	private static final String COMMENT_PREFIX = "#";
	private static final Pattern SECTION_PATTERN = Pattern.compile("^\\[(.*)\\]$");
	private static final Pattern LANGUAGE_VALUE_PATTERN = Pattern.compile("^(.+)\\->(.*)$");

	private static interface ParsingConsumer {

		void consume(ParsingContext parsingContext, String line, int lineNo);

	}

	private static abstract class AbstractParsingConsumer implements ParsingConsumer {

	}

	private static class TopParsingConsumer extends AbstractParsingConsumer {

		@Override
		public void consume(ParsingContext parsingContext, String line, int lineNo) {
			if (isEmpty(line)) {
				// Does nothing
			} else if (isComment(line)) {
				parsingContext.getBundleBuilder().comment(trimComment(line));
			} else {
				// Section
				String section;
				if ((section = getSectionHeader(line)) != null) {
					// Starts a section
					parsingContext.newSection (section);
				} else {
					// Plain line, what to do with that?
					parsingContext.error (line, lineNo, "A blank line, comment or section is expected.");
				}
			}
		}

	}
	
	private static class SectionParsingConsumer extends AbstractParsingConsumer {

		@Override
		public void consume(ParsingContext parsingContext, String line, int lineNo) {
			if (isEmpty(line)) {
				// Does nothing
			} else if (isComment(line)) {
				parsingContext.getSectionBuilder().comment(trimComment(line));
			} else {
				// Section
				String section;
				if ((section = getSectionHeader(line)) != null) {
					// Starts a section
					parsingContext.newSection (section);
				} else {
					// Key
					parsingContext.newKey(line);
				}
			}
		}
		
	}
	
	private static class KeyParsingConsumer extends AbstractParsingConsumer {

		@Override
		public void consume(ParsingContext parsingContext, String line, int lineNo) {
			if (isEmpty(line)) {
				// Does nothing
			} else if (isComment(line)) {
				parsingContext.getKeyBuilder().comment(trimComment(line));
			} else {
				// Section
				String section;
				if ((section = getSectionHeader(line)) != null) {
					// Starts a section
					parsingContext.newSection (section);
				} else {
					// FIXME Language & value
				}
			}
		}
		
	}

	private static class ParsingContext {

		private final BundleBuilder bundleBuilder;
		
		private ParsingConsumer parsingConsumer = new TopParsingConsumer();

		private BundleSectionBuilder sectionBuilder;	
		private BundleKeyBuilder keyBuilder;		

		public ParsingContext(String name) {
			bundleBuilder = BundleBuilder.create(name);
		}

		public void error(String line, int lineNo, String message) {
			throw new LSParsingException (line, lineNo, message);
		}

		public void newSection(String section) {
			if (sectionBuilder != null) {
				if (keyBuilder != null) {
					sectionBuilder.key(keyBuilder.build());
				}
				bundleBuilder.section(sectionBuilder.build());
			}
			sectionBuilder = BundleSectionBuilder.create(section);
			parsingConsumer = new SectionParsingConsumer();
		}

		public void newKey(String key) {
			if (keyBuilder != null) {
				sectionBuilder.key(keyBuilder.build());
			}
			keyBuilder = BundleKeyBuilder.create(key);
			parsingConsumer = new KeyParsingConsumer();
		}

		public BundleBuilder getBundleBuilder() {
			return bundleBuilder;
		}
		
		public BundleSectionBuilder getSectionBuilder() {
			if (sectionBuilder == null) {
				throw new IllegalStateException("No section is currently built");
			}
			return sectionBuilder;
		}
		
		public BundleKeyBuilder getKeyBuilder() {
			if (keyBuilder == null) {
				throw new IllegalStateException("No key is currently built");
			}
			return keyBuilder;
		}

		public void consume(String line, int lineNo) {
			parsingConsumer.consume(this, line, lineNo);
		}

		public Bundle build() {
			close();
			// OK
			return bundleBuilder.build();
		}

		protected void close() {
			// TODO Closes the value
			// Closes the key
			if (keyBuilder != null) {
				sectionBuilder.key(keyBuilder.build());
			}
			// Closes the section
			if (sectionBuilder != null) {
				bundleBuilder.section(sectionBuilder.build());
			}
		}

		public String getConsumerName() {
			return parsingConsumer.getClass().getSimpleName();
		}

	}

	protected static boolean isEmpty(String line) {
		return isBlank(line);
	}

	protected static String getSectionHeader(String line) {
		Matcher m = SECTION_PATTERN.matcher(line);
		if (m.matches()) {
			return m.group(1);
		} else {
			return null;
		}
	}

	protected static String trimComment(String line) {
		return trim(substring(line, COMMENT_PREFIX.length()));
	}

	protected static boolean isComment(String line) {
		return startsWith(line, COMMENT_PREFIX);
	}
	
	private final Logger logger = LoggerFactory.getLogger(LSParser.class);
	
	private final boolean trace;
	
	public LSParser() {
		this(false);
	}
	
	public LSParser(boolean trace) {
		this.trace = trace;
	}

	public Bundle parse(URL url) {
		try {
			InputStream in = url.openStream();
			try {
				return parse(url.getFile(), in);
			} finally {
				in.close();
			}
		} catch (IOException ex) {
			throw new CannotParseLSException(url, ex);
		}
	}

	protected Bundle parse(String name, InputStream in) throws IOException {
		// Reader, assuming UTF-8
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
		// Parsing context
		ParsingContext ctx = new ParsingContext(name);
		// Reads line per line
		String line;
		int no = 1;
		while ((line = reader.readLine()) != null) {
			consume(ctx, line, no++);
		}
		// OK
		return ctx.build();
	}

	protected void consume(ParsingContext ctx, String line, int lineNo) {
		if (trace) {
			logger.debug("[{}] {} --> {}", new Object[]{lineNo, ctx.getConsumerName(), line });
		}
		ctx.consume(StringUtils.trim(line), lineNo);
	}

}
