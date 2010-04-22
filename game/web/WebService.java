package web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class provides web-based services for interacting with a remote server.
 * 
 * @author Abraham Lin
 */
public class WebService {
	private static final String URL_SUFFIX = ".xml";
	
	private static final String CLIENT_KEY = "2d4d4bb4ef2948f7974e072b0c613d97";
	
	private static final String USER_VERIFY_PATH = "/users/verify";
	private static final String LEVEL_UPLOAD_PATH = "/blobs/add";
	
	private static final String ENCODING_SCHEME = "UTF-8";

	/**
	 * Uploads a level to a remote server
	 * 
	 * @param level
	 *            the level to upload
	 * @param title
	 *            the name for the level
	 */
	public static void uploadLevel( Level level, String title ) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream( );
		try {
			ObjectOutputStream objStream = new ObjectOutputStream( byteStream );
			objStream.writeObject( level );
			objStream.close( );
		} catch( IOException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
		
		byte[] levelData = byteStream.toByteArray( );
		String encodedLevelData = Base64.encodeBase64String( levelData );
		
		String userID = getUserID( );
		if( userID == null ) {
			// TODO: Better error handling
			throw new RuntimeException( "Invalid login credentials" );
		}
		
		String levelUploadURL = getLevelUploadURL(
				userID,
				title,
				encodedLevelData );
		String response = ConnectionUtil.doPost( levelUploadURL, "" );
		
		// TODO: Verify response
	}

	/**
	 * Obtains the current user's ID from the remote server.
	 * 
	 * @return the current user's ID if the supplied credentials are valid;
	 *         otherwise, returns <code>null</code>
	 */
	private static String getUserID( ) {
		String userID = null;
		
		WebConfig webConfig = WebConfig.getInstance( );
		UserConfig userConfig = UserConfig.getInstance( );
		String userVerifyURL = new StringBuilder( )
				.append( webConfig.getProtocol( ).getValue( ) )
				.append( "://" )
				.append( webConfig.getHost( ) )
				.append( USER_VERIFY_PATH )
				.append( "/" )
				.append( encodeURLComponent( userConfig.getUsername( ) ) )
				.append( "/" )
				.append( encodeURLComponent( userConfig.getPassword( ) ) )
				.append( URL_SUFFIX	)
				.toString( );
		
		String response = ConnectionUtil.doGet( userVerifyURL );
		Document document = XMLUtil.parseXML( response );
		if( document != null ) {
			// TODO: Error handling
			NodeList userNodes = document.getElementsByTagName( "user" );
			if( userNodes.getLength( ) > 0 ) {
				Node userNode = userNodes.item( 0 );
				if( userNode.getNodeType( ) == Node.ELEMENT_NODE ) {
					userID = ((Element)userNode).getAttribute( "id" );
				}
			}
		}
		
		return userID;
	}

	/**
	 * Returns the URL for uploading levels to the remote server.
	 * 
	 * @param userID
	 *            the current user's ID
	 * @param title
	 *            the name of the level
	 * @param encodedLevelData
	 *            the level as a base64-encoded string
	 * @return the URL for uploading levels
	 */
	private static String getLevelUploadURL( String userID, String title,
			String encodedLevelData ) {
		WebConfig config = WebConfig.getInstance( );
		return new StringBuilder( )
				.append( config.getProtocol( ).getValue( ) )
				.append( "://" )
				.append( config.getHost( ) )
				.append( LEVEL_UPLOAD_PATH )
				.append( "/" )
				.append( encodeURLComponent( CLIENT_KEY ) )
				.append( "/" )
				.append( encodeURLComponent( userID ) )
				.append( "/" )
				.append( encodeURLComponent( title ) )
				.append( "/" )
				.append( encodeURLComponent( encodedLevelData ) )
				.toString( );
	}

	/**
	 * Encodes a URL fragment to <code>application/x-www-form-urlencoded</code>
	 * format, using the UTF-8 encoding scheme.
	 * 
	 * @param url
	 *            the URL fragment to encode
	 * @return the encoded URL fragment
	 */
	private static String encodeURLComponent( String url ) {
		return URLEncoder.encode( url, ENCODING_SCHEME );
	}
}
