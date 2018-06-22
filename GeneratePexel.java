/**
 * @author Grace Chen
 * 6/21/18
 *
 * How to run: java GeneratePexel.java <filename>
 * Strips out more common words from the given text file, generates a stock 
 * image from Pexels for 0-20% of the most important words, and downloads the 
 * stock images (with .jpg extension) to the user's computer under /pexel_imgs.
 * Takes about 3 minutes to process 1k words.
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.net.*;

public class GeneratePexel {
	public static void main (String args[]) throws IOException{

		// command line check
		if (args.length != 1) {
			System.err.println("Please give a file to read from.");
			return;
		}

		Random random = new Random();
		ArrayList<String> impWords = getImportantWords(args[0]);
		ArrayList<String> imageURLs = new ArrayList<String>();
		ArrayList<String> imageText = new ArrayList<String>();

		System.out.println("Generating image URLs...Please be patient.");

		// call python script and get image URL
		for (int i = 0; i < impWords.size(); i++) {
			String s = null;

			// 20% chance of generating image
			if (random.nextDouble() < 0.2) {
				Process p = Runtime.getRuntime().exec("python3 pexel.py " + 
						impWords.get(i));
				BufferedReader stdInput = new BufferedReader(new 
						InputStreamReader(p.getInputStream()));

				// read the output from the command
				while ((s = stdInput.readLine()) != null) {
					if (!s.equals("Skipping")) {					
						// System.out.println(s); // for debugging
						imageURLs.add(s);
						imageText.add(impWords.get(i));
					}
				}
			}
		}

		System.out.println("Saving images to /pexel_imgs...");

		// save images to user's computer
		try {

			File dir = new File("pexel_imgs");

			// directory check
			if (!dir.exists()) dir.mkdir(); 
			else {
				// Comment this part out if you want to keep old images.
				// clear pexel_imgs directory
				for(File file: dir.listFiles()) 
					if (!file.isDirectory()) file.delete();
			}

			// save each image to pexel_imgs directory
			for (int i = 0; i < imageURLs.size(); i++) {

				URL url = new URL(imageURLs.get(i));

				URLConnection conn = url.openConnection();
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 " + 
						"(Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML," + 
						"like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				InputStream in = conn.getInputStream();

				Files.copy(in, Paths.get("pexel_imgs/" + i + "_" + 
							imageText.get(i) + ".jpg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("All done!");
		System.exit(0);
	}

	/**
	 * Generates a list of main words from a given text file. Not foolproof
	 * since list of omitted words is hard-coded.
	 *
	 * @param filename  name of text file to be processed.
	 * @return list of words from text file without more common words
	 */
	private static ArrayList<String> getImportantWords(String filename) {

		String content;
		ArrayList<String> impWordList = new ArrayList<String>();
		String[] commonWords = new String[]{ "a", "an", "and", "or", "on", 
			"in", "of", "if", "to", "too", "the", "for", "any", "why", "what", 
			"where", "when", "who", "were", "was", "by", "can", "can't", 
			"cannot", "will", "would", "wouldn't", "should", "shouldn't", 
			"could", "couldn't", "quite",  "ever", "it's", "it", "its", "it,", 
			"is", "isn't",  "which", "been", "just", "some", "lot", "not", 
			"really", "with", "be", "am", "next", "as", "though", "enough",  
			"stood", "i", "them", "them.", "they", "they're", "their", "her", 
			"him", "she", "he", "he'll", "she'll", "he's", "she's", "my", 
			"your", "had", "other", "that",  "each", "of", "didn't", "almost",
			"but", "very", "said", "don't", "went", "squidgy", "wasn't", "let",
			"into",  "own", "until",  "are", "yet", "both",  "there", "here",
			"meant", "nothing", "found", "beside", "met"};

		// get words from file
		try {
			content = new String(Files.readAllBytes(Paths.get(filename)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Scanner scan = new Scanner(content);

		// strip out all common words and save to arraylist
		while (scan.hasNext()) {
			impWordList.add(scan.next().toLowerCase());
		}

		for (int j = 0; j < impWordList.size(); j++) {
			for (int i = 0; i < commonWords.length; i++) {
				if (impWordList.contains(commonWords[i])) {
					impWordList.remove(commonWords[i]);
				}
			}
		}

		// return array of important words
		return impWordList;
	}

}
