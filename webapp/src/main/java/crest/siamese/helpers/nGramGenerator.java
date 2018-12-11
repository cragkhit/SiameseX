package crest.siamese.helpers;


import java.util.ArrayList;

public class nGramGenerator {
	private int n;
	public nGramGenerator(int n) {
		// set the number "n" in n-grams
		this.n = n;
	}
	
	public String[] generateNGrams(String s) {
		// no. of n-grams from a string S  = size(S)-N+1
		int noOfNGrams = s.length()-this.n+1;
		// create an array to store the n-grams
		String[] ngrams = new String[noOfNGrams];
		for (int i = 0; i < noOfNGrams; i++) {
			ngrams[i] = s.substring(i, i + this.n);
			// System.out.print(ngrams[i] + " ");
		}
		// System.out.println();
		return ngrams;
	}
	
	public ArrayList<String> generateNGramsFromJavaTokens(ArrayList<String> tokens) {
		// no. of n-grams from a string S  = size(S)-N+1
		int noOfNGrams = tokens.size()-this.n+1;
		// create an array to store the n-grams
		ArrayList<String> ngrams = new ArrayList<String>();
		for (int i = 0; i < noOfNGrams; i++) {
			String finalNgrams = "";
			for (int j=0; j < this.n; j++) {
				finalNgrams += tokens.get(i+j);
			}
			ngrams.add(finalNgrams);
			// System.out.print(ngrams.get(i) + " / ");
		}
		// System.out.println();
		return ngrams;
	}
}
