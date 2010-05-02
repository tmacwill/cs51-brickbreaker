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

import brickBreaker.local.FilesystemFailureException;

/**
 * This class provides methods for performing encryption.
 * 
 * @author Abraham Lin
 */
public class EncryptionUtil {
	// Encryption parameters
	private static final String ASYMMETRIC_KEYGEN_ALGORITHM = "RSA";
	private static final String ASYMMETRIC_ENCRYPT_ALGORITHM = "RSA";
	private static final String SYMMETRIC_KEYGEN_ALGORITHM = "AES";
	private static final String SYMMETRIC_ENCRYPT_ALGORITHM = 
			"AES/CBC/NoPadding";
	private static final int SYMMETRIC_KEY_SIZE = 128;
	private static final IvParameterSpec INIT_VECTOR = new IvParameterSpec(
			"fedcba9876543210".getBytes( ) );	// Only used with symmetric key
	
	// The algorithm names for key generation are different from those for
	// encryption
	private static final Map<String, String> ALGORITHMS =
			new HashMap<String, String>( );
	static {
		ALGORITHMS.put(
				ASYMMETRIC_KEYGEN_ALGORITHM,
				ASYMMETRIC_ENCRYPT_ALGORITHM );
		ALGORITHMS
				.put( SYMMETRIC_KEYGEN_ALGORITHM, SYMMETRIC_ENCRYPT_ALGORITHM );
	}
	
	// All available public keys
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
	 * Encrypts the supplied data using the supplied key.
	 * 
	 * @param key
	 *            the encryption key
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
			
			if( SYMMETRIC_KEYGEN_ALGORITHM.equals( keyAlgorithm ) ) {
				// Symmetric key encryption requires initialization vector and
				// data padded to a multiple of 16 bytes
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
			throw new EncryptionFailureException(
					"An invalid key was supplied",
					e );
		} catch( NoSuchAlgorithmException e ) {
			throw new EncryptionFailureException(
					"An invalid algorithm was specified",
					e );
		} catch( NoSuchPaddingException e ) {
			throw new EncryptionFailureException(
					"Invalid data was supplied",
					e );
		} catch( IllegalBlockSizeException e ) {
			throw new EncryptionFailureException(
					"Invalid data was supplied",
					e );
		} catch( BadPaddingException e ) {
			throw new EncryptionFailureException(
					"Invalid data was supplied",
					e );
		} catch( InvalidAlgorithmParameterException e ) {
			throw new EncryptionFailureException(
					"An invalid algorithm parameter was specified",
					e );
		}
	}
	
	/**
	 * Loads all available public keys from disk.
	 */
	private static void loadPublicKeys( ) {
		try {
			KeyFactory factory = KeyFactory
					.getInstance( ASYMMETRIC_KEYGEN_ALGORITHM );
			
			File keysDir = new File( Thread
					.currentThread( )
					.getContextClassLoader( )
					.getResource( "keys" )
					.toURI( ) );
			File[] keyFiles = keysDir.listFiles( );
			if( keyFiles == null ) {
				// Found a file, not a directory
				throw new FilesystemFailureException(
						"Could not resolve path to keys directory" );
			}
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
			throw new EncryptionFailureException(
					"Could not resolve path to keys directory",
					e );
		} catch( FileNotFoundException e ) {
			throw new EncryptionFailureException(
					"Could not find keys directory",
					e );
		} catch( IOException e ) {
			throw new EncryptionFailureException(
					"Error in reading keys from disk",
					e );
		} catch( NoSuchAlgorithmException e ) {
			throw new EncryptionFailureException(
					"An invalid algorithm was specified",
					e );
		} catch( InvalidKeySpecException e ) {
			throw new EncryptionFailureException(
					"An invalid key specification was specified",
					e );
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
	 * Generates a new symmetric key.
	 * 
	 * @return the new key
	 */
	public static Key generateSymmetricKey( ) {
		KeyGenerator keygen;
		try {
			keygen = KeyGenerator.getInstance( SYMMETRIC_KEYGEN_ALGORITHM );
			keygen.init( SYMMETRIC_KEY_SIZE );
		} catch( NoSuchAlgorithmException e ) {
			throw new EncryptionFailureException(
					"An invalid algorithm was specified",
					e );
		}
		return keygen.generateKey( );
	}

	/**
	 * Formats the supplied host to conform with the naming conventions of
	 * public key filenames.
	 * 
	 * @param host
	 *            the host to canonicalize
	 * @return the canonicalized hostname
	 */
	private static String canonicalizeHostName( String host ) {
		return host.replace( '.', '_' );
	}
}
