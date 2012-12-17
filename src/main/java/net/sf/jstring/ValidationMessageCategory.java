package net.sf.jstring;

import static net.sf.jstring.ValidationMessageType.ERROR;

public enum ValidationMessageCategory {

    MISSING_KEY_IN_LANGUAGE (ERROR),
    MISSING_LOCALE(ERROR);

    private final ValidationMessageType type;

    private ValidationMessageCategory(ValidationMessageType type) {
        this.type = type;
    }

    public ValidationMessageType getType() {
        return type;
    }
}
