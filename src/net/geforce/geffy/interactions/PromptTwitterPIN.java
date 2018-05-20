package net.geforce.geffy.interactions;

import net.geforce.geffy.main.Geffy;
import net.geforce.geffy.main.Utils;
import sx.blah.discord.handle.obj.IUser;

/**
 * An implementation of the {@link Prompt} class which takes a user's
 * PIN code after being authenticated with Twitter and sets their access token.
 * 
 * @author Geforce
 */
public class PromptTwitterPIN extends Prompt {
	
	public PromptTwitterPIN(String message, IUser user) {
		super(message, user);
	}

	/**
	 * Sets the user's access token using the PIN code they provide. If the PIN
	 * is invalid, replies with an error message.
	 * 
	 * @param reply The PIN code the user has provided.
	 */
	@Override
	public void completePrompt(String reply) {
		
		try {
			Geffy.getReferences().getTwitterInstanceForUser(getPromptedUser()).setAccessToken(reply);
			Utils.sendMessage(getPromptedUser(), "Thank you! You may now tweet using ~tweet <message> in a channel.");
		}
		catch(Exception e)
		{
			Utils.sendMessage(getPromptedUser(), "I'm sorry, but something went wrong during the authentication process. Please try running ~tweet again.\n\nNote: A typical PIN will look something like 1234567.");
			e.printStackTrace();
		}
		
	}

}
