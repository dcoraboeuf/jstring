package net.sf.jstring.io;

import java.net.URL;

import net.sf.jstring.model.Bundle;

public interface Parser<P extends Parser<P>> {

	Bundle parse(URL url);
	
	P withTraces ();

}
