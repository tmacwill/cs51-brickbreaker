package brickBreaker.web;

/**
 * This class provides an application-specific wrapper for exceptions caused by
 * a failure to parse string representations of XML.
 * 
 * @author Abraham Lin
 */
public class XMLParseFailureException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>XMLParseFailureException</code> with
	 * <code>null</code> as its detail message.
	 */
	public XMLParseFailureException( ) {
		super( );
	}

	/**
	 * Constructs a new <code>XMLParseFailureException</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public XMLParseFailureException( String message ) {
		super( message );
	}

	/**
	 * Constructs a new <code>XMLParseFailureException</code> with the specified
	 * detail message and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public XMLParseFailureException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Constructs a new <code>XMLParseFailureException</code> with the specified
	 * cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public XMLParseFailureException( Throwable cause ) {
		super( cause );
	}
}
