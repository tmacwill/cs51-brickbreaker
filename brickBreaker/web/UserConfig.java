package brickBreaker.web;

/**
 * This singleton class provides a centralized means of storing user
 * credentials.
 * 
 * @author Abraham Lin
 */
public class UserConfig {
	private static final UserConfig INSTANCE = new UserConfig( "", "" );
	
	private String username;
	private String password;

	/**
	 * Constructs a new <code>UserConfig</code> with the specified parameters.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	private UserConfig( String username, String password ) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Returns this <code>UserConfig</code>.
	 * 
	 * @return this <code>UserConfig</code>
	 */
	public static UserConfig getInstance( ) {
		return INSTANCE;
	}
	
	/**
	 * Returns the current user's username.
	 * 
	 * @return the current user's username
	 */
	public String getUsername( ) {
		return username;
	}

	/**
	 * Sets the current user's username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername( String username ) {
		this.username = username;
	}
	
	/**
	 * Returns the current user's password.
	 * 
	 * @return the current user's password
	 */
	public String getPassword( ) {
		return password;
	}

	/**
	 * Sets the current user's password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword( String password ) {
		this.password = password;
	}
}
