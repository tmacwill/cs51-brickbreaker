package brickBreaker.web;

/**
 * This class provides an application-specific wrapper for exceptions caused by
 * a failure to encrypt data.
 * 
 * @author Abraham Lin
 */
public class EncryptionFailureException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>EncryptionFailureException</code> with
	 * <code>null</code> as its detail message.
	 */
	public EncryptionFailureException( ) {
		super( );
	}

	/**
	 * Constructs a new <code>EncryptionFailureException</code> with the
	 * specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public EncryptionFailureException( String message ) {
		super( message );
	}

	/**
	 * Constructs a new <code>EncryptionFailureException</code> with the
	 * specified detail message and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public EncryptionFailureException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Constructs a new <code>EncryptionFailureException</code> with the
	 * specified cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public EncryptionFailureException( Throwable cause ) {
		super( cause );
	}
}
