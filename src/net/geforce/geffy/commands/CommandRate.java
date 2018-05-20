package net.geforce.geffy.commands;

import java.util.concurrent.ThreadLocalRandom;

import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandRate extends Command<MessageReceivedEvent> {

	@Override
	@SuppressWarnings("deprecation")
	public void execute(MessageReceivedEvent event, String[] args) throws Exception {
		if(args.length < 1)
		{
			Utils.sendMessage(event.getChannel(), "Missing argument! Usage: ~rate <thing to rate>");
			return;
		}
		
		int randomNum = ThreadLocalRandom.current().nextInt(0, 6);
				
		event.getMessage().addReaction("🇷");
		Thread.sleep(200);
		
		event.getMessage().addReaction("🇦");
		Thread.sleep(200);
		
		event.getMessage().addReaction("🇹");
		Thread.sleep(200);
		
		event.getMessage().addReaction("🇪");
		Thread.sleep(200);
		
		event.getMessage().addReaction("🇩");
		Thread.sleep(200);

		if(randomNum == 0) {
			event.getMessage().addReaction(":zero:");
		}
		else if(randomNum == 1) {
			event.getMessage().addReaction(":one:");
		}
		else if(randomNum == 2) {
			event.getMessage().addReaction(":two:");
		}
		else if(randomNum == 3) {
			event.getMessage().addReaction(":three:");
		}
		else if(randomNum == 4) {
			event.getMessage().addReaction(":four:");
		}
		else if(randomNum == 5) {
			event.getMessage().addReaction(":five:");
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{"rate"};
	}

}
