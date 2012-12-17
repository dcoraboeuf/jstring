package net.sf.jstring.validation;

import net.sf.jstring.Strings;
import net.sf.jstring.ValidationMessage;
import net.sf.jstring.ValidationMessageCategory;

import java.util.Locale;

public class DefaultValidationMessage implements ValidationMessage {

    private final ValidationMessageCategory category;
    private final Object[] parameters;

    public DefaultValidationMessage(ValidationMessageCategory category, Object[] params) {
        this.category = category;
        this.parameters = params;
    }

    @Override
    public ValidationMessageCategory getCategory() {
        return category;
    }

    @Override
    public String getLocalizedMessage(Strings strings, Locale locale) {
        return strings.get(locale, ValidationMessageCategory.class.getName() + "." + category, parameters);
    }
}
