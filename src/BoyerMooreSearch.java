import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoyerMooreSearch {
	private final int R; // radix
	private int[] right; // skip bad chars
	private String keyword;
	
	public BoyerMooreSearch(String kw) {
		this.R = 256;
		this.keyword = kw;
		
		right = new int[R];
		for(int c = 0; c < R; c++) {
			right[c] = -1;
		}
		for(int j = 0; j < kw.length(); j++) {
			right[kw.charAt(j)] = j;
		}
	}
	
	public List<Integer> searchText(String text) {
		
		int kLen = keyword.length();
		int textLen = text.length();
		int skip;
		List<Integer> idxList = new ArrayList<Integer>();
		for(int i = 0; i <= textLen - kLen; i += skip) {
			skip = 0;
			for(int j = kLen-1; j >= 0; j--) {
				if(keyword.charAt(j) != text.charAt(i+j)) {
					skip = Math.max(1,  j - right[text.charAt(i+j)]);
					break;
				}
			}
			if(skip == 0) {
				idxList.add(i);
				skip++;
			}
		}
		return idxList;
	}

}