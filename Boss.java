import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.*;

class Boss extends Player implements Creatures
{
	// DECLARATION OF PRIVATE VARIABLES
	private String name;
	private boolean dead;
	//private ArrayList items;
	private int health;
	private int damage;
	private String desc;
	Random rand = new Random();
	private double probOfAttack = 3.0/3.0;
	private double probOfSpell = 2.0/5.0;
	private double probOfHeal = 1.5/4.0;
	//private Room location;
	private int a=0;
	private Player player;
	private int str;
	private int ar;
	private Room location;
	private String bcry;
	private String acry;
	private Map inventory;
	///////////////////////////////////
	
	public Boss(String name, Room loc, Player p, int strength, int hp, String desc, int armor, String battlecry, String attackcry) 	// Constructor
	{
		super(name, loc);
		this.name = name;
		health = hp;
		player = p;
		location = loc;
		location.addCreature(this);
		dead = false;
		str = strength;
		this.desc = desc;
		ar = armor;
		bcry = battlecry;
		acry = attackcry;
		inventory = (HashMap)location.takeAllItems();
	}
	public boolean hasItems()
	{
		return !inventory.isEmpty();
	}
	public String getDesc()
	{
		return desc;
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
	public void move(Room loc)														// Whats called every 'turn'
	{
	
		if (health <= 0)
		{
			die();
		}
		if (!isDead())
		{
			
			if (loc == location)
			{
				System.out.println(getBattleCry());
				if (rand.nextDouble() < probOfSpell)
				{
					cast();
				}	
				else
				{
					if (rand.nextDouble() < probOfHeal)
					{
						heal();
					}
					else
					attack();
				}
			
			}
		}
		
	}
	
	public String getAttackCry()
	{
		return acry;
	}
	public String getBattleCry()
	{
		return bcry;
	}
	public boolean isDead()
	{
		if (health <= 0 || dead)
			return true;
		return false;
	}
	public void cast()
	{
		System.out.println(getName() + " casts a spell on you!");
		int damage = player.getHealth()/2;
		player.changeHealth(-damage);
		System.out.println("You lose half your health");
		System.out.println(name + " 's remaining health is " + getHealth());
	}
	public void heal()
	{
		Random rand1 = new Random();
		System.out.println(getName() + " eats a piece of blue slip");
		int damage = rand1.nextInt(41)+10;
		this.changeHealth(damage);
		System.out.println(name + " glows blue and heals " + damage + " points." );
		System.out.println(name + " 's remaining health is " + getHealth());
	}
	
	public String getName()
	{
		
		return name;
	}
	
//	public String getDesc()
//	{
//		return name + "" + description;
//	}
	
	public String toString()
	{
		String temp ="\n";
		temp += "Name:	";
		temp += this.getName() + "\n";
		temp += "HP:	" + this.getHealth() + "\n";
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
	
	
	public int attack()
	{
		System.out.println();
		damage = str*(rand.nextInt(8) + 2); 	//random amount of damage (10-60)
		player.changeHealth(-damage); 			//subtracts health from player
		System.out.println(getAttackCry() + damage + " damage");
		if (player.getHealth() <=0)
		System.out.println(getName() + " has killed you!");
		else
		{
		System.out.println("Your remaining health is: " + player.getHealth());
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
		health += hp;
	}
	public int getArmor()
	{
		return ar;
	}
}