import java.util.*;
import java.util.Map.Entry;
import java.io.*;
/**
 * Extracts all ngrams out of a text file and puts START and STOP symbols at the beginning and end of each paragraph
 * @author Steven
 *
 */
public class NGramExtractor2 {
	
	private int n;
	private ArrayList<NGram> ngrams = new ArrayList<NGram>();
	
	public NGramExtractor2( int inputN, ArrayList<String> words ) {
		// Set the number n for n-grams
		n = inputN; 
	
		extractNGrams( words );
	}
	
	
	public NGramExtractor2( int inputN, File file ) {
		
		// Set the number n for n-grams
		n = inputN; 
		
		try {

            Scanner s = new Scanner(file);
            ArrayList<String> words = new ArrayList<String>();
           
            words.add("START");
            // Create an array containing all words in the text (in the same order)
            while ( s.hasNextLine() ) {
                for ( String word : s.nextLine().split("\\s+") ) {
                    if ( word.equals("") ){
                        if( !((words.get(words.size()-1)).equals("START")) ){
                            words.add("STOP");
                            words.add("START");
                        }
                    } else {
                        words.add(word);
                    }
                   
                }
            }
			words.add("STOP");
			
			extractNGrams( words );
			
		} catch ( Exception e ) {
			// FileNotFoundException
			e.printStackTrace();
		}
	}
	
	public void extractNGrams( ArrayList<String> words ) {
		
		// Temporary HashMap for storing ngrams
		HashMap<String,Integer> table = new HashMap<String,Integer>();
		
		// Allocate space
		String[] temp_ngrams = new String[n];
		int[] sizes = new int[n];
		int offset = 0;
		
		// Initialize the variables
		for ( int i = 0; i < n; i++ ) {
			temp_ngrams[i] = "";
			sizes[i] = 0;
		}
		
		// Start the real work
		for ( int i = 0; i + offset < words.size(); i++ ) {
			String word = words.get(i + offset);
			for ( int j = 0; j < n; j++ ) {
				if ( sizes[j] == n ) {
					// We collected a new ngram!
					// Check whether we already collected it
					if ( table.containsKey( temp_ngrams[j] ) ) {
						table.put( temp_ngrams[j]  , table.get( temp_ngrams[j] ) + 1 );
					} else {
						table.put( temp_ngrams[j], 1 );
					}
					// Start with the creation of a new ngram
					temp_ngrams[j] = word;
					sizes[j] = 1;
				} else {
					// Otherwise, the ngram contains at least 1 word
					if ( i >= j ) { // This condition permits multiple ngrams to be extracted simultaneaously
						// We have an array of words. We look at each word once. So when we encouter a word,
						// it is added to at most n ngrams: 
						//  1  2  3  4  5
						//  .  .  .
						//     .  .  .
						//        .  .  .
						// If we are looking for trigrams, the trigram collector has a running collection 
						// of three trigrams, because when the first trigram is about to finish ( ie it has already
						// two words and 'sees' the next word), another trigram is starting, as showed above.
						// 
						if ( sizes[j] != 0 ) {
							// We need to add an extra space between words, if there is already one, otherwise
							// all words of the ngram are stitched together
							temp_ngrams[j] += " ";
						}
						temp_ngrams[j] += word;
						sizes[j]++;
					}
				}
			}
			
			if ( word.equals("STOP") ) {
				offset += i + 1;
				i = -1;
			
				for ( int j = 0; j < n; j++ ) {
					if ( sizes[j] == n ) {
						// We collected a new ngram!
						// Check whether we already collected it
						if ( table.containsKey( temp_ngrams[j] ) ) {
							table.put( temp_ngrams[j]  , table.get( temp_ngrams[j] ) + 1 );
						} else {
							table.put( temp_ngrams[j], 1 );
						}
					}
					temp_ngrams[j] = "";
					sizes[j] = 0;
				}					
			}
		}
		
		// Collect the last ngram (because the loop stops after adding the last word to an array, not to the table)
		for ( int j = 0; j < n; j++ ) {
			if ( sizes[j] == n ) {
				// We collected a new ngram!
				// Check whether we already collected it
				if ( table.containsKey(temp_ngrams[j]) ) {
					table.put(temp_ngrams[j], table.get(temp_ngrams[j]) + 1 );
				} else {
					table.put(temp_ngrams[j], 1);
				}
			}
		}
		
		// Now we have a HashMap containing every possible ngram and its count
		// So we copy all counts into the NGram instances, so we can sort them
		for ( Entry<String, Integer> e : table.entrySet() ) {
			NGram g = new NGram( n, e.getKey(), e.getValue() );
			ngrams.add( g );
		}
	
		// Now lets sort it
		Collections.sort( ngrams );
	}
	/**
	 * Counts the number of sequences with a frequency of f
	 * @param f is the frequency to count
	 */
	public int freqCount( int f ) {
	
		int count = 0;
		
		// For every entry, if its frequency equals f, increment the counter
		Iterator<NGram> i = ngrams.iterator();
		while ( i.hasNext() ) {
			if ( i.next().getCount() == f ) {
				count++;
			}	
		}
		return count;
	}
	
	/**
	 * @return the sum of all frequencies in the ngram list
	 */
	public int freqSum() {
		
		int sum = 0;
		
		// For every entry, add its frequency to the sum
		Iterator<NGram> i = ngrams.iterator();
		while ( i.hasNext() ) {
			sum += i.next().getCount();
		}
		
		return sum;
	}
	
	/**
	 * Shows the first m elements in the lis
	 * @param m the number of elements you want to see, -1 if you want to see all
	 */
	public void show( int m ) {
		
		// Set the m to maximum size if necessary
		if ( m == -1 || m > ngrams.size() ) {
			m = ngrams.size();
		}
	
		// Do the printing
		Iterator<NGram> i = ngrams.iterator();
		while ( i.hasNext() && m > 0 ) {
			System.out.println( i.next().toString() );
			m--;
		}
	}
	
	/**
	 * @return the size of the ngram table
	 */
	public int getSize() {
		return ngrams.size();
	}
	/**
	 * @return the ArrayList containing all ngrams extracted from the corpus
	 */
	public ArrayList<NGram> getArrayList() {
		return ngrams;
	}
	
}
