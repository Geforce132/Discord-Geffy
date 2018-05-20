package net.geforce.geffy.commands;

import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

public class CommandGiveCookie extends Command<MessageReceivedEvent>{
	
	@Override
	public void execute(MessageReceivedEvent event, String[] args)
	{
		IUser user = Utils.getUserByName(event.getChannel(), args[0]);
		
		event.getChannel().sendMessage("Here's a cookie for you " + user.mention() + "! :cookie:");
	}
	
	public String[] getAliases() {
		return new String[]{"givecookie"};
	}

}
