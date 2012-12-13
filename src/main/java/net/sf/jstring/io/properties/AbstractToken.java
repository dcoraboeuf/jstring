package net.sf.jstring.io.properties;

public abstract class AbstractToken implements Token {

    private final int lineno;
    private final String line;
    private final boolean expectValues;

    protected AbstractToken(int lineno, String line) {
        this(lineno, line, false);
    }

    protected AbstractToken(int lineno, String line, boolean expectValues) {
        this.lineno = lineno;
        this.line = line;
        this.expectValues = expectValues;
    }

    @Override
    public int getLineno() {
        return lineno;
    }

    @Override
    public String getLine() {
        return line;
    }

    @Override
    public boolean expectValues() {
        return expectValues;
    }
}
