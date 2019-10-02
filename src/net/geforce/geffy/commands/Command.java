package net.geforce.geffy.commands;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

/**
 * An abstract class which acts as the base for all of Geffy's commands. Contains required
 * methods and variables.
 * 
 * @author Geforce
 *
 * @param <M> The class of the Event that the command will respond to. Usually MessageReceivedEvent.
 */
public abstract class Command<M> {
	
	/**
	 * Runs whenever the event occurs.
	 * 
	 * @param event The event that triggered this command.
	 * @param args The arguments that the user included with the command.
	 * @throws Exception Throws
	 */
	public abstract void execute(M event, String[] args) throws Exception;
	
	public void reactionAdded(ReactionEmoji reaction, User user, Message message) {}
	
	public void reactionRemoved(ReactionEmoji reaction, User user, Message message) {}

	public boolean reactsToReactions()
	{
		return false;
	}

	public abstract String[] getAliases();
}
