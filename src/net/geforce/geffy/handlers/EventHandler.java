package net.geforce.geffy.handlers;

import net.geforce.geffy.commands.Command;
import net.geforce.geffy.commands.CommandAdmin;
import net.geforce.geffy.commands.CommandGiveCookie;
import net.geforce.geffy.commands.CommandHelp;
import net.geforce.geffy.commands.CommandRate;
import net.geforce.geffy.commands.CommandSteamAnalyst;
import net.geforce.geffy.commands.CommandTweet;
import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import net.geforce.geffy.misc.References;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionRemoveEvent;
import sx.blah.discord.handle.obj.IUser;

/**
 * Geffy's handling of Discord4j events.
 * 
 * @author Geforce
 */
public class EventHandler {
	
	/**
	 * A simple array containing instances of the commands he should check for and execute.
	 */
	private Command[] commands = new Command[]{new CommandGiveCookie(), new CommandRate(), new CommandTweet(), new CommandAdmin(), new CommandHelp(), new CommandSteamAnalyst()};
	
	/**
	 * Handles the event of a Discord user posting a message to a channel.
	 * 
	 * @param event The event being passed by Discord4j.
	 */
	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event)
	{		
		String[] message = event.getMessage().getContent().split(" ");

		if(!event.getMessage().getContent().startsWith(References.COMMAND_PREFIX))
		{
			IUser author = event.getAuthor();
						
			String reply = Utils.arrayToString(message);
			
			for(IUser user : Geffy.getReferences().getPromptedUsers())
			{
				if(author.getLongID() == user.getLongID())
				{
					Geffy.getReferences().getPromptForUser(author).completePrompt(reply);
					Geffy.getReferences().removePromptForUser(author);
				}
			}
		}
		
		try {
			for(Command<MessageReceivedEvent> command : commands)
			{
				for(String alias : command.getAliases())
				{
					if(message[0].matches(References.COMMAND_PREFIX + alias))
					{
						String[] args = new String[message.length];
						
						for(int arg = 0; arg < args.length - 1; arg++)
						{
							args[arg] = message[arg + 1];
						}
						
						command.execute(event, args);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		if(!event.getChannel().isPrivate()) Geffy.getReferences().setLastMessage(event.getAuthor(), event.getMessage());
	}
	
	@EventSubscriber
	public void onReactionAdded(ReactionAddEvent event)
	{
		if(event.getUser().isBot() && event.getUser().getName().matches(Geffy.getUsername())) return;
		
		try {
			for(Command<ReactionAddEvent> command : commands)
			{
				if(command.reactsToReactions())
					command.reactionAdded(event.getReaction(), event.getUser(), event.getChannel());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
	
	@EventSubscriber
	public void onReactionRemoved(ReactionRemoveEvent event)
	{
		if(event.getUser().isBot() && event.getUser().getName().matches(Geffy.getUsername())) return;
		
		try {
			for(Command<ReactionAddEvent> command : commands)
			{
				if(command.reactsToReactions())
					command.reactionRemoved(event.getReaction(), event.getUser(), event.getChannel());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

}
