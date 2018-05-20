package net.geforce.geffy.commands.twitter;

import sx.blah.discord.handle.obj.IUser;
import twitter4j.Status;

/**
 * A simple wrapper class that holds both a {@link Status} that a Discord user has posted
 * using ~tweet and the {@link IUser} object of the user who sent it.
 * 
 * @author Geforce
 */
public class Tweet {
	
	/**
	 * The author of the tweet.
	 */
	private IUser author;
	
	/**
	 * The tweet itself.
	 */
	private Status status;
	
	public Tweet(IUser user, Status tweet)
	{
		author = user;
		status = tweet;
	}
	
	/**
	 * @return The author of this tweet.
	 */
	public IUser getAuthor()
	{
		return author;
	}
	
	/**
	 * @return The {@link Status} object of this tweet.
	 */
	public Status getTweet()
	{
		return status;
	}

}
