/**
 * Class that has the specifications for a Keyword Object
 * @author asim
 *
 */
public class Keyword implements Comparable<Keyword>{
	
	private String myKeyword; // Keyword
	
	/**
	 * Constructor for a Keyword Object
	 * @param k			- keyword string
	 */
	
	public Keyword(String k) {
		this.myKeyword = k.toLowerCase();
	}
	
	/**
	 * Overridden methods and getters and setters
	 */

	@Override
	public int compareTo(Keyword arg0) {
		return this.myKeyword.compareTo(arg0.myKeyword);
	}
	
	@Override 
	public boolean equals(Object o) {
		Keyword other = (Keyword) o;
		return this.myKeyword.equals(other.myKeyword);		
	}
	
	@Override
	public int hashCode() {
		return this.myKeyword.hashCode();
	}
	
	@Override 
	public String toString() {
		return this.myKeyword;
	}

}
