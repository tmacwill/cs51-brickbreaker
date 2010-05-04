package brickBreaker.web;

/**
 * This class provides a representation of a remotely available level with a
 * unique identifier and title.
 * 
 * @author Abraham Lin
 */
public class OnlineLevel {
	private final String levelID;
	private final String title;

	/**
	 * Constructs a level representation with the given unique identifier and
	 * title.
	 * 
	 * @param levelID
	 *            the unique identifier
	 * @param title
	 *            the title
	 */
	public OnlineLevel( String levelID, String title ) {
		this.levelID = levelID;
		this.title = title;
	}

	/**
	 * Returns the unique identifier of this level representation.
	 * 
	 * @return the unique identifier
	 */
	public String getLevelID( ) {
		return levelID;
	}

	/**
	 * Returns the title associated with this level representation.
	 * 
	 * @return the title
	 */
	public String getTitle( ) {
		return title;
	}
}
