package net.geforce.geffy.misc;

import java.util.HashMap;

import sx.blah.discord.handle.obj.IUser;

public class Scoreboard {
	
	private HashMap<IUser, Integer> userScores = new HashMap<IUser, Integer>();
	
	public int getScore(IUser user)
	{
		return userScores.get(user);
	}
	
	public void addUser(IUser user)
	{
		userScores.put(user, 0);
	}
	
	public void removeUser(IUser user)
	{
		userScores.remove(user);
	}

}
