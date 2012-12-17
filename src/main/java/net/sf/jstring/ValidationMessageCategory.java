package net.sf.jstring;

public enum ValidationMessageCategory {

    MISSING_KEY_IN_LANGUAGE (ValidationMessageType.ERROR);

    private final ValidationMessageType type;

    private ValidationMessageCategory(ValidationMessageType type) {
        this.type = type;
    }

    public ValidationMessageType getType() {
        return type;
    }
}
