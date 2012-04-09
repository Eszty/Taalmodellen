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


	public static List<String> ngrams(int n, String str) 
	{
		List<String> ngrams = new ArrayList<String>();
		String[] words = str.split(" ");

		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i+n));

		return ngrams;
	}

	public static String concat(String[] words, int start, int end) 
	{
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);
		return sb.toString();
	}



	public static void main(String[] args)
	{
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
			
			for(int i = 0; i<1; i++)
			{
				String word = TextIO.getln();
				for (String ngram : ngrams(n, word))
					System.out.println(ngram);
				//TextIO.skipBlanks();
			}
		}
		catch (Exception e)
		{
			System.out.println("fail");
		}
							
	}



}
















