import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Modelcode {
 /*
  * Input: 1. Name of the file to get the n-grams from 2. Size of the n-grams
  * 3. Amount of highest counts to be printed
  * 
  * First get all sentences from the file. Then get and count the n-grams in
  * those sentences. After that sort the n-grams to get the highest counts.
  * ovis-trainset.txt 2 10
  */
public static void main(String[] args) throws IOException {

	 String filename = args[0];
	 int n = Integer.parseInt(args[1]);
	 int m = Integer.parseInt(args[2]);
	 
	 File file = new File(filename);
	 StringTokenizer sentences = getSentences(file);
	 StringTokenizer sentences1 = getSentences(file);
	 HashMap<String, Integer> nGramCounts = wordsCounter(sentences, n);

	 int p = n-1;
	 HashMap<String, Integer> n_1GramCounts = wordsCounter(sentences1, p);

	 HashMap<String, Integer> sortedNgrams = new LinkedHashMap<String, Integer>();
	 HashMap<String, Integer> sortedN_1grams = new LinkedHashMap<String, Integer>();
	 
	 printngrams(nGramCounts, sortedNgrams, m);	
	 //printngrams(n_1GramCounts, sortedN_1grams, m);	
	calculateprob(sortedNgrams,sortedN_1grams);
 }

/*
Calculates probability for testsentences
*/
public static void calculateprob( HashMap<String, Integer> sortedNgrams,  HashMap<String, Integer> sortedN_1grams) throws IOException{
	 File file = new File("lorem.txt");
	 StringTokenizer sentences = getSentences(file);
	 HashMap<String, Integer> probshash = new LinkedHashMap<String, Integer>();

	 System.out.println("NOW");
	 String[] sentenceArray = new String[sentences.countTokens()];
	 String[] subsentenceArray = new String[sentences.countTokens()];
	 
	// create string array from sentences in testfile
	 int j = 0;
 	 String empty = "";
	 while(sentences.hasMoreTokens()){
		sentenceArray[j] = sentences.nextToken();	
		sentenceArray[j] = empty.concat(" " + sentenceArray[j]);
		j++;
	 }

	// create n-1 strings
	for(int k = 0; k < sentenceArray.length; k++)
	{
		String[] words = sentenceArray[k].split(" "); 
		subsentenceArray[k] = "";
		for(int p = 0; p < (words.length-1); p++){
			subsentenceArray[k] = subsentenceArray[k].concat(" " + words[p]);
		}

	}
	for(int i = 0; i < sentenceArray.length; i++){
		System.out.println(sentenceArray[i]);
	//	String a = sentenceArray[i];
		//String b = subsentenceArray[i];
		Integer val = 0;
		Integer subval = 0;
			//iterate through hashmap
			for(Map.Entry<String, Integer> entry : sortedNgrams.entrySet())
			{

				String key = (String) entry.getKey();
				//System.out.println(key);
				if(key.equals(sentenceArray[i])){
					val = entry.getValue();
				}
			}

			for(Map.Entry<String, Integer> entry : sortedN_1grams.entrySet())
			{

				String key = (String) entry.getKey();
				//System.out.println(key);
				if(key.equals(subsentenceArray[i])){
					subval = entry.getValue();
				}
			}
		System.out.printf("%d : %d / %d",i,val, subval);

	}


}

public static void printngrams(HashMap<String, Integer> nGramCounts, HashMap<String, Integer> sortedNgrams, int m){

	 /*
	  * Sorting the created n-gram hashmap with counters
	  */
	 List<String> nGrams = new ArrayList<String>(nGramCounts.keySet());

	 List<Integer> yourMapValues = new ArrayList<Integer>(
	 nGramCounts.values());


	 TreeSet<Integer> sortedSet = new TreeSet<Integer>(yourMapValues);
	 Object[] sortedArray = sortedSet.toArray();
	 int size = sortedArray.length;
	 
	 for (int i = size - 1; i >= 0; i--) {
	 	sortedNgrams.put(nGrams.get(yourMapValues.indexOf(sortedArray[i])),
	 	(Integer) sortedArray[i]);
	 }

	 /*
	  * Printing the top m n-grams or all of them if m is bigger than the
	  * n-gram set
	  */
	 Iterator<String> it = sortedNgrams.keySet().iterator();
	 int i = 0;


	 while (it.hasNext()) {
	 	String nGram = (String) it.next();
	 	Integer count = sortedNgrams.get(nGram);

	 	if (i < m) {
	 		i++;
	 		System.out.println(i + ". " + nGram + ": " + count);
	 	}
	 }
	System.out.println("HELLO");
}

 /*
  * Read the input file, and split every sentence on a newline using a
  * deliminator "\n" Returns a StringTokenizer containing the sentences
  */
public static StringTokenizer getSentences(File file) throws IOException {
 
	StringBuffer contents = new StringBuffer();
	BufferedReader reader = null;

 	try {
 		reader = new BufferedReader(new FileReader(file));
 		String text = null;

 		while ((text = reader.readLine()) != null) {
 			contents.append(text).append(
 			System.getProperty("line.separator"));
 		}

 	} 
	catch (FileNotFoundException e) {
 		e.printStackTrace();
 	}

 	String message = contents.substring(0, contents.length());
 	String deliminator = "\t\n\r\f";
 	StringTokenizer st = new StringTokenizer(message, deliminator);

 	return st;
}
 /*
  * Method used to create a sorted hashMap with n-grams of size n from the
  * inputed StringTokenizer st Creates an String array of size n to
  * temporarily hold individual words so we can still use st.nextToken and
  * not lose data Gets every sentence from the st, converts it into a
  * correctedsentence StringTokenizer st2. Fill temp with the first n words
  * of st2. Create ngram using concate. Put ngram in hashMap or add 1 to the
  * value of n-gram in the hashMap. Grab the next token, delete the oldest
  * token in temp, move the rest i to the left, put grabbed token as newest
  * token. Create ngram over temp values. Check if in hashMap , if not add
  * else increase value by 1 Repeat untill no more tokens in st2 or st.
  */
public static HashMap<String, Integer> wordsCounter(StringTokenizer st, int n) {
 	HashMap<String, Integer> ngramMap = new HashMap<String, Integer>();
 	String sentence;
 	String[] temp = new String[n];
	System.out.println("HELLO");
	System.out.println(n);


 	while (st.hasMoreTokens()) {
 		String ngram = "";
 		sentence = st.nextToken();

 		for (int i = 0; i < n - 1; i++) {
 			sentence = "<s> " + sentence;
			
 		}

 		sentence = sentence + " </s>";
 		StringTokenizer st2 = new StringTokenizer(sentence, " ");

 		for (int c = 0; c < n; c++) {
 			temp[c] = st2.nextToken();
 			// fix
 			}

 		for (int i = 0; i < n; i++) {
 			ngram = ngram.concat(" " + temp[i]);
 		}

 		if (ngramMap.containsKey(ngram)) {
 			ngramMap.put(ngram, (ngramMap.get(ngram) + 1));
 		} 
		else {
 			ngramMap.put(ngram, 1);
 		}

 			while (st2.hasMoreTokens()) {
 				ngram = "";

 				for (int c2 = 0; c2 < n - 1; c2++) {
 					temp[c2] = temp[c2 + 1];
 				}

 				temp[n - 1] = st2.nextToken();

 				for (int i = 0; i < n; i++) {
 					ngram = ngram.concat(" " + temp[i]);
 				}

 				if (ngramMap.containsKey(ngram)) {
 					ngramMap.put(ngram, (ngramMap.get(ngram) + 1));
 				} 
				else {
 					ngramMap.put(ngram, 1);
 				}
 			}
 		}
 	return ngramMap;
 }
}
