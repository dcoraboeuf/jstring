package net.sf.jstring.io.ls;

import net.sf.jstring.builder.BundleBuilder;
import net.sf.jstring.builder.BundleKeyBuilder;
import net.sf.jstring.builder.BundleSectionBuilder;
import net.sf.jstring.builder.BundleValueBuilder;
import net.sf.jstring.io.AbstractParser;
import net.sf.jstring.io.CannotOpenException;
import net.sf.jstring.io.CannotParseException;
import net.sf.jstring.model.Bundle;
import net.sf.jstring.model.BundleValue;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.*;

public class LSParser extends AbstractParser<LSParser> {

	private static final String ENCODING = "UTF-8";

	private static final String COMMENT_PREFIX = "#";
	private static final String ESCAPED_COMMENT_PREFIX = "##";
	private static final String MULTILINE_SEPARATOR = "...";

	private static final Pattern SECTION_PATTERN = Pattern.compile("^\\[(.*)\\]$");
	private static final Pattern LANGUAGE_VALUE_PATTERN = Pattern.compile("^(.+)\\->(.*)$");

	private static interface ParsingConsumer {

		void consume(String line, int lineNo);

		void close();

	}

	private static abstract class AbstractParsingConsumer implements ParsingConsumer {

	}

	private class ParsingContext {

		private class TopParsingConsumer extends AbstractParsingConsumer {

			private static final String SECTION_DEFAULT = "default";
			private final BundleBuilder bundleBuilder;

			private TopParsingConsumer(BundleBuilder bundleBuilder) {
				this.bundleBuilder = bundleBuilder;
			}
			
			@Override
			public void close() {
			}

			@Override
			public void consume(String line, int lineNo) {
				if (isEmpty(line)) {
					// Does nothing
				} else if (isComment(line)) {
					bundleBuilder.comment(trimComment(line));
				} else {
					// Section
					String section;
					if ((section = getSectionHeader(line)) != null) {
						// Starts a section
						newSection(section);
					} else {
						// Default section
						BundleSectionBuilder sectionBuilder = newSection(SECTION_DEFAULT);
						// Starts the key
						newKey(sectionBuilder, line);
					}
				}
			}
			
			@Override
			public String toString() {
				return "[top]";
			}

		}

		private class SectionParsingConsumer extends AbstractParsingConsumer {

			private final BundleBuilder bundleBuilder;
			private final BundleSectionBuilder sectionBuilder;

			public SectionParsingConsumer(BundleBuilder bundleBuilder, String section) {
				this.bundleBuilder = bundleBuilder;
				this.sectionBuilder = BundleSectionBuilder.create(section);
			}

			@Override
			public void consume(String line, int lineNo) {
				if (isEmpty(line)) {
					// Does nothing
				} else if (isComment(line)) {
					sectionBuilder.comment(trimComment(line));
				} else {
					// Section
					String section;
					if ((section = getSectionHeader(line)) != null) {
						// Starts a section
						newSection(section);
					} else {
						// Key
						newKey(sectionBuilder, line);
					}
				}
			}
			
			@Override
			public void close() {
				bundleBuilder.section(sectionBuilder.build());
			}
			
			@Override
			public String toString() {
				return "[section] " + sectionBuilder.getName();
			}

		}

		private class KeyParsingConsumer extends AbstractParsingConsumer {

			private static final String MULTILINE_SEPARATOR = "...";

			private final BundleSectionBuilder sectionBuilder;
			private final BundleKeyBuilder keyBuilder;

			public KeyParsingConsumer(BundleSectionBuilder sectionBuilder, String key) {
				this.sectionBuilder = sectionBuilder;
				this.keyBuilder = BundleKeyBuilder.create(key);
			}

			@Override
			public void consume(String line, int lineNo) {
				if (isEmpty(line)) {
					// Does nothing
				} else if (isComment(line)) {
					keyBuilder.comment(trimComment(line));
				} else {
					// Section
					String section;
					if ((section = getSectionHeader(line)) != null) {
						// Starts a section
						newSection(section);
					} else {
						// Language & value
						Matcher m = LANGUAGE_VALUE_PATTERN.matcher(line);
						if (m.matches()) {
							String language = trim(m.group(1));
							String value = trim(m.group(2));
							// ... special value, that indicates that the value
							// is stored on several lines
							if (MULTILINE_SEPARATOR.equals(value)) {
								// Starts the parsing of the value
								newValue(keyBuilder, language);
							}
							// Direct value
							else {
								// Resolve the escaping
								value = StringEscapeUtils.unescapeJava(value);
								// Adds the value
								keyBuilder.value(language, BundleValue.value(value));
							}
						} else {
							// Any other, that is another key
							newKey(sectionBuilder, line);
						}
					}
				}
			}
			
			@Override
			public void close() {
				sectionBuilder.key(keyBuilder.build());
			}
			
			@Override
			public String toString() {
				return "[key] " + keyBuilder.getName();
			}

		}

		private class ValueParsingConsumer extends AbstractParsingConsumer {
			
			private final BundleKeyBuilder keyBuilder;
			private final String language;
			private final BundleValueBuilder valueBuilder;

			public ValueParsingConsumer(BundleKeyBuilder keyBuilder, String language) {
				this.keyBuilder = keyBuilder;
				this.language = language;
				this.valueBuilder = BundleValueBuilder.create();
			}

			@Override
			public void consume(String line, int lineNo) {
				if (MULTILINE_SEPARATOR.equals(line)) {
					// End of the value
					quitValue();
				} else if (isEscapedComment(line)) {
					valueBuilder.value(trimComment(line));
				} else if (isComment(line)) {
					valueBuilder.comment(trimComment(line));
				} else {
					// Resolve the escaping
					String value = StringEscapeUtils.unescapeJava(line);
					// Adding the value
					valueBuilder.value(value);
				}
			}
			
			@Override
			public void close() {
				keyBuilder.value(language, valueBuilder.build());
			}
			
			@Override
			public String toString() {
				return "[value] " + language;
			}

		}

		private final BundleBuilder bundleBuilder;

		private final Stack<ParsingConsumer> consumerStack = new Stack<LSParser.ParsingConsumer>();

		public ParsingContext(String name) {
			bundleBuilder = BundleBuilder.create(name);
			consumerStack.push(new TopParsingConsumer(bundleBuilder));
		}

		protected void closeUntil(Class<? extends ParsingConsumer> consumerClass) {
			while (!consumerClass.isInstance(consumerStack.peek())) {
				ParsingConsumer consumer = consumerStack.pop();
				consumer.close();
			}
		}

		public BundleSectionBuilder newSection(String section) {
			closeUntil(TopParsingConsumer.class);
			SectionParsingConsumer sectionParsingConsumer = new SectionParsingConsumer(bundleBuilder, section);
			consumerStack.push(sectionParsingConsumer);
			return sectionParsingConsumer.sectionBuilder;
		}

		public void newKey(BundleSectionBuilder sectionBuilder, String key) {
			closeUntil(SectionParsingConsumer.class);
			consumerStack.push(new KeyParsingConsumer(sectionBuilder, key));
		}

		public void newValue(BundleKeyBuilder keyBuilder, String language) {
			closeUntil(KeyParsingConsumer.class);
			consumerStack.push(new ValueParsingConsumer(keyBuilder, language));
		}

		public void quitValue() {
			closeUntil(KeyParsingConsumer.class);
		}

		public Bundle build() {
			closeUntil(TopParsingConsumer.class);
			return bundleBuilder.build();
		}

		public void consume(String line, int lineNo) {
			consumerStack.peek().consume(line, lineNo);
		}

		public String getConsumerName() {
			return consumerStack.peek().toString();
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

	protected static boolean isEscapedComment(String line) {
		return startsWith(line, ESCAPED_COMMENT_PREFIX);
	}

	protected static boolean isComment(String line) {
		return startsWith(line, COMMENT_PREFIX);
	}

	public LSParser() {
		super();
	}

	protected LSParser(boolean trace) {
		super(trace);
	}
	
	@Override
	public LSParser withTraces() {
		return new LSParser(true);
	}

	@Override
	public Bundle parse(URL url) {
		try {
			InputStream in = url.openStream();
			if (in == null) {
				throw new CannotOpenException(url);
			} else {
				try {
					return parse(getBundleName(url), in);
				} finally {
					in.close();
				}
			}
		} catch (IOException ex) {
			throw new CannotParseException(url, ex);
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
        trace("[{}] {} --> {}", lineNo, ctx.getConsumerName(), line);
		ctx.consume(StringUtils.trim(line), lineNo);
	}

}
