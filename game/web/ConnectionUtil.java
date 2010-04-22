package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class provides methods for performing HTTP requests.
 * 
 * @author Abraham Lin
 */
public class ConnectionUtil {
	/**
	 * An enumeration of all supported HTTP request methods.
	 * 
	 * @author Abraham Lin
	 */
	public static enum RequestMethod {
		GET( "GET" ),
		POST( "POST" );
		
		private String value;
		
		private RequestMethod( String value ) {
			this.value = value;
		}
		
		public String getValue( ) {
			return value;
		}
	}
	
	private static final int CONNECT_TIMEOUT_IN_MILLIS = 5000;
	private static final int READ_TIMEOUT_IN_MILLIS = 5000;

	/**
	 * Performs a GET request to the specified URL target.
	 * 
	 * @param urlString
	 *            the URL target. This parameter cannot be <code>null</code>
	 * @return the response
	 * 
	 * @throws IllegalArgumentException
	 *             if any of the arguments are <code>null</code>
	 * @throws RequestFailureException
	 *             if the request is unsuccessful
	 */
	public static String doGet( String urlString ) {
		if( urlString == null ) {
			throw new IllegalArgumentException( 
					"Argument 'url' cannot be null" );
		}
		
		return doRequest( RequestMethod.GET, urlString, null );
	}

	/**
	 * Performs a POST request to the specified URL target with the given
	 * POSTDATA.
	 * 
	 * @param urlString
	 *            the URL target. This parameter cannot be <code>null</code>
	 * @param postData
	 *            the POSTDATA. This parameter cannot be <code>null</code>
	 * @return the response
	 * 
	 * @throws IllegalArgumentException
	 *             if any of the arguments are <code>null</code>
	 * @throws RequestFailureException
	 *             if the request is unsuccessful
	 */
	public static String doPost( String urlString, String postData ) {
		if( urlString == null ) {
			throw new IllegalArgumentException( 
					"Argument 'url' cannot be null" );
		}
		if( postData == null ) {
			throw new IllegalArgumentException(
					"Argument 'data' cannot be null" );
		}
		
		return doRequest( RequestMethod.POST, urlString, postData );
	}

	/**
	 * Actually peforms the request.
	 * 
	 * @param method
	 *            the HTTP method for the request (currently only supports GET
	 *            and POST). This parameter cannot be <code>null</code>
	 * @param urlString
	 *            the URL target for the request. This parameter cannot be
	 *            <code>null</code>
	 * @param postData
	 *            the POSTDATA to send. This parameter is ignored if the
	 *            specified method is GET
	 * @return the response
	 * 
	 * @throws RequestFailureException
	 *             if the request is unsuccessful
	 */
	private static String doRequest( RequestMethod method, String urlString,
			String postData ) {
		assert method != null : "method is null";
		assert urlString != null : "urlString is null";
		
		URL url;
		try {
			url = new URL( urlString );
		} catch( MalformedURLException e ) {
			// Unrecoverable error
			throw new RequestFailureException( e );
		}
		
		StringBuilder response = new StringBuilder( );
		
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection)url.openConnection( );
			
			conn.setConnectTimeout( CONNECT_TIMEOUT_IN_MILLIS );
			conn.setReadTimeout( READ_TIMEOUT_IN_MILLIS );
			
			conn.setDoInput( true );	// The response is "input"

			assert method == RequestMethod.GET 
					|| method == RequestMethod.POST 
							: "Invalid method: " + method;
			if( method == RequestMethod.GET ) {
				conn.setRequestMethod( RequestMethod.GET.getValue( ) );
				conn.setDoOutput( false );	// The request is "output"
			} else {
				assert postData != null : "postData is null";
				conn.setRequestMethod( RequestMethod.POST.getValue( ) );
				conn.setDoOutput( true );	// The request is "output"
				
				conn.setRequestProperty(
						"Content-Type",
						"application/x-www-form-urlencoded" );
				
				// Send the request
				Writer requestWriter = new BufferedWriter( 
						new OutputStreamWriter( conn.getOutputStream( ) ) );
				requestWriter.write( postData );
				requestWriter.close( );
			}
			
			// Get the response
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader( conn.getInputStream( ) ) );
			String buffer;
			while( ( buffer = responseReader.readLine( ) ) != null ) {
				response.append( buffer ).append( "\n" );
			}
			responseReader.close( );
		} catch( IOException e ) {
			// Unrecoverable error
			throw new RequestFailureException( e );
		} finally {
			if( conn != null ) {
				conn.disconnect( );
			}
		}
		
		return response.toString( );
	}
}
