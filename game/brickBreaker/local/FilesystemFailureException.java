package brickBreaker.local;

/**
 * This class provides an application-specific wrapper for exceptions caused by
 * a failure in interacting with the local filesystem.
 * 
 * @author Abraham Lin
 */
public class FilesystemFailureException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>FilesystemFailureException</code> with
	 * <code>null</code> as its detail message.
	 */
	public FilesystemFailureException( ) {
		super( );
	}

	/**
	 * Constructs a new <code>FilesystemFailureException</code> with the
	 * specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public FilesystemFailureException( String message ) {
		super( message );
	}

	/**
	 * Constructs a new <code>FilesystemFailureException</code> with the
	 * specified detail message and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public FilesystemFailureException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Constructs a new <code>FilesystemFailureException</code> with the
	 * specified cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public FilesystemFailureException( Throwable cause ) {
		super( cause );
	}
}
