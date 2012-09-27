package net.sf.jstring.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import net.sf.jstring.model.Bundle;

public class LSParser {
	
	private static class ParsingContext {
		
	}

	public static final String ENCODING = "UTF-8";

	public Bundle parse(URL url) {
		try {
			InputStream in = url.openStream();
			try {
				return parse(in);
			} finally {
				in.close();
			}
		} catch (IOException ex) {
			throw new CannotParseLSException (url, ex);
		}
	}

	protected Bundle parse(InputStream in) throws IOException {
		// Reader, assuming UTF-8
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
		// Parsing context
		ParsingContext ctx = new ParsingContext();
		// Reads line per line
		String line;
		while ((line = reader.readLine()) != null) {
			// FIXME consume (ctx, line);
		}
		// OK
		return null;
		// FIXME return ctx.getBundle();
	}

}
