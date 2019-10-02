package net.geforce.geffy.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import net.geforce.geffy.main.Utils;

public class CommandGiveCookie extends Command<MessageCreateEvent>{
	
	@Override
	public void execute(MessageCreateEvent event, String[] args)
	{
		String mention = "Stranger";
		try {
			IUser user = Utils.getUserByName(event.getChannel(), args[0]);
			mention = user.mention();
		}
		catch (Exception e) {
			mention = args[0];
		}

		event.getChannel().sendMessage("Here's a cookie for you, " + mention + "! :cookie:");
	}
	
	public String[] getAliases() {
		return new String[]{"givecookie"};
	}

}
