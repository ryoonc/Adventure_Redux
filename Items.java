import java.util.ArrayList;
import java.util.*;

public class Items// implements Comparable
{
	protected String name;
	protected String descrip;
	protected String groundName;
	protected boolean removable;
	protected ArrayList attributes;
	protected Random rand = new Random();
	protected int healValue;
	protected int stock;
	protected int used;
	protected boolean canAttack;
	protected boolean stockable;
	protected String ID;
	
	public Items(String ID, String n, String d, String groundName, String r)
	{
		// NORMAL ITEMS
		this.ID = ID;
		name = n;
		descrip = d;
		if(r.equals("1"))
			removable = true;
		else
			removable = false;
		this.groundName = groundName;
		healValue = 0;
		stock = 0;
		stockable = false;
		canAttack = true;
	}
	
	public Items(String ID, String n, String d, String groundName, String r, int healValue, int stock)
	{
		// HEALING ITEMS
		this.ID = ID;
		name = n;
		descrip = d;
		if(r.equals("1"))
			removable = true;
		else
			removable = false;
		this.groundName = groundName;
		this.healValue = healValue;
		this.stock = stock;
		used = 0;
		canAttack = false;
		stockable = true;
	}
	
	public String toString() // when 'this' is called
	{
		return "Name:\t\t" + name + "\n"+ "Description:\t" + descrip + "\n" + "Heal:\t\t" + (stock-used);
	}
	
	public boolean canAttack()
	{
		return canAttack;
	}
	
	public boolean stockable()
	{
		return stockable;
	}
	
	public String getID()
	{
		return ID;
	}
	
	public void heal(Player player)
	{
		if (healValue != 0)
		{
			int healed = rand.nextInt(healValue+20)+healValue;
			System.out.println("You drank the " + name + " and gained " + healed + " points.");
			System.out.println("There are " + (stock-used-1) + " left");
			player.potionHealth(healed);
		}
			
	}
	
	public void use(Player player, Room room)
	{
		if (stock != 0)
		{
			if (player.getHealth() != player.getMaxHealth())
			{
				heal(player);
				used++;	
			}
			else
				System.out.println("Your health is already at max!");
		}
	}
	
	public boolean isItems()
	{
		return true;
	}
	
	public int getStock()
	{
		return stock-used;
	}
	
	public void addStock(int stocks)
	{
		stock += stocks;
	}
	
	public String groundName() // When you reference it on the ground..
	{
		return groundName;
	}
	
	public boolean canRemove() // is it removable?
	{
		return removable;
	}
	
	public String getDesc()		// The description, when the command 'examine' is called.
	{
		return descrip;
	}
	
	public String getName()		// name of 'this'
	{
		return name;
	}
	
	public void use(String target, Room room)	// Marks supposed to work on it.. ISNT HE!?
	{
		//actions
		//search for target in room
		//message that shows what has been done
	}
}