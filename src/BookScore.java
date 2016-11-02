import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class describes the implementation details for a BookScore Object
 * A BookScore object contains the necessary information required to 
 * calculate the scores for each genre.
 * @author asim
 *
 */
public class BookScore implements Comparable<BookScore>{

	private String myBookTitle;									// bookTitle
	private Map<Genre, List<Keyword>> myGenreToUniqueKeywords;	// Genre : [Keyword, ... , Keyword]
	private Map<Genre, Integer> myGenreToKeywordCount;			// Genre : Count of Total Keyword Matches

	/**
	 *  Constructor for BookScore
	 *  @param: title
	 */

	public BookScore(String title) {
		this.myBookTitle = title;
		// insert genre according to its lexicographical order by writing a comparator for the Map
		this.myGenreToUniqueKeywords = new TreeMap<Genre, List<Keyword>>(new Comparator<Genre>() {
			@Override 
			public int compare(Genre g1, Genre g2) {
				return g1.compareTo(g2);
			}
		});
		this.myGenreToKeywordCount = new HashMap<Genre, Integer>();
	}

	/**
	 * Method only adds the unique keyword to the map above and 
	 * @param: Keyword k
	 */

	public void addKeywordToBookScore(Keyword keyword) {

		// Get Genres that have the keyword
		List<Genre> genresWithKeyword = ScoreCalculator.myKeywordToGenreMap.get(keyword);
		//System.out.println();
		//System.out.println("ADDING KEYWORD: " + keyword + " to book: " + this.myBookTitle);
		//System.out.println();
		// Loop through the list of Genres that have this keyword
		for(Genre genre : genresWithKeyword) {
			
			// Check if the Genre has been added to our BookScore object already
			if(this.myGenreToUniqueKeywords.containsKey(genre)){
				
				// if the Genre has already been added, 
				// check if this specific keyword has been already found or not.
				if(this.myGenreToUniqueKeywords.get(genre).contains(keyword)) {

					// if it contains the keyword, then increase the total keyword count for this genre
					int newCount = myGenreToKeywordCount.get(genre);
					newCount += 1;
					myGenreToKeywordCount.put(genre, newCount);
				
				} else {
				
					// put the new keyword for this Genre in the unique genre map
					List<Keyword> list = myGenreToUniqueKeywords.get(genre);
					list.add(keyword);
					myGenreToUniqueKeywords.put(genre, list);
					// update the count for this genre
					int newCount = myGenreToKeywordCount.get(genre);
					newCount += 1;
					myGenreToKeywordCount.put(genre, newCount);
				}
				
			} else {
				
				// if the genre doesn't exist, add it to the genreMap
				this.myGenreToUniqueKeywords.put(genre, new ArrayList<Keyword>());
				List<Keyword> list = this.myGenreToUniqueKeywords.get(genre);
				list.add(keyword);
				// add it to the count map and set keyword count to 1
				this.myGenreToKeywordCount.put(genre, 1);
				
			}
		}
	}
	
	/**
	 * Method that generates and calculates the Genre scores 
	 * for this BookScore object based on the total and unique
	 * keywords found based on this formula
	 * (total num keyword matches * avg point value of the unique matching keywords)
	 * @return Aggregated Genre scores as a String
	 */
	
	public String generateGenreScores() {
		
		StringBuilder sb = new StringBuilder();
		
		// Loop through each genre
		for(Genre genre : this.myGenreToUniqueKeywords.keySet()){
			
			List<Keyword> uniqueKeywords = this.myGenreToUniqueKeywords.get(genre);
			int uniqueSum = 0;
			// get keyword count 
			for(Keyword keyword : uniqueKeywords) {
				uniqueSum += genre.getKeywordScore(keyword);
			}
			//System.out.println("uniqueSum: " + uniqueSum);
			int uniqueAvg = uniqueSum/uniqueKeywords.size();
			//System.out.println("uniqueAvg: " + uniqueAvg);
			int totalGenreKeywordMatches = this.myGenreToKeywordCount.get(genre);
			//System.out.println("totalGenreMatches: " + totalGenreKeywordMatches);
			int result = (totalGenreKeywordMatches * uniqueAvg);
			sb.append(genre.getGenreName() + " " + result + "\n");
		}

		return sb.toString();
	}
	
	
	/**
	 * Method that prints the scores for the current book
	 */
	public void printBookScore() {
		System.out.println(this.myBookTitle);
		for(Genre g : this.myGenreToUniqueKeywords.keySet()){
			System.out.println(g.getName() + ", " + this.myGenreToUniqueKeywords.get(g));
		}
	}
	
	/**
	 * Overridden methods and getters and setters
	 */
	
	@Override 
	public String toString() {
		return this.myBookTitle;
	}

	public Map<Genre, List<Keyword>> getMyGenreToKeywordMap() {
		return this.myGenreToUniqueKeywords;
	}

	@Override
	public int compareTo(BookScore o) {
		return this.myBookTitle.compareTo(o.myBookTitle);
	}


}
