import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;
import java.util.List;

/** *** IMPORTANT INFORMATION ***
 * I AM USING AN EXERTAL JSON PARSER LIBRARY (org.json.simple) WHICH I DID NOT WRITE MYSELF. CREDIT DUE WHERE IT IS DUE
 * **************************************************************************************************
 * @author asim
 *	BookReader class contains the specification for a BookReader Object
 *	A BookReader object will parse the input json file provided to it
 *	*** The implementation for this BookReader is based on the sample 
 *	*** input file provided in the description
 */

public class BookReader {
	
	private FileReader myFile;			// File 

	/**
	 * Constructor
	 * @param file			- File Object
	 */
	public BookReader(File file) {
		try {
			this.myFile = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that parses the JSON file and creates the BookScore
	 * Objects and fills them with the keyword information 
	 * necessary to calculate the final score
	 * @throws FileNotFoundException
	 */
	
	@SuppressWarnings("unchecked")
	public void parseJSONFile() throws FileNotFoundException {
		
		// Using an exernal JSON parser library. See description above
		JSONParser parser = new JSONParser();
		
		try {
			// As given in the example, the entire file is one Object, so we read it in.
			Object object = parser.parse(this.myFile);
			
			// Now as given in the example file, we have an array of JSON Objects within our parent Object.
			// We create a JSON Array of these JSONObjects
			// bookObjects = [{JSONObject}, ..., {JSONObject}]
			JSONArray booksObjects = (JSONArray) object;
			
			// Iterator to Iterate through each individual JSONObject
			Iterator<JSONObject> iter = booksObjects.iterator();
			
			// There are two ways which we can approach calculating the genre score.
			// 1. We don't need to save the book description, as we would already have it saved in our JSON file.
			//    We can access the description from the JSON file whenever we need to.
			// 2. We create a new book object with the title and the description both saved. 
			
			// I will implement my code as described in the 1st example as I am assuming that we don't need 
			// to further save in the book description and take extra memory.
			
			// Loop through each book object
			while(iter.hasNext()) {
				// As given in the example JSON file, we get each book object
				// book = {"title" : "bookTitle", "description" : "bookDescription"}
				JSONObject book = (JSONObject) iter.next();
				
				// parse out the title and description of each book
				String bookTitle = (String) book.get("title");
				String bookDescription = (String) book.get("description");
				
				// Create a BookScore object, which contains the title and information necessary to
				// calculate the genre score.
				BookScore bookScore = new BookScore(bookTitle);
				
				// Add the bookScore object to an ArrayList so that we can retrieve the information later
				Bookbub.myBooks.add(bookScore);
				
				// Replace all non-alphanumeric characters and convert the string to lowerCase
				// we leave in the -s because keywords could be made up of words like not-to-distant
				// (ASSUMPTION: keywords don't contain any non-alphanumeric characters)
				bookDescription = bookDescription.trim().replaceAll("[^a-zA-Z-\\s]", "").toLowerCase();
				
				// System.out.println(bookDescription);
				
				// Get the keywords read in from CSV files as a Set
				Set<Keyword> keywordsSet = Bookbub.myKeywords;
				
				for(Keyword keyword : keywordsSet) {
					// Employ Boyer Moore String Search algorithm to detect which indexes does a single keyword occur at.
					BoyerMooreSearch bms = new BoyerMooreSearch(keyword.toString());
					
					// List of all the indexes where the keyword occurs
					List<Integer> kOccurIdx = bms.searchText(bookDescription);
				
					if(kOccurIdx.isEmpty()) {
						// if the list of indexes is empty, we didn't find the keyword, so skip
						continue;
					} else {
						//System.out.println("keyword: " + keyword);
						//System.out.println(kOccurIdx);
						// Keyword match found! Add the keyword to the bookScore object for the current book
						// the number of times it was found!
						for(int i = 0; i < kOccurIdx.size(); i++){
							bookScore.addKeywordToBookScore(keyword);
						}
						// System.out.println(kOccurIdx);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
		
}
