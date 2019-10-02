package net.geforce.geffy.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import net.geforce.geffy.main.Utils;

public class CommandHelp extends Command<MessageCreateEvent> {

	@Override
	public void execute(MessageCreateEvent event, String[] args) throws Exception {
		Utils.sendMessage(event.getMessage().getChannel().block(), "You may find a list of Geffy's commands and his source code here on GitHub: https://github.com/Geforce132/Discord-Geffy/blob/master/README.md");
	}

	@Override
	public String[] getAliases() {
		return new String[]{"help"};
	}

}
