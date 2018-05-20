package net.geforce.geffy.commands;

import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CommandScoreboard extends Command<MessageReceivedEvent> {

	@Override
	public void execute(MessageReceivedEvent event, String[] args) throws Exception {
		if(args.length < 0)
		{
			Utils.sendMessage(event.getChannel(), "Missing arguments!");
		}
		
		if(args.length >= 2)
		{
			if(args[0].matches("create"))
			{
				String sbName = Utils.arrayToString(args, 1);
				System.out.println("Creating new scoreboard named " + sbName + "!");
				if(Geffy.getReferences().createNewScoreboard(sbName))
				{
					Utils.sendMessage(event.getChannel(), "New scoreboard named " + Utils.arrayToString(args, 1) + " created!");
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
						Utils.sendMessage(event.getChannel(), "There is no scoreboard named " + args[1] + "!");
						return;
					}
					
					//System.out.println(args[i]);
					IUser user = Utils.getUserByName(event.getChannel(), args[i]);
					
					if(user != null)
					{
						Geffy.getReferences().getScoreboard(args[1]).addUser(user);
						Utils.sendMessage(event.getChannel(), "Successfully added " + Utils.arrayToString(args, 2) + " to the scoreboard!");
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
