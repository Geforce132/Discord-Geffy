package net.geforce.geffy.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

/**
 * A class containing various utility methods used by a variety of other classes.
 * 
 * @author Geforce
 */
public class Utils {
	
	/**
	 * Sends a message to a channel.
	 * 
	 * @param channel The channel to send a message to.
	 * @param message The message itself.
	 * @return An {@link IMessage} object of the sent message.
	 */
	public static Message sendMessage(Channel channel, String message)
	{
		return channel.sendMessage(message);
	}
	
	/**
	 * Sends a private message to a user.
	 * 
	 * @param user The user to send a DM to.
	 * @param message The message itself.
	 * @return An {@link IMessage} object of the sent message.
	 */
	public static Message sendMessage(User user, String message)
	{
		return sendMessage(user.getOrCreatePMChannel(), message);
	}
	
	/**
	 * Sends a message containing an {@link EmbedObject} to a channel.
	 * 
	 * @param channel The channel to send a message to.
	 * @param message The message itself.
	 * @param embed The {@link EmbedObject} to attach to the message.
	 * @return An {@link IMessage} object of the sent message.
	 */
	public static Message sendMessageWithEmbed(Channel channel, String message, EmbedObject embed)
	{
		return channel.sendMessage(message, embed);
	}
	
	/**
	 * Sends a private message containing an {@link EmbedObject} to a user.
	 * 
	 * @param user The user to send a message to.
	 * @param message The message itself.
	 * @param embed The {@link EmbedObject} to attach to the message.
	 * @return An {@link IMessage} object of the sent message.
	 */
	public static Message sendMessageWithEmbed(User user, String message, EmbedObject embed)
	{
		return sendMessageWithEmbed(user.getOrCreatePMChannel(), message, embed);
	}
	
	/**
	 * Finds and returns the {@link IUser} object of the user with the given name.
	 * 
	 * @param channel The channel to search in.
	 * @param name The name of the user to search for.
	 * @return The IUser instance of the user.
	 */
	public static User getUserByName(Channel channel, String name)
	{
		List<User> users = channel.getUsersHere();
		String tempName = name;
		
		if(tempName.startsWith("<@"))
		{
			tempName = tempName.replace("<@", "").replace(">", "");
		}


		for(User user : users)
		{
			if(user.getUsername().matches(tempName))
			{
				return user;
			}
			
			try {
				if(user.getId().asLong() == Long.parseLong(tempName))
					return user;
			}
			catch(NumberFormatException e) { }
		}
		
		return null;
	}
	
	/**
	 * Converts a String array into a single String.
	 * 
	 * @param args The array to convert.
	 * @return A compiled String consisting of the entire array's values
	 * separated with spaces.
	 */
	public static String arrayToString(String[] args)
	{
		return arrayToString(args, 0);
	}
	
	/**
	 * Converts a String array into a single String. This variation is
	 * useful if you don't want to compile the entire array.
	 * 
	 * @param args The array to convert.
	 * @param index The array index to start at.
	 * @return A compiled String consisting of the entire array's values
	 * separated with spaces.
	 */
	public static String arrayToString(String[] args, int index)
	{
		String string = "";
		
		for(int i = index; i < args.length; i++)
		{
			if(args[i] != null && args[i] != "null")
				string += args[i] + " ";
		}
		
		return string.substring(0, string.length() - 1);
	}
	
	/**
	 * Downloads a Discord {@link Attachment} as a usable File.
	 * 
	 * @param attachment The Attachment to download.
	 * @return The downloaded file.
	 * @throws MalformedURLException If the attachment's URL is not valid (should not happen).
	 * @throws IOException If the file fails to write properly for some reason.
	 */
	public static File downloadImageFrom(Attachment attachment) throws MalformedURLException, IOException
	{
		BufferedImage image = null;
		URL url = new URL(attachment.getUrl());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
		image = ImageIO.read(connection.getInputStream());
		
		String suffix = FilenameUtils.getExtension(attachment.getFilename());
		File outputFile = new File("temp." + suffix);
		ImageIO.write(image, suffix, outputFile);
		
		return outputFile;
	}

}
