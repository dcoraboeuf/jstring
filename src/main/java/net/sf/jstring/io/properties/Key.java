package net.sf.jstring.io.properties;

import java.util.Collections;
import java.util.List;

public class Key extends AbstractToken {

    private final String name;
    private final List<Value> values;

    public Key(String name, String text) {
        this(name, Collections.<Value>singletonList(new Value(text)), false);
    }

    private Key(String name, List<Value> values, boolean expectValues) {
        super(expectValues);
        this.name = name;
        this.values = values;
    }

    public Key(String name, String text, boolean expectValues) {
        this(name, Collections.<Value>singletonList(new Value(text, expectValues)), expectValues);
    }

    public List<Value> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }
}
