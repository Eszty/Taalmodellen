import java.util.*;
import java.io.*;
import java.lang.Math;

public class NGramTable extends HashMap<String, Integer> {
	
	public static final long serialVersionUID = 1;
	
	private int numberOfWords;
	private String fileName;
	private NGramTable lowerNGram;
	private ArrayList<NGramItem> sortedNGrams;
	private int totalCount, totalSentences;
	
	public static String StringFromArray( String[] in )
	{
		String out = "";
		for ( int i = 0; i < in.length-1; i++ )
			out += in[i] + " ";
		out += in[in.length-1];
		return out;		
	}
	
	public static String[] StringToArray( String in )
	{
		return in.replaceAll("  *", " ").replaceAll("^ +", "").replaceAll(" +$", "").split(" ");
	}
	
	private void ParseFile()
	{
		BufferedReader fr = null;
		this.clear();
		try {
			fr = new BufferedReader( new FileReader(fileName) );
			String line, prefix, postfix;
			
			if ( numberOfWords > 1 )
				postfix = " </s>";
			else
				postfix = "";
			
			prefix = "";
			for ( int i = 0; i < numberOfWords - 1; i++ )
				prefix += "<s> ";
			
			String[] tmp = new String[numberOfWords];
			
			while ( (line = fr.readLine()) != null )
			{
				totalSentences++;
				String words[] = StringToArray(prefix + line + postfix);
				
				for ( int i = 0; i <= words.length - numberOfWords; i++ )
				{
					tmp = Arrays.copyOfRange(words, i, i + numberOfWords );
					addItem(StringFromArray(tmp));
				}				
			}
		} catch( IOException e )
		{
			System.out.println("ERRRORRRR");
		}
		finally {
			try {
			if ( fr != null )
				fr.close();
			} catch( IOException e ){}
		}
	}
	
	private void addItem( String ngram )
	{
		Integer oldVal = get(ngram);
		int newVal = oldVal == null ? 1 : oldVal + 1;
		super.put(ngram, newVal);
		totalCount++;
	}
	
	// zodat niet van buiten kan worden gerotzooid
	private NGramTable( int gram, String fn, boolean shouldCreateLowerNGram )
	{
		fileName = fn;
		totalCount = totalSentences = 0;
		numberOfWords = gram;
		
		ParseFile();
		
		sortedNGrams = null;
		sortList();
		
		if ( numberOfWords > 1 && shouldCreateLowerNGram )
			lowerNGram = new NGramTable( gram - 1, fn, false );
		else
			lowerNGram = null;
	}
	
	public NGramTable( int gram, String fn )
	{
		this( gram, fn, true );		
	}
	
	// berekent de waarschijnlijkheid van ngram. Als de lengte van
	// de ngram niet overeenkomt met numberOfWords: stop. Als nOW == 0:
	// waarschijnlijkheid wordt berekend uit prior prob. Anders:
	// bereken waarschijnlijkheid door te delen door frequentie
	// ngram 1 orde kleiner.
	// cf Jurafsky & Martin (2009): 
	// P(w_n | w_1 ... w_n-1) = Count(w_1 ... w_n) / Count(w_1 ... w_n-1) 
	public double ProbabilityOfNGram_Unsmoothed( String[] ngram )
	{
		if ( ngram.length != numberOfWords)
			return -1.;
		double denominator;
		if ( numberOfWords == 1 )
			denominator = (double)totalCount;
		else if ( ngram[ngram.length - 2].equals("<s>") )
			denominator = (double)totalSentences;
		else 
		{
			String shorter[] = Arrays.copyOfRange(ngram, 0, numberOfWords-1);
			Integer x = lowerNGram.get(StringFromArray(shorter));
			if ( x != null )
				denominator = (double)x;
			else
			{
				return 0;
			}
		}
			
		Integer x = get(StringFromArray(ngram));
		if ( x != null )
			return (double)x/denominator;
		else
			return 0;
	}
	
	public double ProbabilityOfNGram_Unsmoothed( String ngram )
	{
		return ProbabilityOfNGram_Unsmoothed(StringToArray(ngram));
	}
	
	// berekent waarschijnlijkheid van string woorden W van lengte M, beginnend met N - 1 <s>
	// symbolen en eindigend op 1 </s> symbool als N > 1.
	// Berekent waarschijnlijkheid cf J&M2009: P(W) = PRODUCT_N...M P(w_n | w_(n-N+1)...w_(n-1))
	// ik neem aan dat als het eerste item in een sentence voor ngrammen n > 1
	// gelijk is aan "<s>", 'sentence' een array is die als is voorbewerkt.
	// returnt de log waarschijnlijkheid, om underflow te voorkomen
	public double ProbabilityOfSentence_Unsmoothed( String[] sentence )
	{
		double resultProb = 0;
		
		if ( numberOfWords > 1 && !sentence[0].equals("<s>") )
		{
			String[] sentence_tmp = new String[sentence.length + numberOfWords];
			for ( int i = 0; i < numberOfWords - 1; i++ )
				sentence_tmp[i] = "<s>";
			for ( int i = 0; i < sentence.length; i++ )
				sentence_tmp[i + numberOfWords - 1] = sentence[i];
			sentence_tmp[sentence.length + numberOfWords - 1] = "</s>";
			sentence = sentence_tmp;
		}
		
		for ( int i = 0; i < sentence.length - numberOfWords; i++ )
		{
			// aanpassen met log
			String[] tmp = Arrays.copyOfRange(sentence, i, i + numberOfWords);
			resultProb += Math.log(ProbabilityOfNGram_Unsmoothed(tmp));
		}
		
		return resultProb;
	} // ProbabilityOfSentence_Unsmoothed
	
	// neemt een string als zin en voegt de begin en eindsymbolen toe
	public double ProbabilityOfSentence_Unsmoothed( String sentence )
	{
		for ( int i = 0; i < numberOfWords - 1; i++ )
			sentence = "<s> " + sentence;
		if ( numberOfWords > 1 )
			sentence += " </s>";
		return ProbabilityOfSentence_Unsmoothed( StringToArray(sentence) );
	}
	
	private void sortList()
	{
		if ( sortedNGrams == null )
			sortedNGrams = new ArrayList<NGramItem>();
		else
			sortedNGrams.clear();
		
		Iterator<String> it = keySet().iterator();
		while ( it.hasNext() )
		{
			String x = it.next();
			sortedNGrams.add(new NGramItem(x, get(x)));
		}
	}
	
	// overidet put om te zorgen dat er 'van buitenaf' niets in de hashmap
	// kan worden gestopt
	public Integer put( String key, Integer value )
	{
		return 0;
	}
	
	public int getNumberOfWords()
	{
		return numberOfWords;
	}
	
	public ArrayList<NGramItem> getSortedList( boolean higher )
	{
		if ( higher )
		{
			Collections.sort(sortedNGrams);
			return sortedNGrams;
		}
		else
			return lowerNGram.getSortedList(true);
	}
	
	// returnt een arraylist met woorden waarvoor P(w | input) > 0.
	// input = numberOfWords - 1 woorden. Als input = numberOfWords -2
	// woorden lang dan wordt lowerNGram geprobeerd, indien die niet
	// null is.
	public ArrayList<NGramItem> MostLikelyFullString( String input )
	{
		int n = StringToArray(input).length;
		if ( n == numberOfWords - 2 && lowerNGram != null )
			return lowerNGram.MostLikelyFullString( input );
		else if ( n != numberOfWords - 1 ) return null;
		 
		ArrayList<NGramItem> tmp = new ArrayList<NGramItem>();
		Iterator<String> it = keySet().iterator();
		boolean found = false;
		while ( it.hasNext() )
		{
			String tt = it.next();
			if ( tt.startsWith(input) )
			{
				found = true;
				tmp.add( new NGramItem(tt, get(tt)) );
			}
		}
		return found ? tmp : null;
	}
	
} // einde van class NGramTable
