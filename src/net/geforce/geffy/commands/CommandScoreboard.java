package net.geforce.geffy.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;

public class CommandScoreboard extends Command<MessageCreateEvent> {

	@Override
	public void execute(MessageCreateEvent event, String[] args) throws Exception {
		if(args.length < 0)
		{
			Utils.sendMessage(event.getMessage().getChannel().block(), "Missing arguments!");
		}
		
		if(args.length >= 2)
		{
			if(args[0].matches("create"))
			{
				String sbName = Utils.arrayToString(args, 1);
				System.out.println("Creating new scoreboard named " + sbName + "!");
				if(Geffy.getReferences().createNewScoreboard(sbName))
				{
					Utils.sendMessage(event.getMessage().getChannel().block(), "New scoreboard named " + Utils.arrayToString(args, 1) + " created!");
				}
				
				return;
			}
		}
		
		if(args.length >= 3)
		{
			if(args[0].matches("add"))
			{
				for(int i = 2; i < args.length; i++)
				{
					if(Geffy.getReferences().getScoreboard(args[1]) == null)
					{
						Utils.sendMessage(event.getMessage().getChannel().block(), "There is no scoreboard named " + args[1] + "!");
						return;
					}
					
					//System.out.println(args[i]);
					User user = Utils.getUserByName(event.getMessage().getChannel().block(), args[i]);
					
					if(user != null)
					{
						Geffy.getReferences().getScoreboard(args[1]).addUser(user);
						Utils.sendMessage(event.getMessage().getChannel().block(), "Successfully added " + Utils.arrayToString(args, 2) + " to the scoreboard!");
						return;
					}
				}
			}
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{"sb"};
	}

}
