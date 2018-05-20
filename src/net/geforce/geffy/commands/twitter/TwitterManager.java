package net.geforce.geffy.commands.twitter;

import net.geforce.geffy.interactions.PromptTwitterPIN;
import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;

/**
 * This class contains utility methods used by Geffy's tweet command, including the methods
 * to send and RT/favorite a tweet. Also handles any exceptions, creates the fancy embed used
 * in tweets' messages, and authenticates users.
 * 
 * @author Geforce
 */
public class TwitterManager {
	
	/**
	 * Posts a {@link Status} with the specified message and embeds to Twitter.
	 * 
	 * @param channel The channel the command was sent from.
	 * @param user The user who used the command.
	 * @param discordMessage The {@link IMessage} object of the command sent.
	 * @param tweetMessage The message to use in the tweet.
	 * @return A Status object of the tweet that was posted. May or may not be used.
	 * @throws Exception Throws an exception if any problems occur during the posting process.
	 */
	@SuppressWarnings("deprecation")
	public static Status sendTweet(IChannel channel, IUser user, IMessage discordMessage, String tweetMessage) throws Exception
	{
		String tempMessage = tweetMessage;
		IMessage uncheckedMessage = discordMessage;
		
		if(tweetMessage.matches("\\^"))
		{
			uncheckedMessage = Geffy.getReferences().getLastMessage(user);
			if(uncheckedMessage == null)
			{
				Utils.sendMessage(channel, "No previous message to tweet!");
				return null;
			}
			
			if(uncheckedMessage.getContent().startsWith("~tweet "))
			{
				tempMessage = uncheckedMessage.getContent().replace("~tweet ", "");
			}
			else
			{
				tempMessage = uncheckedMessage.getContent();	
			}			
		}
		
		if(tempMessage.contains("[a]")) tempMessage = tempMessage.replace("[a]", "@");
		
		Twitter twitter = Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance();

		long[] imageIDs = new long[discordMessage.getAttachments().size()];
		
		for(int i = 0; i < imageIDs.length; i++)
		{
			UploadedMedia mediaFile = twitter.uploadMedia(Utils.downloadImageFrom(discordMessage.getAttachments().get(i)));
			imageIDs[i] = mediaFile.getMediaId();
		}
		
		StatusUpdate statusUpdate = new StatusUpdate(tempMessage);
		
		if(imageIDs.length > 0)
			statusUpdate.setMediaIds(imageIDs);

		Status tweet = twitter.updateStatus(statusUpdate);
		
		IMessage tweetDMessage = Utils.sendMessageWithEmbed(channel, "Successfully posted new tweet!", TwitterManager.getFormattedEmbed(twitter, tweet).build());
		Geffy.getReferences().addFollowedTweet(tweetDMessage.getLongID(), user, tweet);

		tweetDMessage.addReaction(":repeat:");
		Thread.sleep(200);
		tweetDMessage.addReaction("❤");
		Thread.sleep(200);
		tweetDMessage.addReaction("🗑");	
		
		return tweet;
	}
	
	/**
	 * Retweets the specified tweet.
	 * 
	 * @param status The tweet to retweet.
	 * @param message The message that the tweet was first posted in by Geffy.
	 * @param user The user who retweeted the tweet.
	 * @return The {@link Status} object of the newly created retweeted-Status tweet.

	 * @throws TwitterException If any problems occur during the retweeting process.
	 */
	public static Status retweetTweet(Status status, IMessage message, IUser user) throws TwitterException
	{
		Status retweet = Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().retweetStatus(status.getId());
		Geffy.getReferences().getTwitterInstanceForUser(user).addRetweetedTweet(message, retweet);
		
		return retweet;
	}
	
	/**
	 * Unretweets the specified tweet.
	 * 
	 * @param message The message that the tweet was first posted in by Geffy.
	 * @param user The user who retweeted the tweet.
	 * @return The (destroyed) {@link Status} object of deleted tweet.

	 * @throws TwitterException If any problems occur during the unretweeting process.
	 */
	public static Status unretweetTweet(IMessage message, IUser user) throws TwitterException
	{
		if(Geffy.getReferences().getTwitterInstanceForUser(user).getRetweetedTweets().containsKey(message.getLongID()))
		{
			Status rtToBeDeleted = Geffy.getReferences().getTwitterInstanceForUser(user).getRetweetedTweets().get(message.getLongID());
			return Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().destroyStatus(rtToBeDeleted.getId());
		}
		
		return null;
	}
	
	/**
	 * Favorites the specified tweet.
	 * 
	 * @param status The tweet to favorite.
	 * @param message The message that the tweet was first posted in by Geffy.
	 * @param user The user who favorited the tweet.
	 * @return The {@link Status} object of favorited tweet.

	 * @throws TwitterException If any problems occur during the favoriting process.
	 */
	public static Status favoriteTweet(Status status, IMessage message, IUser user) throws TwitterException
	{
		return Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().createFavorite(status.getId());
	}
	
	/**
	 * Unfavorites the specified tweet.
	 * 
	 * @param status The tweet to unfavorite.
	 * @param message The message that the tweet was first posted in by Geffy.
	 * @param user The user who favorited the tweet.
	 * @return The {@link Status} object of unfavorited tweet.

	 * @throws TwitterException If any problems occur during the unfavoriting process.
	 */
	public static Status unfavoriteTweet(Status status, IMessage message, IUser user) throws TwitterException
	{
		return Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().destroyFavorite(status.getId());
	}
	
	/**
	 * Deletes the specified tweet.
	 * 
	 * @param status The tweet to delete.
	 * @param message The message that the tweet was first posted in by Geffy.
	 * @param user The user who deleted the tweet.
	 * @return The (destroyed) {@link Status} object of deleted tweet.

	 * @throws TwitterException If any problems occur while deleting the tweet.
	 */
	public static Status deleteTweet(Status status, IMessage message, IUser user) throws TwitterException
	{
		return Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().destroyStatus(status.getId());
	}
	
	/**
	 * Prompts the user to authenticate themselves using Twitter's API, and waits for their response.
	 * @param user The user to authenticate.
	 */
	public static void authenticateUser(IUser user)
	{
		Geffy.getReferences().setPromptForUser(new PromptTwitterPIN("In order to use this command, please login to Twitter using the following link, and reply with the PIN given:", user));
		Geffy.getReferences().createTwitterInstanceForUser(user);
	}
	
	/**
	 * @return A blank embed for Twitter-related messages. Contains the Twitter logo.
	 */
	public static EmbedBuilder getDefaultTwitterEmbed()
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.withColor(0, 132, 255);
		builder.withAuthorIcon("https://i.imgur.com/juuXVnH.png");
		
		return builder;
	}
	
	/**
	 * @return A Twitter-themed embed containing a message.
	 */
	public static EmbedBuilder getDefaultTwitterEmbed(String message)
	{
		EmbedBuilder builder = getDefaultTwitterEmbed();
		builder.withTitle(message);
		
		return builder;
	}
	
	/**
	 * Creates a filled-out embed for use when posting a tweet. Contains lots of useful
	 * info such as a link to the tweet, the RT/favorite count of the tweet, and the tweet itself.
	 * 
	 * @param twitter The {@link Twitter} instance of the user that's posting this tweet.
	 * @param tweet The {@link Status} being posted.
	 * @return A formatted embed.
	 * @throws TwitterException Throws an exception if there are any errors while collecting required info.
	 */
	public static EmbedBuilder getFormattedEmbed(Twitter twitter, Status tweet) throws TwitterException
	{
		EmbedBuilder builder = new EmbedBuilder();
		builder.withColor(0, 132, 255);
		
		builder.withTitle("New tweet posted!");
		builder.withAuthorName("@" + twitter.getScreenName());
		builder.withAuthorIcon("https://i.imgur.com/juuXVnH.png");
		builder.withAuthorUrl("https://twitter.com/" + twitter.getScreenName());
		
		builder.withDescription(tweet.getText());
		builder.appendField("Retweets:", tweet.getRetweetCount() + "", true);
		builder.appendField("Favorites:", tweet.getFavoriteCount() + "", true);
		builder.appendField("View tweet here:", "https://twitter.com/" + twitter.getScreenName() + "/status/" + tweet.getId(), false);
		
		return builder;
	}
	
	/**
	 * Processes {@link TwitterException}s and returns an useable error message.
	 * 
	 * @param e The exception to process.
	 * @return An error message.
	 */
	public static String handleTwitterException(TwitterException e)
	{
		String message = "";
		
		if(e.getStatusCode() == StatusCodes.NOT_FOUND)
			return "A Twitter resource needed to complete this action cannot be found.";
		else if(e.getStatusCode() == StatusCodes.SERVICE_UNAVAILABLE)
			return Geffy.getUsername() + " is being rate limited by Twitter. Please retry your action again in " + e.getRetryAfter() + " seconds.";
		else if(e.getStatusCode() == StatusCodes.FORBIDDEN)
		{
			message += Geffy.getUsername() + " is being prohibited by Twitter from performing this action. ";
			
			if(e.getErrorCode() == ErrorCodes.TWEET_EXCEEDS_CHARACTER_LIMIT)
				return message += "This message exceeds Twitter's 280 character limit. Please shorten your message.";
			
			if(e.getErrorCode() == ErrorCodes.MISSING_MESSAGE)
				return message += "You are missing a message for this tweet, or are using certain special Unicode characters.";
		}
		
		return "This is an unknown exception; please contact Geforce to get this error resolved.";
	}
	
}
