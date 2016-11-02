import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * 
 */

/**
 * @author asim
 * CSV Reader reads in the Genre, Keyword, Score information
 */

public class CSVReader {
	
	private FileReader myFile;	// File that needs to be read
	/**
	 * Constructor
	 * @param File file 		- File Containing the information 
	 */
	public CSVReader(File file) {
		try {
			this.myFile = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that parses the CSV file
	 */
	
	public void parseCSVFile() throws NumberFormatException, Exception {
		
		String csvSplit = ", ";
		String csvLine = "";
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(this.myFile);
			// while there is a new line
			while((csvLine = br.readLine()) != null) {
				
				// get the values as an array of strings splitting on the comma
				String[] currValues = csvLine.split(csvSplit);
				
				// if it is the first line, that means we have the headings
				if(currValues[0].equals("Genre")) {
					
					// skip the line that has the headings
					continue;
					
				} else {
					
					// Create new Genre and Keyword Objects
					Genre newGenre = new Genre(currValues[0]);
					Keyword newKeyword = new Keyword(currValues[1]);
					
					// add this keyword to the Set of Keywords seen so far
					ScoreCalculator.myKeywords.add(newKeyword);
					ScoreCalculator.putStringKeywordMapping(newKeyword);
					
					// Get the keyword score
					int score = Integer.parseInt(currValues[2]);
					
					// add this keyword with its score to the specific genre it belongs to
					
					if(ScoreCalculator.checkGenre(newGenre)) {
						// if the genre exists, add the keyword and its score to the genre
						ScoreCalculator.addKeywordToGenre(newKeyword, newGenre, score); 
					} else {
						
						// if the genre doesn't exist, add the genre and then add the keyword and its score
						// to that genre
						ScoreCalculator.addGenre(newGenre);
						ScoreCalculator.addKeywordToGenre(newKeyword, newGenre, score);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
