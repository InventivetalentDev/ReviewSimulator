package org.inventivetalent.spiget.reviewsimulator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	private static File in            = new File("reviews.json");
	private static File twitterConfig = new File("twitter.json");

	public static void main(String[] args) throws IOException {
		if (!in.exists()) {
			System.err.println("Missing input file");
			System.exit(-1);
			return;
		}

		Simulator simulator = new Simulator();
		simulator.init(in);

		int LENGTH_MAX = 50;
		int LENGTH_MIN = 5;

		String review = "";
		int tries = 0;
		while (review.isEmpty() || review.length() > 140) {
			review = simulator.makeReview((int) (Math.floor(Math.random() * (LENGTH_MAX - LENGTH_MIN)) + LENGTH_MIN));
			tries++;
		}
		System.out.println(tries + " tries");
		System.out.println(review);

		// Twitter
		JsonObject config;
		try (FileReader reader = new FileReader(twitterConfig)) {
			config = new JsonParser().parse(reader).getAsJsonObject();
		}
		Tweeter tweeter = new Tweeter(config);

		tweeter.postTweet(review);
	}

}


