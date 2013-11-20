import java.util.Random;
import java.util.ArrayList;

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
	private double probOfMove = 4.0/4.0;
	//private Room location;
	private int a=0;
	private Player player;
	private int str;
	private String description;
	private String battleCry;
	private String attackCry;
	private boolean cried;
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
	}
	
	public void move(Room loc)														// Whats called every 'turn'
	{
		if (health <= 0)
		{
			die();
		}
		if (loc == location)
		{
			attack();
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
		location.removeCreature(this);
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
		if (damage > 0)
		{
			damage = str*(rand.nextInt(8) + 2); 			//random amount of damage (10-60)
			player.changeHealth(-damage); 					//subtracts health from player
			if (!cried)
			{
				System.out.println(name + ": \"" + battleCry + "\"\n");
				cried = true;
			}
			
			System.out.println(name + attackCry + damage + " damage");
			if (player.getHealth() <=0)						// Nice addition, Hao wei
				System.out.println(name + " has killed you!");
			else											// Reiterates itself every turn
			{
				System.out.println("Your remaining health is: " + player.getHealth());
				System.out.println(name + " 's remaining health is " + getHealth());
			}
			return damage;
		}
		else
			return 0;
	}
	
	public Room getLocation()
	{
		return location;
	}
	
	public void changeHealth(int hp)
	{
		health += hp;
	}
}