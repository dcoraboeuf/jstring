package net.sf.jstring;

public enum LocalePolicy {

    /**
     * If the locale is not defined, augment the locale scope (fr_FR -&gt; fr) until reaching the default.
     */
    USE_DEFAULT
}
