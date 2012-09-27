package net.sf.jstring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * Creates a {@link Localizable} from a list of {@link Localizable}s, and surrounds
 * each of them with prefixes.
 *
 * @since 1.7
 */
public class MultiLocalizable extends AbstractLocalizable {

    private final Collection<Localizable> localizables;
    private final Localizable prefix;
    private final Localizable suffix;

    /**
     * Empty list, and with default prefix (" - ") and suffix ("\n").
     */
    public MultiLocalizable() {
        this(new ArrayList<Localizable>());
    }

    /**
     * Constructor from a list of {@link Localizable}s, and with default prefix (" - ") and suffix ("\n").
     *
     * @param localizables List of {@link Localizable}s
     */
    public MultiLocalizable(Collection<? extends Localizable> localizables) {
        this(localizables, " - ", "\n");
    }

    /**
     * Constructor from a list of {@link Localizable}s, and fixed prefix and suffix.
     *
     * @param localizables List of {@link Localizable}s
     * @param prefix       Prefix to prepend to each item in the list
     * @param suffix       Suffix to prepend to each item in the list
     */
    public MultiLocalizable(Collection<? extends Localizable> localizables, String prefix, String suffix) {
        this.localizables = new ArrayList<Localizable>(localizables);
        this.prefix = new NonLocalizable(prefix);
        this.suffix = new NonLocalizable(suffix);
    }


    /**
     * Constructor from a list of {@link Localizable}s, and localizable prefix and suffix.
     *
     * @param localizables List of {@link Localizable}s
     * @param prefix       Prefix to prepend to each item in the list
     * @param suffix       Suffix to prepend to each item in the list
     */
    public MultiLocalizable(Collection<? extends Localizable> localizables, Localizable prefix, Localizable suffix) {
        this.localizables = new ArrayList<Localizable>(localizables);
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * Adds a new {@link Localizable} item to the list.
     *
     * @param localizable Item to add
     */
    public void add(Localizable localizable) {
        localizables.add(localizable);
    }

    /**
     * Gets the localisation for each item, prepends the prefix, appends the prefix, and returns
     * the concatenation of each rendered item.
     *
     * @param strings Collection of bundles to use
     * @param locale  Locale to get the message for.
     * @return Localized string
     */
    @Override
    public String getLocalizedMessage(Strings strings, Locale locale) {
        StringBuilder s = new StringBuilder();
        for (Localizable localizable : localizables) {
            String ls = localizable.getLocalizedMessage(strings, locale);
            s.append(prefix.getLocalizedMessage(strings, locale))
                    .append(ls)
                    .append(suffix.getLocalizedMessage(strings, locale));
        }
        return s.toString();
    }
}
