import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


/**
 * The class that handles all of the information we receive and creates objects
 * necessary to calculate the bookScores
 * @author asim
 *
 */
public class Bookbub {
	
	// Map of Strings representing Genres to the Genre Objects
	public static Map<String, Genre> allGenres = new HashMap<String, Genre>();
	// Map of Keyword Objects to Keyword Objects used for fast lookup
	public static Map<Keyword, Keyword> allKeywords = new TreeMap<Keyword, Keyword>();
	// Map of Genre Object to a list of all Keyword Objects it contains
	public static Map<Genre, List<Keyword>> myGenreAndKeywords = new HashMap<Genre, List<Keyword>>();
	// Map of Genre Object to a SET of all Keyword Objects it contains -- used for fast lookup
	public static Map<Genre, Set<Keyword>> myGenreAndKeywordsLookup = new HashMap<Genre, Set<Keyword>>();
	// Map of Keyword Objects to a List of Genre Objects that they belong to
	public static Map<Keyword, List<Genre>> myKeywordToGenreMap = new HashMap<Keyword, List<Genre>>();
	// Set of all Keyword Objects
	public static Set<Keyword> myKeywords = new HashSet<Keyword>();
	// List of BookScore objects that store the final score calculation information
	public static List<BookScore> myBooks = new ArrayList<BookScore>();;
	
	/**
	 * Method that checks whether a Genre already exists 
	 * @param g			- Genre Object
	 * @return boolean	- true or false
	 */
	
	public static boolean checkGenre(Genre g) {
		if(myGenreAndKeywords.containsKey(g)) {
			// System.out.println("Genre already exists! Check failed!" );
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method that adds a new Genre to the corresponding data structures
	 * @param newG			- Genre Object that needs to be added
	 * 
	 */
	public static void addGenre(Genre newG) {
		allGenres.put(newG.getName(), newG);
		myGenreAndKeywords.put(newG, new ArrayList<Keyword>());
		myGenreAndKeywordsLookup.put(newG, new HashSet<Keyword>());
	}
	
	/**
	 * Method that adds a Keyword to a Specific Genre that it belongs to
	 * @param k				- Keyword Object
	 * @param g				- Genre Object
	 * @param score			- int Score for the keyword in that Genre
	 * @throws Exception
	 */
	
	public static void addKeywordToGenre(Keyword k, Genre g, int score) throws Exception {
		if(myGenreAndKeywordsLookup.get(g).size() != 0 && myGenreAndKeywordsLookup.get(g).contains(k)) {
			throw new Exception();
		} else {
			allGenres.get(g.getName()).putKeywordScore(k, score);
			myGenreAndKeywords.get(g).add(k);
			myGenreAndKeywordsLookup.get(g).add(k);
			myKeywords.add(k);
			if(myKeywordToGenreMap.containsKey(k)) {
				myKeywordToGenreMap.get(k).add(allGenres.get(g.getName()));
			} else {
				List<Genre> newList = new ArrayList<Genre>();
				newList.add(allGenres.get(g.getName()));
				myKeywordToGenreMap.put(k, newList);
			}
		}
	}
	
	/**
	 * Method that generates a keyword to keyword mapping
	 * @param k			- Keyword Object
	 */
	
	public static void putStringKeywordMapping(Keyword k) {
		if(allKeywords.containsKey(k)) {
			// ignore if the keyword already exists
			return;
		} else {
			allKeywords.put(k, k);
		}
	}
	
	public static void main(String[] args) throws NumberFormatException, Exception {
		
		//long startTime = System.currentTimeMillis();
		
		// Get the name of the JSON file with Book Descriptions
		String sampleJSONFileName = System.getProperty("user.dir") + "/" + args[0];
		// Get the name of the Genre and Keywords CSV file
		String sampleGenreKeywordsFileName = System.getProperty("user.dir")+ "/" + args[1];

		// Create file objects with the above arguments to pass to the BookReader and the CSVReader
		File books = new File(sampleJSONFileName);
		File csvFile = new File(sampleGenreKeywordsFileName);
		
		// Initialize the BookReader object to read in the book Description
		BookReader bookReader = new BookReader(books);
		
		// Initialize the CSVReader object to read in the CSV file information
		CSVReader csvReader = new CSVReader(csvFile);
		
		// parse the CSV file first so that we can preprocess and make the genre objects and the keyword Objects
		// which we will use to search each input in the Book Description file.
		csvReader.parseCSVFile();
		
		// parse the Book Description information file which will handle the search
		// and populate the fields necessary to calculate their genre specific scores
		// this is a design preference I went for because I didn't want to store the 
		// book description data as the number of books in the book sample file could 
		// be very large and that would need a lot of space
		bookReader.parseJSONFile();
		
		// Sort the books lexicographically after their scores have been calculated
		Collections.sort(myBooks);

		// Calculate the score for each book and print it out
		for(BookScore book : myBooks) {
			String result = book.generateGenreScores();
			System.out.println(book);
			System.out.println(result);
		}
		//long endTime = System.currentTimeMillis();
		//System.out.println(endTime - startTime);
	}

}
