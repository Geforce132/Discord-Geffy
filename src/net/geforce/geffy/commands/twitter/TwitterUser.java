package net.geforce.geffy.commands.twitter;

import java.util.ArrayList;
import java.util.HashMap;

import net.geforce.geffy.interactions.PromptTwitterPIN;
import net.geforce.geffy.misc.Passwords;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * This class contains necessary info needed for a user to be able to use Geffy's
 * tweet command, including the user's {@link RequestToken} and {@link AccessToken}.
 * 
 * @author Geforce
 */
public class TwitterUser {

	/**
	 * The {@link IUser} object of this Twitter instance.
	 */
	private IUser user;
	
	/**
	 * The {@link Twitter} instance of this user.
	 */
	private Twitter twitterInstance;
	
	/**
	 * The RequestToken for this specific user.
	 */
	private RequestToken requestToken;

	/**
	 * An ArrayList containing all the tweets sent by this user.
	 */
	private ArrayList<Status> tweetsSentByUser = new ArrayList<Status>();
	
	/**
	 * A HashMap containing all the tweets this user has retweeted, found by
	 * using an {@link IMessage}s long ID. This is needed to make up for the
	 * fact that Twitter4j doesn't have a "unretweetStatus()" method. :/
	 */
	private HashMap<Long, Status> retweetedTweets = new HashMap<Long, Status>();
	
	/**
	 * An ArrayList containing all the tweets this user is following; namely, tweets
	 * from Geffy that this user has retweeted/liked using a reaction.
	 */
	private ArrayList<Status> followedTweets = new ArrayList<Status>();
	
	public TwitterUser(IUser iuser)
	{
		user = iuser;
	}
	
	/**
	 * @return The authenticated {@link Twitter} instance for this user. If it doesn't
	 * exist, this method will generate one before returning.
	 */
	public Twitter getTwitterInstance()
	{
		if(twitterInstance == null)
		{
			Twitter twitter = new TwitterFactory().getInstance();
			
			twitter.setOAuthConsumer(Passwords.CONSUMER_KEY, Passwords.CONSUMER_SECRET);
			twitterInstance = twitter;
		}
		
		return twitterInstance;
	}
	
	/**
	 * @return The Twitter username of this user.
	 * @throws TwitterException throws an exception if the user cannot be found.
	 */
	public String getTwitterUsername()
	{
		if(twitterInstance == null) return null;
		
		try {
			return twitterInstance.getScreenName();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return The {@link IUser} object of this user.
	 */
	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}
	
	public ArrayList<Status> getTweetsSent() {
		return tweetsSentByUser;
	}
	
	public void addSentTweet(Status tweet) {
		tweetsSentByUser.add(tweet);
	}
	
	public ArrayList<Status> getTweetsFollowed() {
		return followedTweets;
	}
	
	public void addFollowedTweet(Status tweet) {
		followedTweets.add(tweet);
	}
	
	public HashMap<Long, Status> getRetweetedTweets() {
		return retweetedTweets;
	}
	
	public void addRetweetedTweet(IMessage message, Status tweet) {
		retweetedTweets.put(message.getLongID(), tweet);
	}
	
	public void removeRetweetedTweet(IMessage message) {
		retweetedTweets.remove(message.getLongID());
	}
	
	public void setRequestToken(RequestToken token) {
		requestToken = token;
	}

	/**
	 * Sets the access token of this user after they reply to {@link PromptTwitterPIN}
	 * with their PIN code.
	 * 
	 * @param pin The user's PIN code.
	 * @throws TwitterException If the PIN code is invalid.
	 */
	public void setAccessToken(String pin) throws TwitterException {
		AccessToken access = twitterInstance.getOAuthAccessToken(requestToken, pin);
	}
	
}
