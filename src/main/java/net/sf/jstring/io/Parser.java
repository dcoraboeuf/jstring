package net.sf.jstring.io;

import java.net.URL;

import net.sf.jstring.model.Bundle;

public interface Parser {

	Bundle parse(URL url);

}
