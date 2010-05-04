package brickBreaker.web;

/**
 * This class provides an application-specific wrapper for exceptions caused by
 * a failure to authenticate against a remote server.
 * 
 * @author Abraham Lin
 */
public class InvalidUserException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>InvalidUserException</code> with <code>null</code>
	 * as its detail message.
	 */
	public InvalidUserException( ) {
		super( );
	}

	/**
	 * Constructs a new <code>InvalidUserException</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public InvalidUserException( String message ) {
		super( message );
	}

	/**
	 * Constructs a new <code>InvalidUserException</code> with the specified
	 * detail message and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public InvalidUserException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Constructs a new <code>InvalidUserException</code> with the specified
	 * cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public InvalidUserException( Throwable cause ) {
		super( cause );
	}
}
