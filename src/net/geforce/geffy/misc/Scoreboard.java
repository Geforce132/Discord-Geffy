package net.geforce.geffy.misc;

import java.util.HashMap;

import discord4j.core.object.entity.User;

public class Scoreboard {
	
	private HashMap<User, Integer> userScores = new HashMap<User, Integer>();
	
	public int getScore(User user)
	{
		return userScores.get(user);
	}
	
	public void addUser(User user)
	{
		userScores.put(user, 0);
	}
	
	public void removeUser(User user)
	{
		userScores.remove(user);
	}

}
