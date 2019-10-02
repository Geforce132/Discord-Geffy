package net.geforce.geffy.handlers;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.event.domain.message.ReactionRemoveEvent;
import discord4j.core.object.entity.User;
import me.philippheuer.twitch4j.events.EventSubscriber;
import net.geforce.geffy.commands.Command;
import net.geforce.geffy.commands.CommandAdmin;
import net.geforce.geffy.commands.CommandGiveCookie;
import net.geforce.geffy.commands.CommandHelp;
import net.geforce.geffy.commands.CommandRate;
import net.geforce.geffy.commands.CommandSCLinks;
import net.geforce.geffy.commands.CommandSteamAnalyst;
import net.geforce.geffy.commands.CommandTweet;
import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import net.geforce.geffy.misc.References;

/**
 * Geffy's handling of Discord4j events.
 * 
 * @author Geforce
 */
public class EventHandler {
	
	/**
	 * A simple array containing instances of the commands he should check for and execute.
	 */
	private Command[] commands = new Command[]{new CommandGiveCookie(), new CommandRate(), new CommandTweet(), new CommandAdmin(), new CommandHelp(), new CommandSteamAnalyst(), new CommandSCLinks()};
	
	/**
	 * Handles the event of a Discord user posting a message to a channel.
	 * 
	 * @param event The event being passed by Discord4j.
	 */
	public void onMessageReceived(MessageCreateEvent event)
	{		
		String[] message = event.getMessage().getContent().get().split(" ");

		if(!event.getMessage().getContent().get().startsWith(References.COMMAND_PREFIX))
		{
			User author = event.getMessage().getAuthor().get();
						
			String reply = Utils.arrayToString(message);
			
			for(User user : Geffy.getReferences().getPromptedUsers())
			{
				if(author.getId() == user.getId())
				{
					Geffy.getReferences().getPromptForUser(author).completePrompt(reply);
					Geffy.getReferences().removePromptForUser(author);
				}
			}
		}
		
		try {
			for(Command<MessageCreateEvent> command : commands)
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
		
		//if(!event.getMessage().getChannel().block().) Geffy.getReferences().setLastMessage(event.getAuthor(), event.getMessage());
	}
	
	@EventSubscriber
	public void onReactionAdded(ReactionAddEvent event)
	{
		if(event.getUser().block().isBot() && event.getUser().block().getUsername().matches(Geffy.getUsername())) return;
		
		try {
			for(Command<ReactionAddEvent> command : commands)
			{
				if(command.reactsToReactions())
					command.reactionAdded(event, event.getUser().block(), event.getMessage().block());
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
		if(event.getUser().block().isBot() && event.getUser().block().getUsername().matches(Geffy.getUsername())) return;
		
		try {
			for(Command<ReactionRemoveEvent> command : commands)
			{
				if(command.reactsToReactions())
					command.reactionRemoved(event, event.getUser().block(), event.getMessage().block());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

}
