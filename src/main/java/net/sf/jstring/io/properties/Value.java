package net.sf.jstring.io.properties;

public class Value extends AbstractToken {

    private final String text;

    public Value(int lineno, String line, String text) {
        this(lineno, line, text, false);
    }

    public Value(int lineno, String line, String text, boolean expectValues) {
        super(lineno, line, expectValues);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
