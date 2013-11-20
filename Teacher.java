import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

class Teacher extends Player implements Creatures
{
	// DECLARATION OF PRIVATE VARIABLES
	private String name;
	private boolean dead;
	private ArrayList items;
	private int health;
	private int damage;
	Random rand = new Random();
	private double probOfAttack = 3.0/3.0;
	private double probOfMove = 0.0/4.0;
	//private Room location;
	private int a=0;
	private Player player;
	private int str;
	private String description;
	private String battleCry;
	private String attackCry;
	private boolean cried;
	private Map inventory;
	///////////////////////////////////
	
	public Teacher(String name, String desc, String battleCry, String attackCry, Room loc, Player p, int strength, int hp) 	// Constructor
	{
		super(name, loc);
		this.name = name;
		location.addCreature(this);
		health = hp;
		player = p;
		dead = false;
		str = strength;
		description = desc;
		this.battleCry = battleCry;
		this.attackCry = attackCry;
		cried = false; // Whether he made his battlecry or not
		inventory = (HashMap)location.takeAllItems();
	}
	
	public boolean addItems(Object it)
	{
		if (it instanceof Map)
			((Map)it).putAll(inventory);

		else if (it instanceof Items)
			inventory.put(((Items)it).getName(), (Items)it);
		
		return false;
		//System.out.println("Added Object");
	}
	
	public void move(Room loc)														// Whats called every 'turn'
	{
		if (health <= 0)
		{
			die();
			//System.out.println(name + " drops a few things, doubles over, and dies");
		}
		if (location == loc && !isDead())
		{
			ArrayList directions = location.emptyRooms();
			if (rand.nextDouble() < probOfMove)
			{
				String dir = (String)directions.get(rand.nextInt(directions.size()));
				location.removeCreature(this);
				location = location.getNextRoom(dir);
				location.addCreature(this);
			}
			if (super.getLocation().equals(getLocation()) && rand.nextDouble() < probOfAttack)
			{
				attack();
				a++;
			}
		}
		
	}
	
	public boolean isDead()
	{
		if (health <= 0 || dead)
			return true;
		return false;
	}
	
	public void die()
	{
		//
			
		Iterator iter = inventory.keySet().iterator();
		while(iter.hasNext())
		{
			//Map roomInventory = location.getAllItems();
			Items item = (Items)inventory.get(iter.next());
			location.addItem(item);
		}
		
		location.removeCreature(this);
	}
	public boolean hasItems()
	{
		return !inventory.isEmpty();
	}
	
	public String getName()
	{
		
		return name;
	}
	
	public String getDesc()
	{
		return name + "" + description;
	}
	
	public String toString()
	{
		String temp ="\n";
		temp += "Name:	";
		temp += this.getName() + "\n";
		temp += "HP:	" + this.getHealth() + "\n";
		return temp;
	}
//	public String getStats()
	{
		
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
	
	public int attack()
	{
		System.out.println();
		
		damage = str*(rand.nextInt(8) + 2); 			//random amount of damage (10-60)
		player.changeHealth(-damage); 					//subtracts health from player
		if (!cried)
		{
			System.out.println(name + ": \"" + battleCry + "\"\n");
			cried = true;
		}
		
		System.out.println(getName() + attackCry + damage + " damage");
		if (player.getHealth() <=0)						// Nice addition, Hao wei
			System.out.println(name + " has killed you!");
		else											// Reiterates itself every turn
		{
			System.out.println(name + " 's remaining health is " + getHealth());
		}
		return damage;
	}
	
	public Room getLocation()
	{
		return location;
	}
	
	public void changeHealth(int hp)
	{
		if (health + hp >= 100)
			health = 100;
		else
			health += hp;
	}
}