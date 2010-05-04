package brickBreaker.web;

/**
 * This class provides a representation of a high score with a name and score.
 * 
 * @author Abraham Lin
 */
public class HighScore {
	private final String name;
	private final long score;

	/**
	 * Constructs a new high score with the specified name and score.
	 * 
	 * @param name
	 *            the name
	 * @param score
	 *            the score
	 */
	public HighScore( String name, long score ) {
		this.name = name;
		this.score = score;
	}
	
	/**
	 * Returns the name associated with this high score.
	 * 
	 * @return the associated name
	 */
	public String getName( ) {
		return name;
	}
	
	/**
	 * Returns the score associated with this high score.
	 * 
	 * @return the associated score
	 */
	public long getScore( ) {
		return score;
	}
}
