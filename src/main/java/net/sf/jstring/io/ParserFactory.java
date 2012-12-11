package net.sf.jstring.io;

import java.net.URL;

public interface ParserFactory {

    Parser<?> getParser(URL path);

}
