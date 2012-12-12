package net.sf.jstring.io.properties;

public class Comment extends AbstractToken {

    private final String comment;

    public Comment(String comment) {
        this(comment, false);
    }

    public Comment(String comment, boolean expectValues) {
        super(expectValues);
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
