package brickBreaker.web;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	private static final String ENCODING_SCHEME = "UTF-8";
	
	private static final int CONNECT_TIMEOUT_IN_MILLIS = 5000;
	private static final int READ_TIMEOUT_IN_MILLIS = 5000;
	
	private static final int RESPONSE_BUFFER_SIZE = 512;

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
	public static byte[] doGet( String urlString ) {
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
	public static byte[] doPost( String urlString, Map<String, String> postData ) {
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
	private static byte[] doRequest( RequestMethod method, String urlString,
			Map<String, String> postData ) {
		assert method != null : "method is null";
		assert urlString != null : "urlString is null";
		
		URL url;
		try {
			url = new URL( urlString );
		} catch( MalformedURLException e ) {
			// Unrecoverable error
			throw new RequestFailureException( e );
		}
		
		byte[] data;
		
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
				StringBuilder postDataBuilder = new StringBuilder( 1024 );
				Set<String> paramNames = postData.keySet( );
				for( String paramName : paramNames ) {
					postDataBuilder
							.append( paramName )
							.append( "=" )
							.append( encodeURLComponent( postData.get( 
									paramName ) ) )
							.append( "&" );
				}
				
				// Send the request
				Writer requestWriter = new BufferedWriter( 
						new OutputStreamWriter( conn.getOutputStream( ) ) );
				requestWriter.write( postDataBuilder.toString( ) );
				requestWriter.close( );
			}
			
			// Get the response
			DataInputStream responseReader = new DataInputStream( conn
					.getInputStream( ) );
			List<byte[]> buffers = new ArrayList<byte[]>( );
			int totalBytesRead = 0;
			
			// Read the response in chunks
			byte[] buffer = new byte[RESPONSE_BUFFER_SIZE];
			int bytesRead;
			while( ( bytesRead = responseReader.read( buffer ) ) > 0 ) {
				buffers.add( Arrays.copyOf( buffer, bytesRead ) );
				totalBytesRead += bytesRead;
			}
			responseReader.close( );
			
			// Combine the chunks into a single array
			data = new byte[totalBytesRead];
			int bytesCopied = 0;
			int numBuffers = buffers.size( );
			for( int i = 0; i < numBuffers; i++ ) {
				buffer = buffers.get( i );
				System.arraycopy( buffer, 0, data, bytesCopied, buffer.length );
				bytesCopied += buffer.length;
			}
		} catch( IOException e ) {
			// Unrecoverable error
			throw new RequestFailureException( e );
		} finally {
			if( conn != null ) {
				conn.disconnect( );
			}
		}
		
		return data;
	}

	/**
	 * Encodes a URL fragment to <code>application/x-www-form-urlencoded</code>
	 * format, using the UTF-8 encoding scheme.
	 * 
	 * @param fragment
	 *            the URL fragment to encode
	 * @return the encoded URL fragment
	 */
	public static String encodeURLComponent( String fragment ) {
		try {
			return URLEncoder.encode( fragment, ENCODING_SCHEME );
		} catch( UnsupportedEncodingException e ) {
			// Should never happen
			throw new RuntimeException( e );
		}
	}
}
