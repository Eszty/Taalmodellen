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

public class Extracting 
{
	public static void main(String[] args)
	{
		try
		{
			TextIO.readFile("ovis-trainset.txt"); //reads in the file 
			System.out.printf("done");
		}
		catch (Exception e)
		{
			System.out.println("fail");
		}
							
	}
}
















