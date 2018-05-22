package net.geforce.geffy.commands;

import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandHelp extends Command<MessageReceivedEvent> {

	@Override
	public void execute(MessageReceivedEvent event, String[] args) throws Exception {
		Utils.sendMessage(event.getChannel(), "You may find a list of Geffy's commands and his source code here on GitHub: https://github.com/Geforce132/Discord-Geffy/blob/master/README.md");
	}

	@Override
	public String[] getAliases() {
		return new String[]{"help"};
	}

}
