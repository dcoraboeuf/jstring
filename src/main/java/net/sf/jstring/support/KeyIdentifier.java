package net.sf.jstring.support;

import lombok.Data;

@Data
public class KeyIdentifier {

    private final String bundle;
    private final String section;
    private final String key;

}
