package net.sf.jstring.io.properties;

public class Value extends AbstractToken {

    private final String text;

    public Value(String text) {
        this(text, false);
    }

    public Value(String text, boolean expectValues) {
        super(expectValues);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
