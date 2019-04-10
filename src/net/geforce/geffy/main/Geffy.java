package net.geforce.geffy.main;

import net.geforce.geffy.handlers.EventHandler;
import net.geforce.geffy.misc.Passwords;
import net.geforce.geffy.misc.References;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

public class Geffy {
	
	public static final String VERSION = "1.3.0";
	
	/**
	 * Geffy's Discord client instance.
	 */
	private static IDiscordClient client;
	
	/**
	 * A References instance which holds a variety of variables and lists for other features.
	 */
	private static References referencesInstance = new References();
	
	public static void main(String args[])
	{
		client = createClient();
		
		EventDispatcher dispatcher = client.getDispatcher();
		dispatcher.registerListener(new EventHandler());
	}
	
	/**
	 * Builds Geffy's initial client.
	 */
	public static IDiscordClient createClient() 
	{
        ClientBuilder clientBuilder = new ClientBuilder().online("with new ~tweet command"); 
        clientBuilder.withToken(isInDevMode() ? Passwords.devAppToken : Passwords.appToken);

        try 
        {
            return clientBuilder.login(); 
        } 
        catch (DiscordException e) 
        {
            e.printStackTrace();
            return null;
        }
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
	public static IDiscordClient getClient()
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
