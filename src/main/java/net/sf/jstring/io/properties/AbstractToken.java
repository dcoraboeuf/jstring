package net.sf.jstring.io.properties;

public abstract class AbstractToken implements Token {

    private final boolean expectValues;

    protected AbstractToken() {
        this(false);
    }

    protected AbstractToken(boolean expectValues) {
        this.expectValues = expectValues;
    }

    @Override
    public boolean expectValues() {
        return expectValues;
    }
}
