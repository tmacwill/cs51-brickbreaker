package web;

/**
 * This singleton class provides a centralized means of storing details about
 * the remote server.
 * 
 * @author Abraham Lin
 */
public class WebConfig {
	/**
	 * This class enumerates all network protocols supported by the application.
	 * 
	 * @author Abraham Lin
	 */
	public static enum Protocol {
		HTTP( "http" );
		
		private String value;

		/**
		 * Constructs a new <code>Protocol</code> with the given string
		 * representation.
		 * 
		 * @param value
		 *            the string representation
		 */
		private Protocol( String value ) {
			this.value = value;
		}

		/**
		 * Returns this <code>Protocol</code>'s string representation.
		 * 
		 * @return the string representation
		 */
		public String getValue( ) {
			return value;
		}
	}
	
	private static final Protocol DEFAULT_PROTOCOL = Protocol.HTTP;
	private static final String DEFAULT_HOST = "brickbreaker.x10hosting.com";
	private static final WebConfig INSTANCE = new WebConfig(
			DEFAULT_PROTOCOL,
			DEFAULT_HOST );
	
	private Protocol protocol;
	private String host;

	/**
	 * Constructs a new <code>WebConfig</code> with the specified parameters.
	 * 
	 * @param protocol
	 *            the protocol to use
	 * @param host
	 *            the hostname of the remote server
	 */
	private WebConfig( Protocol protocol, String host ) {
		this.protocol = protocol;
		this.host = host;
	}
	
	/**
	 * Returns this <code>WebConfig</code>.
	 * 
	 * @return this <code>WebConfig</code>
	 */
	public static WebConfig getInstance( ) {
		return INSTANCE;
	}

	/**
	 * Returns the current network protocol used for accessing the remote
	 * server.
	 * 
	 * @return the current network protocol
	 */
	public Protocol getProtocol( ) {
		return protocol;
	}

	/**
	 * Sets the network protocol used for accessing the remote server.
	 * 
	 * @param protocol
	 *            the new network protocol
	 */
	public void setProtocol( Protocol protocol ) {
		this.protocol = protocol;
	}
	
	/**
	 * Returns the hostname of the current remote server.
	 * 
	 * @return the hostname of the current remote server
	 */
	public String getHost( ) {
		return host;
	}

	/**
	 * Sets the hostname of the remote server.
	 * 
	 * @param host
	 *            the new hostname
	 */
	public void setHost( String host ) {
		this.host = host;
	}
}
