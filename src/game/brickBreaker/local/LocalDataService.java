package brickBreaker.local;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import brickBreaker.Level;

/**
 * This class provides methods for persisting and loading levels by using the
 * local filesystem.
 * 
 * @author Abraham Lin
 */
public class LocalDataService {
	private static final String LEVELS_DIR = "levels";

	/**
	 * Saves the specified level using the provided filename.
	 * 
	 * @param level
	 *            the level to save
	 * @param filename
	 *            the filename for the level file
	 * 
	 * @throws FilesystemFailureException 
	 *             if any errors occur while attempting the operation
	 */
	public static void saveLevel( Level level, String filename )
			throws FilesystemFailureException {
		try {
			File levelsDir = new File( Thread
					.currentThread( )
					.getContextClassLoader( )
					.getResource( LEVELS_DIR )
					.toURI( ) );
			ObjectOutputStream levelStream = new ObjectOutputStream(
					new FileOutputStream( new File( levelsDir, filename ) ) );
			levelStream.writeObject( level );
			levelStream.close( );
		} catch( FileNotFoundException e ) {
			throw new FilesystemFailureException(
					"Could not find levels directory",
					e );
		} catch( IOException e ) {
			throw new FilesystemFailureException(
					"Error in saving level to disk",
					e );
		} catch( URISyntaxException e ) {
			throw new FilesystemFailureException(
					"Could not resolve path to levels directory",
					e );
		}
	}

	/**
	 * Loads all existing levels from the filesystem.
	 * 
	 * @return a list of all loaded levels
	 * 
	 * @throws FilesystemFailureException
	 *             if any errors occur while attempting the operation
	 */
	public static BiMap<String, Level> loadAllLevelsFromDisk( )
			throws FilesystemFailureException {
		Map<String, Level> levels = new LinkedHashMap<String, Level>( );
		
		File levelsDir;
		try {
			levelsDir = new File( Thread
					.currentThread( )
					.getContextClassLoader( )
					.getResource( LEVELS_DIR )
					.toURI( ) );
			File[] levelFiles = levelsDir.listFiles( );
			if( levelFiles == null ) {
				// Found a file, not a directory
				throw new FilesystemFailureException(
						"Could not resolve path to levels directory" );
			}
			for( File levelFile : levelFiles ) {
				ObjectInputStream levelStream = new ObjectInputStream(
						new FileInputStream( levelFile ) );
				Level level = (Level)levelStream.readObject( );
				levelStream.close( );
				
				// Compute the ID for the level
				String levelID = calculateHash( level );
				
				levels.put( levelID, level );
			}
		} catch( URISyntaxException e ) {
			throw new FilesystemFailureException(
					"Could not resolve path to levels directory",
					e );
		} catch( FileNotFoundException e ) {
			throw new FilesystemFailureException(
					"Could not find levels directory",
					e );
		} catch( IOException e ) {
			throw new FilesystemFailureException(
					"Error in reading level from disk",
					e );
		} catch( ClassNotFoundException e ) {
			throw new FilesystemFailureException(
					"Error in reading level from disk",
					e );
		}
		
		return HashBiMap.create( levels );
	}

	/**
	 * Calculates the MD5 hash for the provided level.
	 * 
	 * @param level
	 *            the level
	 * @return the MD5 hash of the level
	 */
	public static String calculateHash( Level level ) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream( );
			ObjectOutputStream objStream = new ObjectOutputStream( byteStream );
			objStream.writeObject( level );
			objStream.close( );
			return DigestUtils.md5Hex( byteStream.toByteArray( ) );
		} catch( IOException e ) {
			throw new RuntimeException( "Error in reading level data", e );
		}
	}
}
