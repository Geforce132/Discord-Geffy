package net.geforce.geffy.commands.twitter;

/**
 * This class contains status codes thrown by {@link TwitterException}.
 * 
 * @author Geforce
 */
public class StatusCodes {
	
	/**
	 * This status code is thrown when Twitter refuses to complete a request by Geffy.
	 * Usually do to being rate limited, see RATE_LIMITED and {@link ErrorCodes}.
	 */
	public static final int FORBIDDEN = 403;
	
	/**
	 * This status code is thrown when Geffy cannot find a resource needed to complete
	 * a request, usually a tweet after it has been deleted by the author.
	 */
	public static final int NOT_FOUND = 404;
	
	/**
	 * This status code is thrown when Twitter is rate limiting Geffy due to an excess of
	 * requests.
	 */
	public static final int RATE_LIMITED = 420;
	
	/**
	 * This status code is thrown when Twitter is down or is having API issues.
	 */
	public static final int SERVICE_UNAVAILABLE = 502;
	
}
