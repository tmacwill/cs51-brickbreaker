package brickBreaker.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.BiMap;

import brickBreaker.Level;

/**
 * This singleton class provides an in-memory catalog of levels available for
 * use by the application.
 * 
 * @author Abraham Lin
 */
public class LevelCatalog {
	private static final LevelCatalog INSTANCE = new LevelCatalog( );

	private BiMap<String, Level> levelData;

	/**
	 * Constructs a new catalog containing all levels present on the local
	 * system.
	 */
	private LevelCatalog( ) {
		reset( );
	}
	
	/**
	 * Returns a singleton instance of this class.
	 * 
	 * @return the instance
	 */
	public static LevelCatalog getInstance( ) {
		return INSTANCE;
	}
	
	/**
	 * Resets the catalog by re-loading all levels from disk.
	 */
	public void reset( ) {
		levelData = LocalDataService.loadAllLevelsFromDisk( );
	}
	
	/**
	 * Returns an immutable list of all levels in the catalog.
	 * 
	 * @return the immutable list of available levels
	 */
	public List<Level> getLevels( ) {
		return Collections.unmodifiableList( new ArrayList<Level>( levelData
				.values( ) ) );
	}

	/**
	 * Adds a new level to the catalog, saving it to disk if necessary.
	 * 
	 * @param level
	 *            the level to add
	 */
	public void addLevel( Level level ) {
		// Compute the ID for the level
		String levelID = LocalDataService.calculateHash( level );

		if( levelData.get( levelID ) != null ) {
			throw new RuntimeException(
					"Supplied level already exists in the catalog" );
		}

		// Save the level
		LocalDataService.saveLevel( level, levelID );

		// Add the level to the catalog
		levelData.put( levelID, level );
	}

	/**
	 * Returns a unique identifier for the provided level.
	 * 
	 * @param level
	 *            the level
	 * @return the unique identifier for the level
	 */
	public String getLevelID( Level level ) {
		return levelData.inverse( ).get( level );
	}
}
