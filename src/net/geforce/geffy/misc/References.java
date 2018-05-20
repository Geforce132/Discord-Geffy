package net.geforce.geffy.misc;

import java.util.HashMap;
import java.util.Set;

import net.geforce.geffy.commands.twitter.Tweet;
import net.geforce.geffy.commands.twitter.TwitterUser;
import net.geforce.geffy.interactions.Prompt;
import net.geforce.geffy.interactions.Prompt.PromptType;
import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

public class References {
	
	public static final String COMMAND_PREFIX = "~";
	
	// Scoreboards
	private HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();
	
	// Actively running prompts
	private HashMap<IUser, Prompt> activePrompts = new HashMap<IUser, Prompt>();
	
	// HashMaps used for the ~tweet command
	private HashMap<IUser, TwitterUser> twitterUsers = new HashMap<IUser, TwitterUser>();
	private HashMap<Long, Tweet> tweetsFollowedByGeffy = new HashMap<Long, Tweet>();
	private HashMap<IUser, IMessage> usersLastMessage = new HashMap<IUser, IMessage>();

	
	public boolean createNewScoreboard(String sbName)
	{
		if(sbName.isEmpty()) return false;
		
		scoreboards.put(sbName, new Scoreboard());
		return true;
	}
	
	public Scoreboard getScoreboard(String sbName)
	{
		return scoreboards.get(sbName);
	}
	
	public void setPromptForUser(Prompt prompt)
	{
		if(prompt.getType() == PromptType.MESSAGE)
		{
			prompt.getChannel().sendMessage(prompt.getPromptMessage());
		}
		else if(prompt.getType() == PromptType.DM)
		{
			prompt.getPromptedUser().getOrCreatePMChannel().sendMessage(prompt.getPromptMessage());
		}
		
		activePrompts.put(prompt.getPromptedUser(), prompt);
	}
	
	public Prompt getPromptForUser(IUser user)
	{
		return activePrompts.get(user);
	}
	
	public Set<IUser> getPromptedUsers()
	{
		return activePrompts.keySet();
	}
	
	public boolean isUserAwaitingPrompt(IUser user)
	{
		return activePrompts.containsKey(user);
	}
	
	public void removePromptForUser(IUser user)
	{
		activePrompts.remove(user);
	}
	
	public void createTwitterInstanceForUser(IUser user) 
	{		
		TwitterUser tu = new TwitterUser(user);
		
		Twitter twitter = tu.getTwitterInstance();
		
	    try {
			RequestToken requestToken = twitter.getOAuthRequestToken();
		    tu.setRequestToken(requestToken);
			Utils.sendMessage(user, requestToken.getAuthenticationURL());
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	    
		twitterUsers.put(user, tu);
	}

	public TwitterUser getTwitterInstanceForUser(IUser user)
	{
		if(!twitterUsers.containsKey(user)) return null;
		
		return twitterUsers.get(user);
	}
	
	public TwitterUser getTwitterAuthenticatorForUser(IUser user)
	{
		if(!twitterUsers.containsKey(user)) return null;
		
		return twitterUsers.get(user);
	}
	
	public void addFollowedTweet(long messageID, IUser user, Status tweet)
	{		
		tweetsFollowedByGeffy.put(messageID, new Tweet(user, tweet));
	}
	
	public Tweet getTweetFromMessage(long message)
	{
		if(!tweetsFollowedByGeffy.containsKey(message)) return null;
		
		return tweetsFollowedByGeffy.get(message);
	}
	
	public void setLastMessage(IUser user, IMessage message)
	{
		usersLastMessage.put(user, message);
	}
	
	public IMessage getLastMessage(IUser user)
	{
		return usersLastMessage.get(user);
	}
}
