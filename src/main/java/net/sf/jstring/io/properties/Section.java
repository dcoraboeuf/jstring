package net.sf.jstring.io.properties;

public class Section extends AbstractToken {
    private final String name;

    public Section(int lineno, String line, String name) {
        super(lineno, line);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
