package brickBreaker.web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * This class provides methods for performing public-key encryption.
 * 
 * @author Abraham Lin
 */
public class EncryptionUtil {
	private static final String ALGORITHM = "RSA";
	private static final Map<String, PublicKey> KEYS = 
			new HashMap<String, PublicKey>( );
	
	/**
	 * Loads all available public keys into memory.
	 * 
	 * @return the number of keys that were successfully loaded
	 */
	public static int init( ) {
		loadPublicKeys( );
		
		return KEYS.size( );
	}

	/**
	 * Encrypts the supplied data using public-key encryption.
	 * 
	 * @param input
	 *            the data to encrypt
	 * @return the encrypted data in base64-encoded form
	 */
	public static String encryptData( String input ) {
		PublicKey key = getPublicKey( );
		
		try {
			Cipher cipher = Cipher.getInstance( ALGORITHM );
			cipher.init( Cipher.ENCRYPT_MODE, key );
			byte[] encryptedData = cipher.doFinal( input.getBytes( ) );
			return Base64.encodeBase64String( encryptedData );
		} catch( InvalidKeyException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( NoSuchAlgorithmException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( NoSuchPaddingException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( IllegalBlockSizeException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( BadPaddingException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * Loads all available public keys.
	 */
	private static void loadPublicKeys( ) {
		try {
			KeyFactory factory = KeyFactory.getInstance( ALGORITHM );
			
			File keysDir = new File( Thread
					.currentThread( )
					.getContextClassLoader( )
					.getResource( "keys" )
					.toURI( ) );
			// FIXME: Better error handling (e.g. make sure keysDir is directory)
			File[] keyFiles = keysDir.listFiles( );
			for( File keyFile : keyFiles ) {
				String keyName = keyFile.getName( ).replace( ".der", "" );
				DataInputStream keyByteStream = new DataInputStream(
						new FileInputStream( keyFile ) );
				byte[] keyBytes = new byte[(int)keyFile.length( )];
				keyByteStream.readFully( keyBytes );
				keyByteStream.close( );
				
				X509EncodedKeySpec keySpec = new X509EncodedKeySpec( keyBytes );
				PublicKey key = factory.generatePublic( keySpec );
				
				KEYS.put( keyName, key );
			}
		} catch( URISyntaxException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( FileNotFoundException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( IOException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( NoSuchAlgorithmException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( InvalidKeySpecException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * Returns the public key for the current remote server.
	 * 
	 * @return the public key for the current remote server
	 */
	private static PublicKey getPublicKey( ) {
		String keyName = canonicalizeHostName( WebConfig
				.getInstance( )
				.getHost( ) );
		
		return KEYS.get( keyName );
	}

	/**
	 * Formats the supplied host to conform with the naming conventions of
	 * public key files.
	 * 
	 * @param host
	 *            the host to canonicalize
	 * @return the canonicalized hostname
	 */
	private static String canonicalizeHostName( String host ) {
		return host.replace( '.', '_' );
	}
}
