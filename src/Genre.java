import java.util.HashMap;
import java.util.Map;

/**
 * @author Asim
 *	Class that implements the details about a Genre Object
 */

public class Genre implements Comparable<Genre>{
	
	private String myGenre;								// Name of the Genre
	private Map<Keyword, Integer> myKeywordsScore;		// Keyword -> Score
	
	/**
	 * Constructor
	 * @param genre 		- Genre name as a string
	 */
	
	public Genre(String genre) {
		this.myGenre = genre;
		this.myKeywordsScore = new HashMap<Keyword, Integer>();
	}
	
	/**
	 * Method to check whether the genre contains a keyword
	 * @param k			- keyword object
	 * @return boolean 	- true or false
	 */
	
	public boolean containsKeyword(Keyword k) {
		return myKeywordsScore.containsKey(k);
	}
	
	/**
	 * Method that returns the score for the keyword in this genre
	 * @param k			- Keyword object
	 * @return			- Integer score
	 */
	
	public int getKeywordScore(Keyword k) {
		if(this.myKeywordsScore.containsKey(k)){
			return this.myKeywordsScore.get(k);
		} else {
			return -1;
		}
	}
	
	/**
	 * Method called from CSV reader that maps the keyword in a Genre to its Score
	 * @param k				- Keyword Object
	 * @param score			- int Score
	 */
	// method that is called from CSV reader that maps the keyword in a genre to its score
	public void putKeywordScore(Keyword k, int score) {
		if(this.myKeywordsScore.containsKey(k)) {
			return;
		} else {
			this.myKeywordsScore.put(k, score);
		}
	}
	
	/**
	 * Getters and Setters and Overridden methods
	 * 
	 */
	
	public String getName() {
		return this.myGenre;
	}

	public Map<Keyword, Integer> getMyKeywordsScore() {
		return myKeywordsScore;
	}

	
	public String getGenreName() {
		return this.myGenre;
	}
	
	@Override 
	public int compareTo(Genre other) {
		return this.myGenre.compareTo(other.myGenre);
	}
	
	@Override
	public int hashCode() {
		return this.myGenre.hashCode();
	}
	@Override 
	public boolean equals(Object o) {
		Genre other = (Genre) o;
		if( ! (o instanceof Genre) ) {
			return false;
		} else {
			return this.myGenre.equals(other.myGenre);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[Genre: " + this.myGenre);
		sb.append("\nKeywords: ");
		for(Keyword k : this.myKeywordsScore.keySet()) {
			sb.append("[" + k + " " + myKeywordsScore.get(k) + "] ");
		}
		return sb.toString();
	}
}
