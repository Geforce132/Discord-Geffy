package net.geforce.geffy.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;
import net.geforce.geffy.commands.twitter.StatusCodes;
import net.geforce.geffy.commands.twitter.Tweet;
import net.geforce.geffy.commands.twitter.TwitterManager;
import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import twitter4j.Status;
import twitter4j.TwitterException;

public class CommandTweet extends Command<MessageCreateEvent> {

	@Override
	public void execute(MessageCreateEvent event, String[] args) throws Exception {		
		try
		{
			if(Geffy.getReferences().getTwitterInstanceForUser(event.getMessage().getAuthor().get()) == null)
			{
				TwitterManager.authenticateUser(event.getMessage().getAuthor().get());
			}
			else
			{
				if(args.length <= 1)
				{
					Utils.sendMessage(event.getMessage().getChannel().block(), "You must include a message for your tweet!");
					return;
				}
				
				TwitterManager.sendTweet(event.getMessage().getChannel().block(), event.getMessage().getAuthor().get(), event.getMessage(), Utils.arrayToString(args));
			}
		}
		catch(TwitterException e) 
		{
			e.printStackTrace();
			Utils.sendMessage(event.getChannel(), "Oops! An error occurred while posting a new tweet!\n\nCause: " + TwitterManager.handleTwitterException(e));
		}
	}

	@Override
	public void reactionAdded(ReactionEmoji reaction, User user, Message message)
	{
		String emoji = reaction.asCustomEmoji().get().getName();
		if(!emoji.matches("ðŸ”�") && !emoji.matches("â�¤") && !emoji.matches("ðŸ—‘")) return;

		Tweet potentialTweet = Geffy.getReferences().getTweetFromMessage(message.getId().asLong());
		
		try {
			if(potentialTweet != null)
			{
				Status tweet = Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance().showStatus(potentialTweet.getTweet().getId());
				
				if(Geffy.getReferences().getTwitterInstanceForUser(user) != null)
				{
					if(emoji.matches("ðŸ”�"))
					{
						TwitterManager.retweetTweet(tweet, reaction.getMessage(), user);
						Status updatedStatus = Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().showStatus(tweet.getId());

						Thread.sleep(100);	
						reaction.getMessage().edit(reaction.getMessage().getContent(), TwitterManager.getFormattedEmbed(Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance(), updatedStatus).build());
					}
					else if(emoji.matches("â�¤"))
					{
						TwitterManager.favoriteTweet(tweet, reaction.getMessage(), user);
						Status updatedStatus = Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance().showStatus(tweet.getId());

						Thread.sleep(100);	
						reaction.getMessage().edit(reaction.getMessage().getContent(), TwitterManager.getFormattedEmbed(Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance(), updatedStatus).build());
					}
					else if(emoji.matches("ðŸ—‘") && user.getLongID() == potentialTweet.getAuthor().getLongID())
					{
						TwitterManager.deleteTweet(tweet, reaction.getMessage(), user);
						reaction.getMessage().delete();
					}
				}
				else {
					TwitterManager.authenticateUser(user);
				}
			}
		} catch(TwitterException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reactionRemoved(IReaction reaction, IUser user, IChannel channel)
	{
		String emoji = reaction.getEmoji().toString();
		if(!emoji.matches("ðŸ”�") && !emoji.matches("â�¤") && !emoji.matches("ðŸ—‘")) return;

		Tweet potentialTweet = Geffy.getReferences().getTweetFromMessage(reaction.getMessage().getLongID());
		
		try {
			if(potentialTweet != null)
			{
				Status tweet = Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance().showStatus(potentialTweet.getTweet().getId());
				
				if(Geffy.getReferences().getTwitterInstanceForUser(user) != null)
				{
					if(emoji.matches("ðŸ”�"))
					{
						TwitterManager.unretweetTweet(reaction.getMessage(), user);
						Status updatedStatus = Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().showStatus(tweet.getId());

						Thread.sleep(100);	
						reaction.getMessage().edit(reaction.getMessage().getContent(), TwitterManager.getFormattedEmbed(Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance(), updatedStatus).build());
					}
					else if(emoji.matches("â�¤"))
					{
						TwitterManager.unfavoriteTweet(tweet, reaction.getMessage(), user);
						Status updatedStatus = Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance().showStatus(tweet.getId());

						Thread.sleep(100);	
						reaction.getMessage().edit(reaction.getMessage().getContent(), TwitterManager.getFormattedEmbed(Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance(), updatedStatus).build());
					}
				}
			}
		} catch(TwitterException e) {
			e.printStackTrace();
			
			if(e.getStatusCode() == StatusCodes.NOT_FOUND)
				reaction.getMessage().edit(TwitterManager.getDefaultTwitterEmbed("This tweet cannot be found!").build());
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean reactsToReactions()
	{
		return true;
	}

	@Override
	public String[] getAliases() {
		return new String[] {"tweet"};
	}

}
