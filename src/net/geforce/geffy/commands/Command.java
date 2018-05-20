package net.geforce.geffy.commands;

import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;

/**
 * An abstract class which acts as the base for all of Geffy's commands. Contains required
 * methods and variables.
 * 
 * @author Geforce
 *
 * @param <M> The class of the Event that the command will respond to. Usually MessageReceivedEvent.
 */
public abstract class Command<M extends Event> {
	
	/**
	 * Runs whenever the event occurs.
	 * 
	 * @param event The event that triggered this command.
	 * @param args The arguments that the user included with the command.
	 * @throws Exception Throws
	 */
	public abstract void execute(M event, String[] args) throws Exception;
	
	public void reactionAdded(IReaction reaction, IUser user, IChannel channel) {}
	
	public void reactionRemoved(IReaction reaction, IUser user, IChannel channel) {}

	public boolean reactsToReactions()
	{
		return false;
	}

	public abstract String[] getAliases();
}
