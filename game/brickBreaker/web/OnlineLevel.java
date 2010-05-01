package brickBreaker.web;

/**
 * 
 * 
 * @author Abraham Lin
 */
public class OnlineLevel {
	private final String levelID;
	private final String title;
	
	public OnlineLevel( String levelID, String title ) {
		this.levelID = levelID;
		this.title = title;
	}

	public String getLevelID( ) {
		return levelID;
	}

	public String getTitle( ) {
		return title;
	}
}
