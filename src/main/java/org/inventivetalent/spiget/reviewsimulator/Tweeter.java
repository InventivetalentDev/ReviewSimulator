package org.inventivetalent.spiget.reviewsimulator;

import com.google.gson.JsonObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class Tweeter {

	private Twitter twitter;

	public Tweeter(JsonObject config) {
		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(config.get("consumer_key").getAsString(), config.get("consumer_secret").getAsString());
		twitter.setOAuthAccessToken(new AccessToken(config.get("access_token").getAsString(), config.get("access_token_secret").getAsString()));
	}

	void postTweet(String text) {
		try {
			twitter.updateStatus(text);
		} catch (TwitterException e) {
			throw new RuntimeException("Failed to post tweet", e);
		}
	}

}
