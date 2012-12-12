package net.sf.jstring;

public enum LocalePolicy {

    /**
     * If the locale is not defined, augment the locale scope (fr_FR -&gt; fr) until reaching the default.
     */
    EXTENDS,

    /**
     * If the locale is not defined, uses the default
     */
    DEFAULT,

    /**
     * If the locale is not defined, raises an error. This is the default.
     */
    ERROR;


}
