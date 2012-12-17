package net.sf.jstring;

import net.sf.jstring.model.BundleCollection;

public interface Validator {

    ValidationResult validate (Strings strings);

    ValidationResult validate (BundleCollection bundleCollection, SupportedLocales supportedLocales);

}
