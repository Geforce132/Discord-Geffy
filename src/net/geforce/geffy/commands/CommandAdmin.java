package net.geforce.geffy.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import net.geforce.geffy.commands.sa.SAResourceManager;
import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import net.geforce.geffy.misc.Passwords;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandAdmin extends Command<MessageCreateEvent> {

	@Override
	public void execute(MessageCreateEvent event, String[] args) throws Exception {
		if(!(event.getMessage().getAuthor().get().getId().asLong() == Passwords.GEFORCES_ID)) return;

		String command = args[0];
		
		if(command.matches("playingtext"))
			Geffy.getClient().changePlayingText(Utils.arrayToString(args, 1));
		
		if(command.matches("resetsasearches"))
			SAResourceManager.SEARCH_QUERIES_REMAINING = 100;
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"admin"};
	}

}
