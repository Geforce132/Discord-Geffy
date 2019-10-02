package net.geforce.geffy.commands;

import java.util.concurrent.ThreadLocalRandom;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.reaction.ReactionEmoji;
import net.geforce.geffy.main.Utils;

public class CommandRate extends Command<MessageCreateEvent> {

	@Override
	@SuppressWarnings("deprecation")
	public void execute(MessageCreateEvent event, String[] args) throws Exception {
		if(args.length < 1)
		{
			Utils.sendMessage(event.getMessage().getChannel().block(), "Missing argument! Usage: ~rate <thing to rate>");
			return;
		}
		
		int randomNum = ThreadLocalRandom.current().nextInt(0, 6);
				
		event.getMessage().addReaction(ReactionEmoji.unicode("🇷"));
		Thread.sleep(300);
		
		event.getMessage().addReaction(ReactionEmoji.unicode("🇦"));
		Thread.sleep(300);
		
		event.getMessage().addReaction(ReactionEmoji.unicode("🇹"));
		Thread.sleep(300);
		
		event.getMessage().addReaction(ReactionEmoji.unicode("🇪"));
		Thread.sleep(300);
		
		event.getMessage().addReaction(ReactionEmoji.unicode("🇩"));
		Thread.sleep(300);

		if(randomNum == 0) {
			event.getMessage().addReaction(ReactionEmoji.unicode(":zero:"));
		}
		else if(randomNum == 1) {
			event.getMessage().addReaction(ReactionEmoji.unicode(":one:"));
		}
		else if(randomNum == 2) {
			event.getMessage().addReaction(ReactionEmoji.unicode(":two:"));
		}
		else if(randomNum == 3) {
			event.getMessage().addReaction(ReactionEmoji.unicode(":three:"));
		}
		else if(randomNum == 4) {
			event.getMessage().addReaction(ReactionEmoji.unicode(":four:"));
		}
		else if(randomNum == 5) {
			event.getMessage().addReaction(ReactionEmoji.unicode(":five:"));
		}
	}

	@Override
	public String[] getAliases() {
		return new String[]{"rate"};
	}

}
