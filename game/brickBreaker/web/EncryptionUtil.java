package brickBreaker.web;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

/**
 * This class provides methods for performing public-key encryption.
 * 
 * @author Abraham Lin
 */
public class EncryptionUtil {
	private static final IvParameterSpec INIT_VECTOR = new IvParameterSpec(
			"fedcba9876543210".getBytes( ) );
	private static final String ASYMMETRIC_ALGORITHM = "RSA";
	private static final String SYMMETRIC_ALGORITHM = "AES";
	private static final int SYMMETRIC_KEY_SIZE = 128;
	
	private static final Map<String, String> ALGORITHMS =
			new HashMap<String, String>( );
	static {
		ALGORITHMS.put( ASYMMETRIC_ALGORITHM, ASYMMETRIC_ALGORITHM );
		ALGORITHMS.put( SYMMETRIC_ALGORITHM, "AES/CBC/NoPadding" );
	}
	
	private static final Map<String, Key> PUBLIC_KEYS = 
			new HashMap<String, Key>( );
	
	/**
	 * Loads all available public keys into memory.
	 * 
	 * @return the number of keys that were successfully loaded
	 */
	public static int init( ) {
		loadPublicKeys( );
		
		return PUBLIC_KEYS.size( );
	}

	/**
	 * Encrypts the supplied data using public-key encryption.
	 * @param key TODO
	 * @param data
	 *            the data to encrypt
	 * 
	 * @return the encrypted data in base64-encoded form
	 */
	public static String encryptData( Key key, byte[] data ) {
		try {
			String keyAlgorithm = key.getAlgorithm( );
			String algorithm = ALGORITHMS.get( keyAlgorithm );
			Cipher cipher = Cipher.getInstance( algorithm );
			
			if( SYMMETRIC_ALGORITHM.equals( keyAlgorithm ) ) {
				cipher.init( Cipher.ENCRYPT_MODE, key, INIT_VECTOR );
				if( data.length % 16 != 0 ) {
					int newLength = 16 * ( data.length / 16 + 1 );
					byte[] newData = new byte[newLength];
					System.arraycopy( data, 0, newData, 0, data.length );
					data = newData;
				}
			} else {
				cipher.init( Cipher.ENCRYPT_MODE, key );
			}
			byte[] encryptedData = cipher.doFinal( data );
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
		} catch( InvalidAlgorithmParameterException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * Loads all available public keys.
	 */
	private static void loadPublicKeys( ) {
		try {
			KeyFactory factory = KeyFactory.getInstance( ASYMMETRIC_ALGORITHM );
			
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
				Key key = factory.generatePublic( keySpec );
				
				PUBLIC_KEYS.put( keyName, key );
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
	public static Key getPublicKey( ) {
		String keyName = canonicalizeHostName( WebConfig
				.getInstance( )
				.getHost( ) );
		
		return PUBLIC_KEYS.get( keyName );
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public static Key getSymmetricKey( ) {
		KeyGenerator keygen;
		try {
			keygen = KeyGenerator.getInstance( SYMMETRIC_ALGORITHM );
			keygen.init( SYMMETRIC_KEY_SIZE );
		} catch( NoSuchAlgorithmException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
		return keygen.generateKey( );
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
