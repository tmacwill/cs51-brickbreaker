package brickBreaker;

import java.util.List;

import brickBreaker.local.LevelCatalog;
import brickBreaker.web.EncryptionUtil;
import brickBreaker.web.OnlineLevel;
import brickBreaker.web.UserConfig;
import brickBreaker.web.WebConfig;
import brickBreaker.web.WebService;

public class Test {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		LevelCatalog catalog = LevelCatalog.getInstance( );
//		Brick[][] bricks = new Brick[20][20];
//		Level level = new Level( bricks, 1, "a" );
//		catalog.addLevel( level );
		
		EncryptionUtil.init( );
		
		WebConfig webConfig = WebConfig.getInstance( );
		webConfig.setHost( "localhost" );
		webConfig.setPath( "/brickbreaker" );
		
		UserConfig userConfig = UserConfig.getInstance( );
		userConfig.setUsername( "test" );
		userConfig.setPassword( "test" );
		
		List<OnlineLevel> onlineLevels = WebService.getOnlineLevels( );
		for( OnlineLevel onlineLevel : onlineLevels ) {
//			System.out.println( onlineLevel.getLevelID( ) + ":" + onlineLevel.getTitle( ) );
			Level l = WebService.downloadLevel( onlineLevel.getLevelID( ) );
			LevelCatalog.getInstance( ).addLevel( l );
		}
		
		List<Level> levels = catalog.getLevels( );
		for( Level l : levels ) {
//			System.out.println( l );
//			System.out.println( catalog.getLevelID( l ) );
//			WebService.uploadLevel( l, "A Random Level" );
//			WebService.submitScore( l, 12345 );
		}
	}

}
