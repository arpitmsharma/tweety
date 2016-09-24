package dev.cdc.service.text;

public class TweetCleaner {
	private static String patternUrl = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
	private static String patternMention = "@\\w+";

	public static String cleanText(String text) {
		return text.replaceAll(patternMention, " ").replaceAll(patternUrl, " ");
	}
}
