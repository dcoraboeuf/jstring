package net.sf.jstring.io.properties;

public class Comment extends AbstractToken {

    private final String comment;

    public Comment(int lineno, String line, String comment) {
        this(lineno, line, comment, false);
    }

    public Comment(int lineno, String line, String comment, boolean expectValues) {
        super(lineno, line, expectValues);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
