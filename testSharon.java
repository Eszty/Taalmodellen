import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public static void main(String[] args) throws IOException {

	 int n = Integer.parseInt(args[0]);
	 int m = Integer.parseInt(args[1]);
	 

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
				
	 }

		catch (Exception e)
		{
			System.out.println("An error occured");
		}
