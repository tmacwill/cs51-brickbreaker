package brickBreaker.web;

/**
 * This class provides an application-specific wrapper for exceptions caused by
 * failures to perform web-related functions.
 * 
 * @author Abraham Lin
 */
public class WebException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>WebException</code> with <code>null</code> as its
	 * detail message.
	 */
	public WebException( ) {
		super( );
	}

	/**
	 * Constructs a new <code>WebException</code> with the specified detail
	 * message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public WebException( String message ) {
		super( message );
	}

	/**
	 * Constructs a new <code>WebException</code> with the specified detail
	 * message and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public WebException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Constructs a new <code>WebException</code> with the specified cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public WebException( Throwable cause ) {
		super( cause );
	}
}
