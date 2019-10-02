package net.geforce.geffy.misc;

import java.util.HashMap;
import java.util.Set;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import net.geforce.geffy.commands.twitter.Tweet;
import net.geforce.geffy.commands.twitter.TwitterUser;
import net.geforce.geffy.interactions.Prompt;
import net.geforce.geffy.interactions.Prompt.PromptType;
import net.geforce.geffy.main.Utils;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

public class References {
	
	public static final String COMMAND_PREFIX = "~";
	
	// Scoreboards
	private HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();
	
	// Actively running prompts
	private HashMap<User, Prompt> activePrompts = new HashMap<User, Prompt>();
	
	// HashMaps used for the ~tweet command
	private HashMap<User, TwitterUser> twitterUsers = new HashMap<User, TwitterUser>();
	private HashMap<Long, Tweet> tweetsFollowedByGeffy = new HashMap<Long, Tweet>();
	private HashMap<User, Message> usersLastMessage = new HashMap<User, Message>();

	
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
	
	public Prompt getPromptForUser(User user)
	{
		return activePrompts.get(user);
	}
	
	public Set<User> getPromptedUsers()
	{
		return activePrompts.keySet();
	}
	
	public boolean isUserAwaitingPrompt(User user)
	{
		return activePrompts.containsKey(user);
	}
	
	public void removePromptForUser(User user)
	{
		activePrompts.remove(user);
	}
	
	public void createTwitterInstanceForUser(User user) 
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

	public TwitterUser getTwitterInstanceForUser(User user)
	{
		if(!twitterUsers.containsKey(user)) return null;
		
		return twitterUsers.get(user);
	}
	
	public TwitterUser getTwitterAuthenticatorForUser(User user)
	{
		if(!twitterUsers.containsKey(user)) return null;
		
		return twitterUsers.get(user);
	}
	
	public void addFollowedTweet(long messageID, User user, Status tweet)
	{		
		tweetsFollowedByGeffy.put(messageID, new Tweet(user, tweet));
	}
	
	public Tweet getTweetFromMessage(long message)
	{
		if(!tweetsFollowedByGeffy.containsKey(message)) return null;
		
		return tweetsFollowedByGeffy.get(message);
	}
	
	public void setLastMessage(User user, Message message)
	{
		usersLastMessage.put(user, message);
	}
	
	public Message getLastMessage(User user)
	{
		return usersLastMessage.get(user);
	}
}
