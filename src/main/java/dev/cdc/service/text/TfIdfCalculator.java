package dev.cdc.service.text;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TfIdfCalculator {

	/**
	 * @param doc
	 *            list of strings
	 * @param term
	 *            String represents a term
	 * @return term frequency of term in document
	 */
	public static double tf(List<String> doc, String term) {
		double count = 0;
		for (String word : doc) {
			if (term.equalsIgnoreCase(word))
				count++;
		}
		return count / doc.size();
	}

	/**
	 * @param docs
	 *            list of list of strings represents the dataset
	 * @param term
	 *            String represents a term
	 * @return the inverse term frequency of term in documents
	 */
	public static double idf(List<List<String>> docs, String term) {
		double count = 0;
		for (List<String> doc : docs) {
			for (String word : doc) {
				if (term.equalsIgnoreCase(word)) {
					count++;
					break;
				}
			}
		}
		return Math.log(docs.size() / count);
	}

	/**
	 * @param doc
	 *            a text document
	 * @param docs
	 *            all documents
	 * @param term
	 *            term
	 * @return the TF-IDF of term
	 */
	public static double tfIdf(List<String> doc, List<List<String>> docs, String term) {
		return tf(doc, term) * idf(docs, term);

	}

	public HashMap<String, TfIdfValuePair> getTfIdfVectorMap(List<String> doc1, List<String> doc2,
			List<List<String>> documents) {

		TfIdfCalculator calculator = this;

		HashMap<String, TfIdfValuePair> map = new HashMap<String, TfIdfValuePair>(doc1.size());
		for (String word : doc1) {
			double tfidfDoc1 = calculator.tfIdf(doc1, documents, word);
			double tfidfDoc2 = calculator.tfIdf(doc2, documents, word);
			map.put(word, new TfIdfValuePair(tfidfDoc1, tfidfDoc2));
		}
		for (String word : doc2) {
			double tfidfDoc1 = calculator.tfIdf(doc1, documents, word);
			double tfidfDoc2 = calculator.tfIdf(doc2, documents, word);
			map.put(word, new TfIdfValuePair(tfidfDoc1, tfidfDoc2));
		}

		return map;
	}
}
