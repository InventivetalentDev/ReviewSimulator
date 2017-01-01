package org.inventivetalent.spiget.reviewsimulator;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 *  Markov-Chains text generator modified for Java, based on http://www.soliantconsulting.com/blog/2013/02/title-generator-using-markov-chains
 */
class Simulator {

	private Set<String>               reviews    = new HashSet<>();
	private Map<String, Boolean>      terminals  = new HashMap<>();
	private List<String>              startWords = new ArrayList<>();
	private Map<String, List<String>> wordStats  = new HashMap<>();

	void init(File in) throws IOException {
		try (FileReader reader = new FileReader(in)) {
			JsonArray reviewsArray = (JsonArray) new JsonParser().parse(reader);
			reviewsArray.forEach(jsonElement -> reviews.add(jsonElement.getAsString()));
		}

		for (String review : reviews) {
			String[] words = review.split(" ");
			terminals.put(words[words.length - 1], true);
			startWords.add(words[0]);
			for (int i = 0; i < words.length - 1; i++) {
				if (wordStats.containsKey(words[i])) {
					wordStats.get(words[i]).add(words[i + 1]);
				} else {
					wordStats.put(words[i], new ArrayList<>(Collections.singleton(words[i + 1])));
				}
			}
		}
	}

	private String choice(List<String> a) {
		int i = (int) Math.floor(a.size() * Math.random());
		return a.get(i);
	}

	String makeReview(int minLength) {
		String word = choice(startWords);
		List<String> review = new ArrayList<>(Collections.singleton(word));
		while (wordStats.containsKey(word)) {
			List<String> nextWords = wordStats.get(word);
			word = choice(nextWords);
			review.add(word);
			if (review.size() > minLength && terminals.containsKey(word)) { break; }
		}
		if (review.size() < minLength) { return makeReview(minLength); }

		StringJoiner stringJoiner = new StringJoiner(" ");
		review.forEach(stringJoiner::add);
		return stringJoiner.toString();
	}

}
