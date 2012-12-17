package net.sf.jstring.validation;

import net.sf.jstring.Strings;
import net.sf.jstring.SupportedLocales;
import net.sf.jstring.ValidationResult;
import net.sf.jstring.Validator;
import net.sf.jstring.model.BundleCollection;

public class DefaultValidator implements Validator {

    /**
     * @see #validate(net.sf.jstring.model.BundleCollection, net.sf.jstring.SupportedLocales)
     */
    @Override
    public ValidationResult validate(Strings strings) {
        return validate(strings.getBundleCollection(), strings.getSupportedLocales());
    }

    @Override
    public ValidationResult validate(BundleCollection bundleCollection, SupportedLocales supportedLocales) {
        // Creates the result
        DefaultValidationResult result = new DefaultValidationResult();



        // OK
        return result;

    }
}
