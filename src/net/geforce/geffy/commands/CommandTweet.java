package net.geforce.geffy.commands;

import net.geforce.geffy.commands.twitter.StatusCodes;
import net.geforce.geffy.commands.twitter.Tweet;
import net.geforce.geffy.commands.twitter.TwitterManager;
import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IReaction;
import sx.blah.discord.handle.obj.IUser;
import twitter4j.Status;
import twitter4j.TwitterException;

public class CommandTweet extends Command<MessageReceivedEvent> {

	@Override
	public void execute(MessageReceivedEvent event, String[] args) throws Exception {		
		try
		{
			if(Geffy.getReferences().getTwitterInstanceForUser(event.getAuthor()) == null)
			{
				TwitterManager.authenticateUser(event.getAuthor());
			}
			else
			{
				if(args.length <= 1)
				{
					Utils.sendMessage(event.getChannel(), "You must include a message for your tweet!");
					return;
				}
				
				TwitterManager.sendTweet(event.getChannel(), event.getAuthor(), event.getMessage(), Utils.arrayToString(args));
			}
		}
		catch(TwitterException e) 
		{
			e.printStackTrace();
			Utils.sendMessage(event.getChannel(), "Oops! An error occurred while posting a new tweet!\n\nCause: " + TwitterManager.handleTwitterException(e));
		}
	}

	@Override
	public void reactionAdded(IReaction reaction, IUser user, IChannel channel)
	{
		String emoji = reaction.getEmoji().toString();
		if(!emoji.matches("🔁") && !emoji.matches("❤") && !emoji.matches("🗑")) return;

		Tweet potentialTweet = Geffy.getReferences().getTweetFromMessage(reaction.getMessage().getLongID());
		
		try {
			if(potentialTweet != null)
			{
				Status tweet = Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance().showStatus(potentialTweet.getTweet().getId());
				
				if(Geffy.getReferences().getTwitterInstanceForUser(user) != null)
				{
					if(emoji.matches("🔁"))
					{
						TwitterManager.retweetTweet(tweet, reaction.getMessage(), user);
						Status updatedStatus = Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().showStatus(tweet.getId());

						Thread.sleep(100);	
						reaction.getMessage().edit(reaction.getMessage().getContent(), TwitterManager.getFormattedEmbed(Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance(), updatedStatus).build());
					}
					else if(emoji.matches("❤"))
					{
						TwitterManager.favoriteTweet(tweet, reaction.getMessage(), user);
						Status updatedStatus = Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance().showStatus(tweet.getId());

						Thread.sleep(100);	
						reaction.getMessage().edit(reaction.getMessage().getContent(), TwitterManager.getFormattedEmbed(Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance(), updatedStatus).build());
					}
					else if(emoji.matches("🗑") && user.getLongID() == potentialTweet.getAuthor().getLongID())
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
		if(!emoji.matches("🔁") && !emoji.matches("❤") && !emoji.matches("🗑")) return;

		Tweet potentialTweet = Geffy.getReferences().getTweetFromMessage(reaction.getMessage().getLongID());
		
		try {
			if(potentialTweet != null)
			{
				Status tweet = Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance().showStatus(potentialTweet.getTweet().getId());
				
				if(Geffy.getReferences().getTwitterInstanceForUser(user) != null)
				{
					if(emoji.matches("🔁"))
					{
						TwitterManager.unretweetTweet(reaction.getMessage(), user);
						Status updatedStatus = Geffy.getReferences().getTwitterInstanceForUser(user).getTwitterInstance().showStatus(tweet.getId());

						Thread.sleep(100);	
						reaction.getMessage().edit(reaction.getMessage().getContent(), TwitterManager.getFormattedEmbed(Geffy.getReferences().getTwitterInstanceForUser(potentialTweet.getAuthor()).getTwitterInstance(), updatedStatus).build());
					}
					else if(emoji.matches("❤"))
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
