package brickBreaker.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import brickBreaker.Level;
import brickBreaker.local.LevelCatalog;

/**
 * This class provides web-based services for interacting with a remote server.
 * 
 * @author Abraham Lin
 */
public class WebService {
	private static final String URL_SUFFIX = ".xml";
	
	private static final String XML_PARAM_NAME = "postdata";
	
	private static final String CLIENT_KEY = "2d4d4bb4ef2948f7974e072b0c613d97";
	
	private static final String USER_VERIFY_PATH = "/users/verify";
	private static final String LEVEL_UPLOAD_PATH = "/blobs/add";
	private static final String SCORE_SUBMIT_PATH = "/scores/add";

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
		
		String levelUploadURL = getLevelUploadURL( );
		Map<String, String> levelUploadRequest = getLevelUploadRequest( userID,
				title,
				encodedLevelData );
		
		String response = ConnectionUtil.doPost(
				levelUploadURL,
				levelUploadRequest );
		
		// TODO: Verify response
	}
	
	/**
	 * 
	 * 
	 * @param level
	 * @param score
	 */
	public static void submitScore( Level level, long score ) {
		String levelID = LevelCatalog.getInstance( ).getLevelID( level );
		
		String userID = getUserID( );
		if( userID == null ) {
			// TODO: Better error handling
			throw new RuntimeException( "Invalid login credentials" );
		}
		
		String scoreSubmitURL = getScoreSubmitURL( );
		Map<String, String> scoreSubmitRequest = getScoreSubmitRequest(
				userID,
				levelID,
				score );
		
		String response = ConnectionUtil.doPost(
				scoreSubmitURL,
				scoreSubmitRequest );
		
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
		
		String userVerifyURL = getUserVerifyURL( );
		Map<String, String> userVerifyReqest = getUserVerifyRequest( );
		
		String response = ConnectionUtil.doPost(
				userVerifyURL,
				userVerifyReqest );
		Document document = XMLUtil.parseXML( response );
		if( document != null ) {
			// TODO: Error handling
			NodeList userNodes = document.getElementsByTagName( "std_class" );
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
	 * 
	 * 
	 * @return
	 */
	private static StringBuilder getBaseURL( ) {
		WebConfig webConfig = WebConfig.getInstance( );
		return new StringBuilder( )
				.append( webConfig.getProtocol( ).getValue( ) )
				.append( "://" )
				.append( webConfig.getHost( ) )
				.append( webConfig.getPath( ) );
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private static String getUserVerifyURL( ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( USER_VERIFY_PATH )
				.append( URL_SUFFIX	)
				.toString( );
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private static Map<String, String> getUserVerifyRequest( ) {
		UserConfig userConfig = UserConfig.getInstance( );
		Map<String, String> postData = new HashMap<String, String>( );
		String data = new StringBuilder( 256 )
				.append( "<user>" )
				.append( "<username>" )
				.append( StringEscapeUtils.escapeXml( 
						userConfig.getUsername( ) ) )
				.append( "</username>" )
				.append( "<password>" )
				.append( StringEscapeUtils.escapeXml( 
						userConfig.getPassword( ) ) )
				.append( "</password>" )
				.append( "</user>" )
				.toString( );
		postData.put( XML_PARAM_NAME, data );
		
		return postData;
	}

	/**
	 * Returns the URL for uploading levels to the remote server.
	 * 
	 * @return the URL for uploading levels
	 */
	private static String getLevelUploadURL( ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( LEVEL_UPLOAD_PATH )
				.append( URL_SUFFIX	)
				.toString( );
	}
	
	/**
	 * 
	 * 
	 * @param userID
	 *            the current user's ID
	 * @param title
	 *            the name of the level
	 * @param encodedLevelData
	 *            the level as a base64-encoded string
	 * @return
	 */
	private static Map<String, String> getLevelUploadRequest( String userID,
			String title, String encodedLevelData ) {
		Map<String, String> postData = new HashMap<String, String>( );
		String data = new StringBuilder( 2048 )
				.append( "<blob>" )
				.append( "<client-key>" )
				.append( StringEscapeUtils.escapeXml( CLIENT_KEY ) )
				.append( "</client-key>" )
				.append( "<user-id>" )
				.append( StringEscapeUtils.escapeXml( userID ) )
				.append( "</user-id>" )
				.append( "<title>" )
				.append( StringEscapeUtils.escapeXml( title ) )
				.append( "</title>" )
				.append( "<data>" )
				.append( StringEscapeUtils.escapeXml( encodedLevelData ) )
				.append( "</data>" )
				.append( "</blob>" )
				.toString( );
		postData.put( XML_PARAM_NAME, data );
		
		return postData;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private static String getScoreSubmitURL( ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( SCORE_SUBMIT_PATH )
				.append( URL_SUFFIX	)
				.toString( );
	}
	
	/**
	 * 
	 * 
	 * @param userID
	 * @param levelID
	 * @param score
	 * @return
	 */
	private static Map<String, String> getScoreSubmitRequest( String userID,
			String levelID, long score ) {
		Map<String, String> postData = new HashMap<String, String>( );
		String data = new StringBuilder( 256 )
				.append( "<score>" )
				.append( "<client-key>" )
				.append( StringEscapeUtils.escapeXml( CLIENT_KEY ) )
				.append( "</client-key>" )
				.append( "<user-id>" )
				.append( StringEscapeUtils.escapeXml( userID ) )
				.append( "</user-id>" )
				.append( "<blob-id>" )
				.append( StringEscapeUtils.escapeXml( levelID ) )
				.append( "</blob-id>" )
				.append( "<score>" )
				.append( score )
				.append( "</score>" )
				.append( "</score>" )
				.toString( );
		postData.put( XML_PARAM_NAME, EncryptionUtil.encryptData( data ) );
		
		return postData;
	}
}