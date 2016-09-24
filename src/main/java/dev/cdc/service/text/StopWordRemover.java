package dev.cdc.service.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class StopWordRemover {
	private static final String FILE_PATH = "/home/arpit/Files/stopwords.txt";

	private static List<String> stopWords = StopWordRemover.readStopWords();

	protected static boolean isStopWord(String word) {
		if (Collections.binarySearch(stopWords, word) >= 0) {
			return true;
		}
		return false;
	}

	private static List<String> readStopWords() {
		List<String> stopWords = new ArrayList<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(FILE_PATH));
			scanner.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!\"]+"));
			while (scanner.hasNext()) {
				stopWords.add(scanner.next().trim().toLowerCase());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return stopWords;
	}

	public static List<String> removeStopWords(String text) {
		List<String> words = new ArrayList<String>();
		words.addAll(Arrays.asList(text
				.replaceAll("[^A-Za-z0-9\\'\\-\\s\\t\\r\\n#$%^*()&]", " ")
				.toLowerCase().trim().split("[\\s]+")));
		Iterator<String> wordsItr = words.iterator();
		String word = null;
		while (wordsItr.hasNext()) {
			word = wordsItr.next();
			if (isStopWord(word)) {
				wordsItr.remove();
			}
		}
		return words;
	}
}
