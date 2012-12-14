package net.sf.jstring.builder;

import java.util.*;

public abstract class AbstractBuilderCommented<T, B extends AbstractBuilderCommented<T,B>> extends Builder<T> {

    private final List<String> comments = new ArrayList<String>();

    @SuppressWarnings("unchecked")
    public B comment (String comment) {
        comments.add(comment);
        return (B) this;
    }

    protected List<String> getComments() {
        return comments;
    }

    protected void mergeComments(Collection<String> source) {
        // Converts the target as a set (preserving order)
        Set<String> targetSet = new LinkedHashSet<String>(comments);
        // Adds all items of the source if they are not there yet
        List<String> itemsToAdd = new ArrayList<String>();
        for (String e: source) {
            if (!targetSet.contains(e)) {
                itemsToAdd.add(e);
            }
        }
        // Adds those new items to the target
        comments.addAll(itemsToAdd);
    }
}
