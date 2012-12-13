package net.sf.jstring.io.properties;


public class Key extends AbstractToken {

    private final String name;
    private final String value;

    public Key(int lineno, String line, String name, String text) {
        this(lineno, line, name, text, false);
    }

    public Key(int lineno, String line, String name, String value, boolean expectValues) {
        super(lineno, line, expectValues);
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
