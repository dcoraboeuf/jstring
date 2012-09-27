package net.sf.jstring;

import java.util.Locale;

import org.apache.commons.lang3.Validate;

/**
 * Model for exceptions that are using a code and some parameters for their
 * message
 * 
 * @author Damien Coraboeuf
 */
public class LocalizableException extends RuntimeException implements Localizable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Code
	 */
	private String code;

	/**
	 * Parameters
	 */
	private Object[] parameters;

	/**
	 * Copy
	 * 
	 * @param ex
	 *            Exception to copy
	 */
	public LocalizableException(LocalizableException ex) {
		super(ex.code, ex);
		this.code = ex.code;
		this.parameters = ex.parameters;
	}

	/**
	 * Only one code
	 * 
	 * @param code
	 *            Code of the exception
	 */
	public LocalizableException(String code) {
		super(code);
		this.code = code;
		this.parameters = null;
	}

	/**
	 * Constructor with some parameters
	 * 
	 * @param code
	 *            Code of the exception
	 * @param params
	 *            Parameters of the exception
	 */
	public LocalizableException(String code, Object... params) {
		super(code);
		this.code = code;
		this.parameters = params;
	}

	/**
	 * Constructor with some parameters
	 *
	 * @param code
	 *            Code of the exception as a non-null object
	 * @param params
	 *            Parameters of the exception
	 */
	public LocalizableException(Object code, Object... params) {
		super(code.toString());
		this.code = code.toString();
		this.parameters = params;
	}

	/**
	 * Full constructor
	 * 
	 * @param code
	 *            Code of the exception as a non-null object
	 * @param error
	 *            Source exception
	 * @param params
	 *            Parameters of the exception
	 */
	public LocalizableException(Object code, Throwable error, Object... params) {
		super(code.toString());
		initCause(error);
		this.code = code.toString();
		this.parameters = params;
	}

	/**
	 * Full constructor
	 *
	 * @param code
	 *            Code of the exception
	 * @param error
	 *            Source exception
	 * @param params
	 *            Parameters of the exception
	 */
	public LocalizableException(String code, Throwable error, Object... params) {
		super(code);
		initCause(error);
		this.code = code;
		this.parameters = params;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return this.code;
	}

    @Override
    public String getLocalizedMessage(Strings strings, Locale locale) {
        Validate.notNull(strings, "The strings argument is required.");
        return strings.get(locale, getCode(), getParameters());
    }

    /**
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return getLocalizedMessage();
	}

	/**
	 * @return Returns the parameters.
	 */
	public Object[] getParameters() {
		return this.parameters;
	}
}
