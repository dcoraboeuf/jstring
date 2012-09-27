package net.sf.jstring;

public enum Keys {

    FileNotFound,

    FileCannotRead,

    FileCannotParse;


    @Override
    public String toString() {
        return "jstring.test.keys." + name();
    }
}
