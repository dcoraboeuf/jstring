package net.sf.jstring.io.properties;

import java.util.Collections;
import java.util.List;

public class Key extends AbstractToken {

    private final String name;
    private final List<Value> values;

    public Key(int lineno, String line, String name, String text) {
        this(lineno, line, name, Collections.<Value>singletonList(new Value(lineno, line, text)), false);
    }

    private Key(int lineno, String line, String name, List<Value> values, boolean expectValues) {
        super(lineno, line, expectValues);
        this.name = name;
        this.values = values;
    }

    public Key(int lineno, String line, String name, String text, boolean expectValues) {
        this(lineno, line, name, Collections.<Value>singletonList(new Value(lineno, line, text, expectValues)), expectValues);
    }

    public List<Value> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }
}
