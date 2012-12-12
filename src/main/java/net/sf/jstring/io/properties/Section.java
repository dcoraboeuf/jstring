package net.sf.jstring.io.properties;

public class Section extends AbstractToken {
    private final String name;

    public Section(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
