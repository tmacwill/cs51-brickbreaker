package brickBreaker.local;

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
	 */
	public static void saveLevel( Level level, String filename ) {
		try {
			ObjectOutputStream levelStream = new ObjectOutputStream(
					new FileOutputStream( LEVELS_DIR + "/" + filename ) );
			levelStream.writeObject( level );
			levelStream.close( );
		} catch( FileNotFoundException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		} catch( IOException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * Loads all existing levels from the filesystem.
	 * 
	 * @return a list of all loaded levels
	 */
	public static BiMap<String, Level> loadAllLevelsFromDisk( ) {
		Map<String, Level> levels = new LinkedHashMap<String, Level>( );
		
		File levelsDir;
		try {
			levelsDir = new File( Thread
					.currentThread( )
					.getContextClassLoader( )
					.getResource( LEVELS_DIR )
					.toURI( ) );
			// FIXME: Better error handling (e.g. make sure levelsDir is directory)
			File[] levelFiles = levelsDir.listFiles( );
			for( File levelFile : levelFiles ) {
				ObjectInputStream levelStream = new ObjectInputStream(
						new FileInputStream( levelFile ) );
				Level level = (Level)levelStream.readObject( );
				String levelID = DigestUtils.md5Hex( levelStream );
				levelStream.close( );
				
				levels.put( levelID, level );
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
		} catch( ClassNotFoundException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
		
		return HashBiMap.create( levels );
	}
}
