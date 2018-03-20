package com.cyk.gameobjs;

import java.util.Random;
import java.util.ArrayList;

class Troll extends Player implements Creatures
{
	private String trollName;
	//private ArrayList items;
	private String temp;
	private int health;
	Random rand = new Random();
	private double probOfAttack = 1.0/4.0;
	private double probOfMove = 0.0/1.0;
//	private boolean player = false;
	private Player player;
	private String description;
	private String battleCry;
	private String attackCry;
	private boolean cried;
	private int str;
	
	public Troll(String name, Room loc, Player p)
	{
		super(name, loc);
		trollName = name;
		location.addCreature(this);
		health = rand.nextInt(10) + 50; //random health for troll (100-200)
		player = p;
		
	}
	
	public void move()
	{
		ArrayList directions = location.emptyRooms();
		if (getHealth() <= 0)
		{
			die();
		}
		/*if (rand.nextDouble() < probOfMove)`
		{
			//checks if prob is okay and then moves
			String dir = (String)directions.get(rand.nextInt(directions.size()));
			location.removeCreature(this); //removes creature from room 
			location = location.getNextRoom(dir);
			location.addCreature(this); //adds creature to next room
		}*/
		if (player.getLocation().equals(getLocation()) && rand.nextDouble() < probOfAttack) 
		{
			//checks if there is a player in the room, changes player to true
			System.out.println("There is a troll called " + getName() + " in this room, it's health is: " + getHealth());
			System.out.println(getName() + "lets out a fiersome growl and gives you an evil stare...");
			System.out.println();
			attack();
		}
	}
	
	public String getName()
	{
		return trollName;
	}
	
	public void die()
	{
		location.removeCreature(this);
	}
	
	
	public String toString()
	{
		temp += "Name:	";
		temp += getName() + "\n";
		temp += "HP:	" + getHealth() + "\n";
		return temp;
	}
	
	/*public void getItems()
	{
		System.out.print("This troll has: ");
		for (int a = 0; a < items.size(); a++)
		{
			System.out.print(items.get(a));
			System.out.print(", ");
		}
	}*/
	
	public int getHealth()
	{
		return health;
	}
	
	public void attack()
	{
		System.out.println("...You are under attack...");
		int damage = rand.nextInt(10) + 5; //random amount of damage (10-5)
		//int loss = rand.nextInt(20) + 20;
		player.changeHealth(-damage); //subtracts health from player
		//player.setIntel(loss);
		System.out.println(getName() + " does " + damage + " damage to you.");
		System.out.println("Your remaining health is: " + player.getHealth());
		//System.out.println(getName() + " stupifies you ... ");
		//System.out.println("Your intelligence went down to: " + player.setIntel());
		System.out.println();
		if(player.getHealth() <= 0)
		{
			System.out.println("YOU SUCK!!! You have been killed by the troll!");
		}
	}

	public Room getLocation()
	{
		return location;
	}
	
	public void changeHealth(int damage)
	{
		super.changeHealth(damage);
	}
}