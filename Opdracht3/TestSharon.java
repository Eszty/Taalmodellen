import java.util.*;
import java.io.*;
import java.util.HashMap;

public class TestSharon {

	//trainingset
	static ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
	static HashMap<String, Integer> trainset_ngram = new HashMap<String, Integer>();			

	// MAIN
	public static void main(String[] args) throws IOException {

		int n = Integer.parseInt(args[0]);
		String file1 = args[1];		//trainingset
		String file2 = args[2];		//testset

		//calculate probability sentences (String: line, Integer: prob)
		HashMap<ArrayList<String>, Float> prob = new HashMap<ArrayList<String>, Float>();			
		prob = calculateprobability(file1, file2, n);
		System.out.println(prob);
		
/*     	
		calculateprob_3(file2,n);

		percentage3(1, "corpus.txt", 2);*/
	}

	//calculates probability sentences (String: line, Integer: prob)
	public static HashMap<ArrayList<String>, Float> calculateprobability(String trainfile, String testfile, int n){
		HashMap<ArrayList<String>, Float> prob = new HashMap<ArrayList<String>, Float>();		//hashmap for probabilities
		ArrayList<String> problines = new ArrayList<String>();				//all lines from file
		
		try {
           	File file = new File(testfile);
			Scanner s = new Scanner(file);

            // Create an array containing all words in the text (in the same order)
            while ( s.hasNextLine() ) {
    			String line = s.nextLine();
				problines.add(line);
		    }
		} catch ( Exception e ) {
			// FileNotFoundException
			e.printStackTrace();
		}

		//create ngrams for testset
		ArrayList<ArrayList<String>> testset_lines = new ArrayList<ArrayList<String>>();
		try{
			testset_lines = filetolines(testfile, n);
			lines = filetolines(trainfile,n); 
		} catch ( Exception e ) {
			// FileNotFoundException
			e.printStackTrace();
			System.out.println("problem with testset");
		}

		/*//create corpuslines from trainingset
		HashMap<String, Integer> trainset_ngram = new HashMap<String, Integer>();			
		trainset_ngram = createNgrams(lines, n);

		HashMap<String, Integer> trainset_ngram_1 = new HashMap<String, Integer>();			
		trainset_ngram_1 = createNgrams(lines, n-1);

		//create ngrams for testset
		HashMap<String, Integer> testset_ngram = new HashMap<String, Integer>();			
		testset_ngram = createNgrams(testset_lines, n);*/
		
		//probs per line
		for(int i = 0; i < testset_lines.size(); i++){
			ArrayList<String> line = testset_lines.get(i);
			ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
			lines.add(line);
			float probability = calcprob(lines, n);
			prob.put(line, probability);
		}

		return prob;
	}

	public static float calcprob(ArrayList<ArrayList<String>> testline, int n){
		float probability = 1;
		//create ngram from sentence
		HashMap<String, Integer> testset_ngram = new HashMap<String, Integer>();			
		testset_ngram = createNgrams(testline, n);
		
		//create ngrams of trainingset
		HashMap<String, Integer> trainset_ngram = new HashMap<String, Integer>();			
		trainset_ngram = createNgrams(lines, n);

		HashMap<String, Integer> trainset_ngram_1 = new HashMap<String, Integer>();			
		trainset_ngram_1 = createNgrams(lines, n-1);


		for (Map.Entry<String, Integer> mapEntry : testset_ngram.entrySet()) {
		float count_up = 0;
		float count_down = 0;
			String keyTest = mapEntry.getKey();
			//create wi-1			
			String[] test = keyTest.split(" ");
			String keyTest2 = "";
			for(int i = 0; i < test.length-1; i++){
				if(i == 0)
					keyTest2 = test[i];
				else
					keyTest2 = keyTest2 + " " + test[i];				
			}
			//get count(wi-1, wi)
			for (Map.Entry<String, Integer> mapEntryTrain : trainset_ngram.entrySet()) {
				String keyTrain = mapEntryTrain.getKey();
				if(keyTest.equals(keyTrain)){
					count_up = mapEntryTrain.getValue();
					break;
				}
			}
			//get count(wi-1)
			for (Map.Entry<String, Integer> mapEntryTrain : trainset_ngram.entrySet()) {
					String keyTrain = mapEntryTrain.getKey();
					String[] temp = keyTrain.split(" ");					
					String[] keytesttemp = keyTest2.split(" ");
					if(temp[1].equals(keytesttemp[1])){
						count_down += mapEntryTrain.getValue();
						System.out.printf("Trainstring: %s -> %d \n", keyTrain, mapEntryTrain.getValue());
						//System.out.println(keyTrain)

					}
					/*if(keyTest2.equals(keyTrain)){
						count_down = mapEntryTrain.getValue();
						break;
					}*/
				}
			System.out.println("\nkeyTest");
			System.out.println(keyTest);
			System.out.printf("count up: %f \n", count_up);
			System.out.printf("count down: %f \n\n-------\n", count_down);
			if(count_down==0)
				count_down = 1;
			if(count_up==0)
				count_up = 1;

			//P(wi|wi-1) = Count(wi-1, wi) / Count(wi, w) voor gehele zin
			probability *= (count_up/count_down);


		}
			//System.out.println(testline);
			//System.out.println(probability);
		return probability;
	}

	//FILETOLINES: Get lines from file
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

///////////////////////////////////////////////////////////////
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




// exercise 3!!
	public static void calculateprob_3(String filename, int n){
		
		try{
           ArrayList<ArrayList<String>> testlines = new ArrayList<ArrayList<String>>();
			testlines = filetolines(filename,n );
 			HashMap<String, Integer> ngram_testlines = new HashMap<String, Integer>();			
			ngram_testlines = createNgrams(testlines, n); 
			System.out.println(ngram_testlines);
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
