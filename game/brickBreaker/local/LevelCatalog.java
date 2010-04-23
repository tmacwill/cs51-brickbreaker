package brickBreaker.local;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.common.collect.BiMap;

import brickBreaker.Level;

public class LevelCatalog {
	private static final LevelCatalog INSTANCE = new LevelCatalog( );
	
	private BiMap<String, Level> levelData;
	
	private LevelCatalog( ) {
		levelData = LocalDataService.loadAllLevelsFromDisk( );
	}
	
	public static LevelCatalog getInstance( ) {
		return INSTANCE;
	}
	
	public List<Level> getLevels( ) {
		return new ArrayList<Level>( levelData.values( ) );
	}
	
	public void addLevel( Level level ) {
		// Compute the ID for the level
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream( );
		try {
			ObjectOutputStream objStream = new ObjectOutputStream( byteStream );
			objStream.writeObject( level );
			objStream.close( );
		} catch( IOException e ) {
			// FIXME: Rethrow better exception
			throw new RuntimeException( e );
		}
		byte[] levelBytes = byteStream.toByteArray( );
		String levelID = DigestUtils.md5Hex( levelBytes );
		
		if( levelData.get( levelID ) != null ) {
			// FIXME: Throw better exception
			throw new RuntimeException( "Conflicting level" );
		}
		
		// Save the level
		LocalDataService.saveLevel( level, levelID );
		
		// Add the level to the catalog
		levelData.put( levelID, level );
	}
	
	public String getLevelID( Level level ) {
		return levelData.inverse( ).get( level );
	}
}
