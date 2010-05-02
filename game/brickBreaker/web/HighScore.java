package brickBreaker.web;

public class HighScore {
	private final String name;
	private final int score;
	
	public HighScore( String name, int score ) {
		this.name = name;
		this.score = score;
	}

	public String getName( ) {
		return name;
	}

	public int getScore( ) {
		return score;
	}
}
