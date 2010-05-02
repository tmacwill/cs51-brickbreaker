package brickBreaker.web;

/**
 * This class provides an application-specific wrapper for runtime exceptions
 * caused by a failure to complete a request to a remote server.
 * 
 * @author Abraham Lin
 */
public class RequestFailureException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>RequestFailureException</code> with
	 * <code>null</code> as its detail message.
	 */
	public RequestFailureException( ) {
		super( );
	}

	/**
	 * Constructs a new <code>RequestFailureException</code> with the specified
	 * detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public RequestFailureException( String message ) {
		super( message );
	}

	/**
	 * Constructs a new <code>RequestFailureException</code> with the specified
	 * detail message and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public RequestFailureException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Constructs a new <code>RequestFailureException</code> with the specified
	 * cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public RequestFailureException( Throwable cause ) {
		super( cause );
	}
}
