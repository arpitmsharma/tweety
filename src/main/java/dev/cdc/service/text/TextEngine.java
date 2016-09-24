package dev.cdc.service.text;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TextEngine {

	public static List<String> preprocessText(String text) {
		String cleanText = TweetCleaner.cleanText(text);
		List<String> words = StopWordRemover.removeStopWords(text);
		List<String> stems = Stemmer.stem(words);
		return stems;
	}

}
