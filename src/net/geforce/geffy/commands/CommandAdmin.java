package net.geforce.geffy.commands;

import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import net.geforce.geffy.misc.Passwords;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandAdmin extends Command<MessageReceivedEvent> {

	@Override
	public void execute(MessageReceivedEvent event, String[] args) throws Exception {
		if(!(event.getAuthor().getLongID() == Passwords.GEFORCES_ID)) return;

		String command = args[0];
		
		if(command.matches("playingtext"))
			Geffy.getClient().changePlayingText(Utils.arrayToString(args, 1));
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"admin"};
	}

}
