package net.geforce.geffy.main;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import net.geforce.geffy.handlers.EventHandler;
import net.geforce.geffy.misc.Passwords;
import net.geforce.geffy.misc.References;
import sx.blah.discord.api.IDiscordClient;

public class Geffy {
	
	public static final String VERSION = "1.4.0";
	
	/**
	 * Geffy's Discord client instance.
	 */
	private static DiscordClient client;
	
	/**
	 * A References instance which holds a variety of variables and lists for other features.
	 */
	private static References referencesInstance = new References();
	
	public static void main(String args[])
	{
		client = createClient();

		client.getEventDispatcher().on(EventHandler.class).subscribe();
		
        client.login().block(); 
	}
	
	/**
	 * Builds Geffy's initial client.
	 */
	public static DiscordClient createClient() 
	{
        //DiscordClientBuilder clientBuilder = new DiscordClientBuilder().online("with new ~tweet command"); 
		return new DiscordClientBuilder(isInDevMode() ? Passwords.devAppToken : Passwords.appToken).build();
    }
	
	/**
	 * @return If Geffy is currently being dev'd on. :P
	 */
	public static boolean isInDevMode()
	{
		return false;
	}
	
	/**
	 * @return Geffy's Discord username.
	 */
	public static String getUsername()
	{
		return isInDevMode() ? "GeffyDev" : "Geffy";
	}
	
	/**
	 * @return Geffy's {@link IDiscordClient} instance.
	 */
	public static DiscordClient getClient()
	{
		return client;
	}
	
	/**
	 * @return Geffy's {@link References} instance.
	 */
	public static References getReferences()
	{
		return referencesInstance;
	}
}
