package com.cyk.gameobjs;

public interface Creatures
{
	//gets the currect location of the creature
	public Room getLocation();
	
	public void move(String dir);
	
	public String toString();
	public int getHealth();
	public void changeHealth(int hp);
	public String getName();
	public boolean isDead();
	public String getDesc();
	public boolean hasItems();
}

