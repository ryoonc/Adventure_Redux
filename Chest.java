import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.*;

class Chest implements Creatures
{
	private Room location;
	private String name;
	Map inventory;
	Items key;
	boolean opened = false;
	boolean door;
	String dir;
	int ID;
	private Room locToGo;
	
	public Chest(int ID, Room loc, String n, Items k)
	{
		this.ID = ID;
		location = loc;
		name = n;
		inventory = (HashMap)location.takeAllItems();
		location.addCreature(this);
		key = k;
		door = false;
	}
	
	public Chest(int ID, Room loc, String n, Items k, String dir)
	{
		this.ID = ID;
		location = loc;
		name = n;
		//inventory = (HashMap)location.takeAllItems();
		location.addCreature(this);
		key = k;
		door = true;
		this.dir = dir;
		this.locToGo = location.getNextRoom(dir);
		location.setNextRoom(dir, null);
	}
	
	public String getDesc()
	{
		if (!door)
		{
			if (opened)
				return "There is an empty " + name + " here";
			else
				return "A " + name + " which may contain items lies here";
		}
		else
		{
			if (opened)
				return "There is an open " + name + " here";
			else
				return "A locked " + name + " to the " + dir + " is here";
		}
	}
	
	public int getID()
	{
		return ID;
	}
	
	public boolean isDead()
	{
		return false;
	}
	
	public Room getLocation()
	{
		return location;
	}
	
	public void move(String dir)
	{
		return;
	}
	
	public String toString()
	{
		return "This " + name + " contains " + inventory.keySet().toString();
	}
	
	public int getHealth()
	{
		return 1;
	}
	
	public void changeHealth(int hp)
	{
		
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDir()
	{
		return dir;
	}
	
	public boolean canOpen(ArrayList playerInventory)
	{
		boolean openable = false;
		for (int i = 0; i < playerInventory.size(); i++)
		{
			if(((Items)playerInventory.get(i)).equals(key))
				openable = true;
		}
		return openable;
	}
	
	public void open(ArrayList playerInventory, Player player)
	{
		if (!door)
		{
			if(!canOpen(playerInventory))
				System.out.println("You do not have the correct item to open this chest.");
			else
			{
				if (opened)
					System.out.println("Why open an already-opened chest?");
				else
				{
					System.out.println("You have successfully opened the chest. Its contents spill all over the floor..");
					opened = true;
					player.destroyItems(key);
					Iterator iter = inventory.keySet().iterator();
					while(iter.hasNext())
					{
						//Map roomInventory = location.getAllItems();
						Items item = (Items)inventory.get(iter.next());
						location.addItem(item);
					}
				}
				
			}
		}
		else
		{
			if(!canOpen(playerInventory))
				System.out.println("You do not have the correct key to open this door.");
			else
			{
				if (opened)
					System.out.println("Why open an already-opened " + name + "?");
				else
				{
					System.out.println("You have successfully unlocked the " + name + ". Its destination revealed to the " + dir + "..");
					opened = true;
					player.destroyItems(key);
					location.setNextRoom(dir, locToGo);
				}
				
			}
		}
	}
	public boolean hasItems()
	{
		return !inventory.isEmpty();
	}
	
}