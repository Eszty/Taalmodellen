/* Program for extracting ngrams and counting word frequencies
Eszter Fodor & Sharon Gieske
*/

/* TODO:
	- INPUT: file (ovis-trainset.txt) + integers (n => ngrams, m => m most frequent sequences) 
	- read file
	- search for the ngrams
	- count each sequence's frequency
	- return the m most frequence sequence
*/ 

import java.util.*;
import java.io.*;

public class Extracting 
{

	//Create a string list from input string
	public static List<String> ngrams(int n, String str) 
	{
		List<String> ngrams = new ArrayList<String>();
		String[] words = str.split(" ");

		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i+n));

		return ngrams;
	}

	//Create a string from the input words
	public static String concat(String[] words, int start, int end) 
	{
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);
		return sb.toString();
	}

	public static Map<String, Integer> hashmap(String str)
	{
		Map<String, Integer> hashmap = new HashMap<String, Integer>();
		
		/*
		- read line (TextIO) => string
		- put string in hasmap
		- val +1
		- if string already exists only increase val
		*/
		
		if(hashmap.containsKey(str))
		{
			Integer val = hashmap.get(str);
			val += 1;
			hashmap.put(str, val);
		}
		else
			hashmap.put(str, 1);

		return hashmap;
		
	}

	public static void main(String[] args)
	{

		Map<String, Integer> hashmap = new HashMap<String, Integer>();
		
		//Get values for n and m
		Scanner sc = new Scanner(System.in);
		System.out.println("n: ");
		int n = sc.nextInt();
		/*System.out.println("m: ");
		int m = sc.nextInt();*/

		try
		{
			TextIO.readFile("ovis-trainset.txt"); //reads the file 
			System.out.printf("done\n");
			
			TextIO.writeFile("output.txt");
				
			while(!TextIO.eof())
			{
				String word = TextIO.getln();
				for (String ngram : ngrams(n, word))
					TextIO.putln(ngram); //writes ngrams to file
				//TextIO.skipBlanks();
			}

			//TODO: use hashmap for storing ngrams and their frequencies

			TextIO.readFile("output.txt");
			
			TextIO.writeFile("hash.txt");

			while(!TextIO.eof())
			{
				String string = TextIO.getln();

				if(hashmap.containsKey(string))
				{
					Integer val = hashmap.get(string);
					val += 1;
					hashmap.put(string, val);
				}
				else
					hashmap.put(string, 1);


			}
			
			TextIO.putln(hashmap);
		}
		catch (Exception e)
		{
			System.out.println("An error occured");
		}
							
	}



}
















