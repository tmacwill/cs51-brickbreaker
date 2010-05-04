package brickBreaker.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private static final String LEVEL_BROWSE_PATH = "/blobs/results";
	private static final String LEVEL_DOWNLOAD_PATH = "/blobs/download";
	private static final String LEVEL_UPLOAD_PATH = "/blobs/add";
	private static final String SCORE_RETRIEVE_PATH = "/scores/view";
	private static final String SCORE_SUBMIT_PATH = "/scores/add";

	/**
	 * Returns whether the given login credentials are valid.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return whether the given username/password pair is valid
	 * 
	 * @throws WebException
	 *             if any errors occur while attempting the operation
	 */
	public static boolean verifyUser( String username, String password )
			throws WebException {
		boolean isValidUser = false;

		String userVerifyURL = getUserVerifyURL( );
		Map<String, String> userVerifyReqest = getUserVerifyRequest(
				username,
				password );

		String response;
		try {
			response = new String( ConnectionUtil.doPost(
					userVerifyURL,
					userVerifyReqest ) );
		} catch( RequestFailureException e ) {
			throw new WebException(
					"Could not successfully contact remote server",
					e );
		}
		Document document;
		try {
			document = XMLUtil.parseXML( response );
		} catch( XMLParseFailureException e ) {
			throw new WebException( "Could not parse the response", e );
		}
		if( document != null ) {
			NodeList userNodes = document.getElementsByTagName( "std_class" );
			isValidUser = ( userNodes.getLength( ) > 0 );
		}

		return isValidUser;
	}

	/**
	 * Returns a list of all levels available on the remote server.
	 * 
	 * @return the list of available levels
	 * 
	 * @throws WebException
	 *             if any errors occur while attempting the operation
	 */
	public static List<OnlineLevel> getOnlineLevels( ) throws WebException {
		List<OnlineLevel> onlineLevels = new ArrayList<OnlineLevel>( );

		String levelBrowseURL = getLevelBrowseURL( );
		String response;
		try {
			response = new String( ConnectionUtil.doGet( levelBrowseURL ) );
		} catch( RequestFailureException e ) {
			throw new WebException(
					"Could not successfully contact remote server",
					e );
		}
		Document document;
		try {
			document = XMLUtil.parseXML( response );
		} catch( XMLParseFailureException e ) {
			throw new WebException( "Could not parse the response", e );
		}
		if( document != null ) {
			NodeList levelNodes = document.getElementsByTagName( "blob" );
			int numLevels = levelNodes.getLength( );
			for( int i = 0; i < numLevels; i++ ) {
				Element level = (Element)levelNodes.item( i );
				String id = level.getAttribute( "id" );
				String title = level.getAttribute( "title" );
				onlineLevels.add( new OnlineLevel( id, title ) );
			}
		}

		return onlineLevels;
	}

	/**
	 * Downloads the level with the specified unique identifier.
	 * 
	 * @param levelID
	 *            the unique identifier
	 * @return the downloaded level
	 * 
	 * @throws WebException
	 *             if any errors occur while attempting the operation
	 */
	public static Level downloadLevel( String levelID ) throws WebException {
		Level level = null;

		String levelDownloadURL = getLevelDownloadURL( levelID );
		byte[] response;
		try {
			response = ConnectionUtil.doGet( levelDownloadURL );
		} catch( RequestFailureException e ) {
			throw new WebException(
					"Could not successfully contact remote server",
					e );
		}

		ByteArrayInputStream byteStream = new ByteArrayInputStream( response );
		try {
			ObjectInputStream objStream = new ObjectInputStream( byteStream );
			level = (Level)objStream.readObject( );
			objStream.close( );
		} catch( IOException e ) {
			throw new WebException( "Could not read level data", e );
		} catch( ClassNotFoundException e ) {
			throw new WebException( "Could not read level data", e );
		}

		return level;
	}

	/**
	 * Uploads a level to a remote server.
	 * 
	 * @param level
	 *            the level to upload
	 * @param title
	 *            the name for the level
	 * 
	 * @throws WebException
	 *             if any errors occur while attempting the operation
	 */
	public static void uploadLevel( Level level, String title )
			throws WebException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream( );
		try {
			ObjectOutputStream objStream = new ObjectOutputStream( byteStream );
			objStream.writeObject( level );
			objStream.close( );
		} catch( IOException e ) {
			throw new WebException( "Could not read level data", e );
		}

		byte[] levelData = byteStream.toByteArray( );
		String encodedLevelData = Base64.encodeBase64String( levelData );

		String userID;
		try {
			userID = getUserID( );
		} catch( InvalidUserException e ) {
			throw new WebException( e );
		}

		String levelUploadURL = getLevelUploadURL( );
		Map<String, String> levelUploadRequest = getLevelUploadRequest(
				userID,
				title,
				encodedLevelData );

		try {
			ConnectionUtil.doPost( levelUploadURL, levelUploadRequest );
		} catch( RequestFailureException e ) {
			throw new WebException(
					"Could not successfully contact remote server",
					e );
		}
	}

	/**
	 * Retrieves high scores for the specified level from the remote server.
	 * 
	 * @param level
	 *            the level
	 * @return a list of high scores for that level retrieved from the remote
	 *         server
	 * 
	 * @throws WebException
	 *             if any errors occur while attempting the operation
	 */
	public static List<HighScore> retrieveHighScores( Level level )
			throws WebException {
		List<HighScore> highScores = new ArrayList<HighScore>( );

		String levelID = LevelCatalog.getInstance( ).getLevelID( level );

		String scoreRetrieveURL = getScoreRetrieveURL( levelID );
		String response;
		try {
			response = new String( ConnectionUtil.doGet( scoreRetrieveURL ) );
		} catch( RequestFailureException e ) {
			throw new WebException(
					"Could not successfully contact remote server",
					e );
		}
		Document document;
		try {
			document = XMLUtil.parseXML( response );
		} catch( XMLParseFailureException e ) {
			throw new WebException( "Could not parse the response", e );
		}
		if( document != null ) {
			NodeList scoreNodes = document.getElementsByTagName( "score" );
			int numLevels = scoreNodes.getLength( );
			for( int i = 0; i < numLevels; i++ ) {
				Element score = (Element)scoreNodes.item( i );
				String name = ((Element)score.getFirstChild( ))
						.getAttribute( "username" );
				long highScore = Long.parseLong( 
						score.getAttribute( "score" ) );
				highScores.add( new HighScore( name, highScore ) );
			}
		}

		return highScores;
	}

	/**
	 * Submits a new high score to the remote server.
	 * 
	 * @param level
	 *            the level with which the score is associated
	 * @param score
	 *            the score
	 * 
	 * @throws WebException
	 *             if any errors occur while attempting the operation
	 */
	public static void submitScore( Level level, long score )
			throws WebException {
		String levelID = LevelCatalog.getInstance( ).getLevelID( level );

		String userID;
		try {
			userID = getUserID( );
		} catch( InvalidUserException e ) {
			throw new WebException( e );
		}

		// Make sure level is found online before submitting score
		List<OnlineLevel> onlineLevels = getOnlineLevels( );
		boolean found = false;
		int numOnlineLevels = onlineLevels.size( );
		for( int i = 0; !found && i < numOnlineLevels; i++ ) {
			OnlineLevel onlineLevel = onlineLevels.get( i );
			found = ( onlineLevel.getLevelID( ).equals( levelID ) );
		}
		if( !found ) {
			throw new WebException( "Level has not been uploaded yet" );
		}

		String scoreSubmitURL = getScoreSubmitURL( );
		Map<String, String> scoreSubmitRequest;
		try {
			Key publicKey = EncryptionUtil.getPublicKey( );
			Key symmetricKey = EncryptionUtil.generateSymmetricKey( );
			scoreSubmitRequest = getScoreSubmitRequest(
					userID,
					levelID,
					score,
					publicKey,
					symmetricKey );
		} catch( EncryptionFailureException e ) {
			throw new WebException( "Failed to properly encrypt data", e );
		}

		try {
			ConnectionUtil.doPost( scoreSubmitURL, scoreSubmitRequest );
		} catch( RequestFailureException e ) {
			throw new WebException(
					"Could not successfully contact remote server",
					e );
		}
	}

	/**
	 * Obtains the current user's ID from the remote server.
	 * 
	 * @return the current user's ID if the supplied credentials are valid;
	 *         otherwise, returns <code>null</code>
	 * 
	 * @throws InvalidUserException
	 *             if the user's identity cannot be determined
	 * @throws WebException
	 *             if any errors occur while attempting the operation
	 */
	private static String getUserID( ) throws InvalidUserException,
			WebException {
		String userID;

		String userVerifyURL = getUserVerifyURL( );
		UserConfig userConfig = UserConfig.getInstance( );
		Map<String, String> userVerifyReqest = getUserVerifyRequest( userConfig
				.getUsername( ), userConfig.getPassword( ) );

		String response;
		try {
			response = new String( ConnectionUtil.doPost(
					userVerifyURL,
					userVerifyReqest ) );
		} catch( RequestFailureException e ) {
			throw new WebException(
					"Could not successfully contact remote server",
					e );
		}
		Document document;
		try {
			document = XMLUtil.parseXML( response );
		} catch( XMLParseFailureException e ) {
			throw new WebException( "Could not parse the response", e );
		}
		NodeList userNodes = document.getElementsByTagName( "std_class" );
		if( userNodes.getLength( ) > 0 ) {
			Node userNode = userNodes.item( 0 );
			userID = ((Element)userNode).getAttribute( "id" );
		} else {
			throw new InvalidUserException( "Could not log in successfully" );
		}

		return userID;
	}

	/**
	 * Returns the base URL to the remote server.
	 * 
	 * @return the base URL
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
	 * Returns the full URL for verifying user identities.
	 * 
	 * @return the URL
	 */
	private static String getUserVerifyURL( ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( USER_VERIFY_PATH )
				.append( URL_SUFFIX )
				.toString( );
	}

	/**
	 * Returns the parameters used in requests for verifying user identities.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * 
	 * @return the parameters
	 */
	private static Map<String, String> getUserVerifyRequest( String username,
			String password ) {
		Map<String, String> postData = new HashMap<String, String>( );
		String data = new StringBuilder( 256 )
				.append( "<user>" )
				.append( "<username>" )
				.append( StringEscapeUtils.escapeXml( username ) )
				.append( "</username>" )
				.append( "<password>" )
				.append( StringEscapeUtils.escapeXml( password ) )
				.append( "</password>" )
				.append( "</user>" )
				.toString( );
		postData.put( XML_PARAM_NAME, data );

		return postData;
	}

	/**
	 * Returns the full URL for browsing levels.
	 * 
	 * @return the URL
	 */
	private static String getLevelBrowseURL( ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( LEVEL_BROWSE_PATH )
				.append( URL_SUFFIX )
				.toString( );
	}

	/**
	 * Returns the full URL for downloading the level with the specified unique
	 * identifier.
	 * 
	 * @param levelID
	 *            the unique identifier
	 * @return the URL
	 */
	private static String getLevelDownloadURL( String levelID ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( LEVEL_DOWNLOAD_PATH )
				.append( "/" )
				.append( ConnectionUtil.encodeURLComponent( levelID ) )
				.toString( );
	}

	/**
	 * Returns the full URL for uploading levels.
	 * 
	 * @return the URL
	 */
	private static String getLevelUploadURL( ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( LEVEL_UPLOAD_PATH )
				.append( URL_SUFFIX )
				.toString( );
	}

	/**
	 * Returns the parameters used in requests for uploading levels.
	 * 
	 * @param userID
	 *            the current user's ID
	 * @param title
	 *            the name of the level
	 * @param encodedLevelData
	 *            the level as a base64-encoded string
	 * @return the parameters
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
	 * Returns the full URL for retrieving high scores for the level with the
	 * specified unique identifier.
	 * 
	 * @param levelID
	 *            the unique identifier
	 * @return the URL
	 */
	private static String getScoreRetrieveURL( String levelID ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( SCORE_RETRIEVE_PATH )
				.append( "/" ).append( 
						ConnectionUtil.encodeURLComponent( levelID ) )
				.append( URL_SUFFIX )
				.toString( );
	}

	/**
	 * Returns the full URL for submitting high scores.
	 * 
	 * @return the URL
	 */
	private static String getScoreSubmitURL( ) {
		return new StringBuilder( )
				.append( getBaseURL( ) )
				.append( SCORE_SUBMIT_PATH )
				.append( URL_SUFFIX )
				.toString( );
	}

	/**
	 * Returns the parameters used in requests for submitting high scores.
	 * 
	 * @param userID
	 *            the current user's ID
	 * @param levelID
	 *            the unique identifier for the level associated with the high
	 *            score
	 * @param score
	 *            the score
	 * @param publicKey
	 *            the public key to be used for encrypting the symmetric key
	 * @param symmetricKey
	 *            the symmetric key to be used for encrypting the score data
	 * @return the parameters
	 * 
	 * @throws EncryptionFailureException
	 *             if any errors occur while attempting the operation
	 */
	private static Map<String, String> getScoreSubmitRequest( String userID,
			String levelID, long score, Key publicKey, Key symmetricKey )
			throws EncryptionFailureException {
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

		postData.put( "key", EncryptionUtil.encryptData(
				publicKey,
				symmetricKey.getEncoded( ) ) );
		postData.put( XML_PARAM_NAME, EncryptionUtil.encryptData(
				symmetricKey,
				data.getBytes( ) ) );

		return postData;
	}
}
