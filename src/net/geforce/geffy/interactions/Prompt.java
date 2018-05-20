package net.geforce.geffy.interactions;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public abstract class Prompt {
	
	/**
	 * The prompt's message.
	 */
	private String prompt;
	
	/**
	 * The user that has been prompted.
	 */
	private IUser userPrompted;
	
	/**
	 * The channel that the user has been prompted in (or the DM in the
	 * case of a private conversation).
	 */
	private IChannel channelPromptedIn;
	
	/**
	 * The {@link PromptType} of this prompt. Can either be MESSAGE or DM.
	 */
	private PromptType promptType;
	
	/**
	 * The response that this prompt should send back to the user once it has
	 * received their reply.
	 */
	private String response;
	
	public Prompt(String message, IUser user)
	{
		prompt = message;
		userPrompted = user;
		promptType = PromptType.DM;
	}
	
	public Prompt(String message, IUser user, IChannel channel)
	{
		prompt = message;
		userPrompted = user;
		channelPromptedIn = channel;
		promptType = PromptType.MESSAGE;
	}
	
	/**
	 * An abstract method which is called once from {@link EventHandler} whenever
	 * a user sends a message after being prompted.
	 * 
	 * @param reply The message that the user has sent.
	 */
	public abstract void completePrompt(String reply);
	
	/**
	 * @return The message of this prompt.
	 */
	public String getPromptMessage()
	{
		return prompt;
	}
	
	/**
	 * @return The user that has been prompted.
	 */
	public IUser getPromptedUser()
	{
		return userPrompted;
	}
	
	/**
	 * @return The channel that the user has been prompted in.
	 */
	public IChannel getChannel()
	{
		return channelPromptedIn;
	}
	
	/**
	 * Sets the response of this prompt.
	 * 
	 * @param reply The response this prompt should use.
	 */
	public void setResponse(String reply)
	{
		response = reply;
	}
	
	/**
	 * @return The response of this prompt.
	 */
	public String getResponse()
	{
		return response;
	}
	
	/**
	 * @return The {@link PromptType} of this prompt.
	 */
	public PromptType getType()
	{
		return promptType;
	}
	
	/**
	 * A simple enum which keeps track of whether this prompt is expecting a response
	 * from a channel message or DM.
	 * 
	 * @author Geforce
	 */
	public enum PromptType 
	{
		MESSAGE,
		DM;
	}

}
