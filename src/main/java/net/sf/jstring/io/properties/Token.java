package net.sf.jstring.io.properties;

public interface Token {

    boolean expectValues ();

    int getLineno();

    String getLine();
}
