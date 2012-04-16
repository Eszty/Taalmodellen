import java.io.File;
import java.util.ArrayList;

public class LanguageModel {


	private static ArrayList<NGram> ngrams;
	private static ArrayList<NGram> n1grams;
	private NGramExtractor2 nExtractor;
	private NGramExtractor2 n1Extractor;
	private int n;
	
	public LanguageModel( int input_n, ArrayList<String> words ) {
		n = input_n;

		nExtractor = new NGramExtractor2( n, words );
		ngrams = nExtractor.getArrayList();
		n1Extractor= new NGramExtractor2( n - 1, words );
		n1grams = n1Extractor.getArrayList();
	}
	
	public LanguageModel(int input_n, File corpus) {
		n = input_n;

		nExtractor = new NGramExtractor2( n, corpus );
		ngrams = nExtractor.getArrayList();
		NGramExtractor2 n1Extractor= new NGramExtractor2( n - 1, corpus );
		n1grams = n1Extractor.getArrayList();
	}


	double calculateSentence(ArrayList<String> sentence) {
		double prod = 1;
		String[] sequence = new String[n];

		for ( int i = 0; i < sentence.size() + 1; i++ ) {
			if ( i+n > sentence.size() ) {
				break;
			}
			for ( int j = 0; j < n; j++ ) {
				sequence[j] = sentence.get(i+j);
			}
			double val = calculateSequence(sequence);
			if ( val == 0 ) {
				return 0;
			} else {
				prod *= val;
			}
		}
		return prod;
	}

	double calculateSequence(String[] sequence) {
		int n_count = 0;
		int n1_count = 0;

		String n_str = sequence[0];
		String n1_str = sequence[0];

		for ( int i = 1; i < n; i++ ) {
			if ( i < n - 1 ) {
				n1_str += " " + sequence[i];
			}
			n_str += " " + sequence[i];
		}

		for ( NGram ngram : ngrams ) {
			if ( ngram.getString().equals(n_str) ) {
				n_count = ngram.getCount();
				break;
			}
		}

		for ( NGram ngram : n1grams ) {
			if ( ngram.getString().equals( n1_str ) ) {
				n1_count = ngram.getCount();
				break;
			}
		}

		try {
			return (double) n_count/n1_count;
		} catch ( Exception e ) {
			// Division by zero
			return 0;
		}
	}
	
	public double calculateGoodTuringProbability( String[] sequence ) {
		int n_count = 0;

		String n_str = sequence[0];
		
		for ( int i = 1; i < n; i++ ) {
			n_str += " " + sequence[i];
		}

		for ( NGram ngram : ngrams ) {
			if ( ngram.getString().equals(n_str) ) {
				n_count = ngram.getCount();
				break;
			}
		}
		
		if ( n_count != 0 ) {
			int N_c1 = nExtractor.freqCount(n_count + 1);
			int N_c  = nExtractor.freqCount(n_count);
			
			double new_count = (n_count + 1) * ( (double) N_c1 / N_c );
			try {
				return (double) new_count/nExtractor.freqSum();
			} catch (Exception e) {
				return 0;
			}
		} else {
			try {
				return (double) nExtractor.freqCount(1) / nExtractor.freqSum();
			} catch ( Exception e) {
				return 0;
			}
		}
		
	}
	
	void show(int m) {
		nExtractor.show(m);
	}
}
