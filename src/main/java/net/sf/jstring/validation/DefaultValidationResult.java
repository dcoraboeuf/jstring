package net.sf.jstring.validation;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import net.sf.jstring.ValidationMessage;
import net.sf.jstring.ValidationMessageCategory;
import net.sf.jstring.ValidationMessageType;
import net.sf.jstring.ValidationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DefaultValidationResult implements ValidationResult {

    private final List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

    public void add(ValidationMessageCategory category, Object... params) {
        messages.add(new DefaultValidationMessage (category, params));
    }

    @Override
    public boolean hasErrors() {
        return Iterables.any(messages, new Predicate<ValidationMessage>() {
            @Override
            public boolean apply(ValidationMessage validationMessage) {
                return validationMessage.getCategory().getType() == ValidationMessageType.ERROR;
            }
        });
    }

    @Override
    public boolean hasWarnings() {
        return Iterables.any(messages, new Predicate<ValidationMessage>() {
            @Override
            public boolean apply(ValidationMessage validationMessage) {
                return validationMessage.getCategory().getType() == ValidationMessageType.WARNING;
            }
        });
    }
}
