package brickBreaker;

import java.util.List;

import brickBreaker.local.FilesystemFailureException;
import brickBreaker.local.LevelCatalog;
import brickBreaker.local.LocalDataService;

public class Test {
	public static void main( String[] args ) throws FilesystemFailureException {
		LevelCatalog catalog = LevelCatalog.getInstance( );
		List<Level> levels = catalog.getLevels( );
		for( Level level : levels ) {
			System.out.println( catalog.getLevelID( level ) + " : " + level );
//			LocalDataService.saveLevel( level, LocalDataService.calculateHash( level ) );
		}
	}
}
