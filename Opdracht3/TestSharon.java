import java.util.*;
import java.io.*;
import java.util.HashMap;

public class TestSharon {
 /*
  * Input: 1. Name of the file to get the n-grams from 2. Size of the n-grams
  * 3. Amount of highest counts to be printed
  * 
  * First get all sentences from the file. Then get and count the n-grams in
  * those sentences. After that sort the n-grams to get the highest counts.
  * ovis-trainset.txt 2 10
  */

	static ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

	public static ArrayList<ArrayList<String>> filetolines(String filename, int n) throws IOException{
		
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		try {

        	File file = new File(filename);
			Scanner s = new Scanner(file);

            // Create an array containing all words in the text (in the same order)
            while ( s.hasNextLine() ) {
 		        ArrayList<String> words = new ArrayList<String>();
				for(int i = 0; i < n-1; i++){
					words.add("START");
				}
                for ( String word : s.nextLine().split("\\s+") ) {

                    words.add(word);

                }
				for(int i = 0; i < n-1; i++){
					words.add("STOP");
				}
				lines.add(words);
		    }
		} catch ( Exception e ) {
			// FileNotFoundException
			e.printStackTrace();
		}
		return lines;
	}


	// opdracht 3 create train- and testset
	public static void percentage3(int percentage, String filename, int n){
		//create corpus		
		ArrayList<ArrayList<String>> corpus = new ArrayList<ArrayList<String>>();
		try{
			corpus = filetolines(filename,n);
		} catch ( Exception e ) {
			// FileNotFoundException
			e.printStackTrace();
		}
		float size = corpus.size();
		int testsize = (int)(size/100 * percentage);
		System.out.println(testsize);
		
		ArrayList<ArrayList<String>> traincorpus = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> testcorpus = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < size; i++){
			if(i < testsize)
				traincorpus.add(corpus.get(i));		
			else
				testcorpus.add(corpus.get(i));
		}	
		HashMap<String, Integer> ngram = new HashMap<String, Integer>();			
		ngram = createNgrams(testcorpus, n);
		System.out.println(ngram);

	}

	public static void main(String[] args) throws IOException {

		int n = Integer.parseInt(args[0]);
		System.out.printf("??? %d \n",n);
		String file1 = args[1];
		//String file2 = args[2];
		//String file3 = args[3];

		//create corpuslines from file
		lines = filetolines(file1,n); 

		HashMap<String, Integer> ngram = new HashMap<String, Integer>();			
		HashMap<String, Integer> n_1gram = new HashMap<String, Integer>();
		ngram = createNgrams(lines, n);
		n_1gram = createNgrams(lines, n-1);



		//sort ngram
		LinkedHashMap<String, Integer> sortedNgrams = new LinkedHashMap<String, Integer>();
		System.out.println("n gram");
		//SORTEREN GAAT MIS!
		sortedNgrams = printngrams(ngram, sortedNgrams,n);
		System.out.println("n-1 gram");
		LinkedHashMap<String, Integer> sortedN_1grams = new LinkedHashMap<String, Integer>();
		sortedN_1grams = printngrams(n_1gram, sortedN_1grams,n);			

		//calculateprob(ngram, n_1gram, n, file2);
		//print to file
     	PrintStream out = new PrintStream(new FileOutputStream("OutFileSHARON.txt"));
		for(Map.Entry<String, Integer> entry : sortedNgrams.entrySet())
		{
			String key = entry.getKey();
			Integer val = entry.getValue();
			//System.out.printf("%s, %d\n", key, val);
       		out.println(val+ ": " + key);
		}
      	out.close();
     	PrintStream out2 = new PrintStream(new FileOutputStream("OutFileSHARON_1.txt"));
		for(Map.Entry<String, Integer> entry : sortedN_1grams.entrySet())
		{
			String key = entry.getKey();
			Integer val = entry.getValue();
			//System.out.printf("%s, %d\n", key, val);
        	out2.println(val+ ": " + key);
		}
	    out2.close();

		//calculateprob_3(file3,n);

		//percentage3(1, "corpus.txt", 2);
	}

	// exercise 2.3
	public static void calculateprob_3(String filename, int n){
		
		try{
           ArrayList<ArrayList<String>> testlines = new ArrayList<ArrayList<String>>();
			testlines = filetolines(filename,n );
  
			System.out.println("Probabilities -------- \n");
			//loop lines
			for(int i = 0; i < testlines.size(); i++){

				double count = 1;
				for(int j = 2; j < testlines.get(i).size(); j++){

					//create ngram with j = n					
					HashMap<String, Integer> ngram = new HashMap<String, Integer>();			
					ngram = createNgrams(lines, j);
					
					HashMap<String, Integer> n_1gram = new HashMap<String, Integer>();			
					n_1gram = createNgrams(lines, j-1);



					//get substring 
					String substr = "";
					String substr_n1 = "";
					double prob1 = 0;
					double prob2 = 0;

					for(int k = 0; k < j; k++){
						substr += " " + testlines.get(i).get(k)  ;
						if(k < j-1)
							substr_n1 += " " + testlines.get(i).get(k)  ;
					}	

					//get value of substring from ngram
					if(ngram.containsKey(substr))
					{
						prob1 =  (double)(ngram.get(substr))/ (double)(ngram.size());
						System.out.println(substr) ;
						System.out.println(prob1);
					}

					if(n_1gram.containsKey(substr_n1))
					{
						System.out.println(substr_n1);
						prob2 = (double)(n_1gram.get(substr_n1))/ ((double)n_1gram.size());
						System.out.println(prob2);
					}
					
					
					System.out.println((double)prob1/(double)prob2);
					count = (double)(count * ((double)prob1/(double)prob2) );
	
					}
				System.out.printf("STRING: %s \n", testlines.get(i));			

				System.out.printf("Final count: ");
				System.out.println(count);	
			}

			}catch ( Exception e ) {
			// FileNotFoundException
			e.printStackTrace();}

		}

	public static void calculateprob(HashMap<String, Integer> ngram, HashMap<String, Integer> n_1gram, int n, String filename){
		
		try{

            ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
			lines = filetolines(filename, n);
            // Create an array containing all words in the text (in the same order)
           
			HashMap<String, Double> prob = new HashMap<String, Double>();			
			//get wn			
			for(int i = 0; i < lines.size()-1; i++){
				//create only for testsentences of length n		
				int val  = 0;
				int valn_1 = 0;		
				if(lines.get(i).size() == n){				
					String str = "";
					for(int k = 0; k < lines.get(i).size(); k++){
							str = str.concat(" " +lines.get(i).get(k));		
					}

					String substr = "";		
					//get wn-1				
					for(int j = 0; j < lines.get(i).size()-1; j++){
							substr = substr.concat(" " + lines.get(i).get(j));		
					}

					//get values
					if(ngram.containsKey(str))
					{
						val = ngram.get(str);
					}
					if(n_1gram.containsKey(substr))
					{
						valn_1 = n_1gram.get(substr);
					}
					//calculate prob
					Double prob_1 = (double) val / (double) valn_1;
					prob.put(str, prob_1);
					
				}

			}
			System.out.println(prob);
			}catch ( Exception e ) {
			// FileNotFoundException
			e.printStackTrace();
		}
	}

	public static LinkedHashMap<String, Integer> printngrams(HashMap<String, Integer> nGramCounts, LinkedHashMap<String, Integer> sortedNgrams, int m){

        ValueComparator bvc =  new ValueComparator(nGramCounts);
        sortedNgrams.putAll(nGramCounts);




	 /*
	  * Printing the top m n-grams or all of them if m is bigger than the
	  * n-gram set
	  */
	 Iterator<String> it = sortedNgrams.keySet().iterator();
	 int i = 0;


	 while (it.hasNext()) {
	 	String nGram = (String) it.next();
	 	Integer count = sortedNgrams.get(nGram);

	 	if (i < sortedNgrams.size()) {
	 		i++;
	 		//System.out.println(i + ". " + nGram + ": " + count);
	 	}
	 }
	return sortedNgrams;
}


	public static HashMap<String, Integer> createNgrams(ArrayList<ArrayList<String>> lines, int n){
		HashMap<String, Integer> ngram = new HashMap<String, Integer>();
		// loop through all text	
		for(int i =0; i < lines.size(); i++){

			// loop through sentence if long enough sentence to form ngram!
			if(lines.get(i).size() > n){

				for(int j = 0; j < lines.get(i).size(); j++){
					//break if not enough words left for ngram

					if(j+n > lines.get(i).size()){
						continue;
					}
					//create ngram string
					else{
						String str = "";
						for(int k = 0; k < n; k++){
							str = str + " " + lines.get(i).get(k+j);
						}

						//if str is known, add value
						if(ngram.containsKey(str))
						{
							Integer val = ngram.get(str);
							val += 1;
							ngram.put(str, val);
						}
						else{
							ngram.put(str, 1);	
						}
					}
				}

			}


		}


		return ngram;
	}

}


	class ValueComparator implements Comparator {

	  Map base;
	  public ValueComparator(Map base) {
		  this.base = base;
	  }

	  public int compare(Object a, Object b) {

		if((Double)base.get(a) < (Double)base.get(b)) {
		  return 1;
		} else if((Double)base.get(a) == (Double)base.get(b)) {
		  return 0;
		} else {
		  return -1;
		}
	  }
	}	
