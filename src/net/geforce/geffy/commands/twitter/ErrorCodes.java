package net.geforce.geffy.commands.twitter;

/**
 * This class contains status-specific error codes that are thrown from a
 * specific {@link StatusCodes}.
 * 
 * @author Geforce
 */
public class ErrorCodes {
	
	/**
	 * This error code accompanies {@link StatusCodes}.FORBIDDEN and is thrown
	 * when Geffy tries to send a tweet without a message, or if there are only
	 * special unicode characters in the message.
	 */
	public static final int MISSING_MESSAGE = 170;
	
	/**
	 * This error code accompanies {@link StatusCodes}.FORBIDDEN and is
	 * thrown when Geffy tries to send a tweet that exceeds Twitter's character limit.
	 */
	public static final int TWEET_EXCEEDS_CHARACTER_LIMIT = 186;

}
