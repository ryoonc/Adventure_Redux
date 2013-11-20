import java.util.ArrayList;
import java.util.*;

abstract class Room
{
	Map creatures;
	Map items;
	Map directions;
	String name;
	String description;
	int id;
	boolean hasVisited;
	boolean delay;
	protected boolean light;
	
	public Room(String n, String d, int l)
	{
		creatures = new HashMap();
		items = new HashMap();
		directions = new HashMap();
		description = d;
		name = n;
		hasVisited = false;
		delay = false;
		if ( l == 1)
		{
			light = true;
		}
		else
		{
			light = false;
		}
	}
	
	public Map getAllCreatures()
	{
		return creatures;
	}
	
	public Room visit()
	{
		if (delay)
			hasVisited = true;
		else
			delay = true;
		return this;
	}
	
	public int getID()
	{
		return id;
	}
	
	public Map getAllItems()
	{
		return items;
	}
	
	public Items getItem(String name)
	{
		return (Items)items.remove((Object)name);
	}
	
	public Items getItem(Object item)
	{
		return (Items)items.remove(((Items)item).getName());
	}
	
	public Items checkItem(String name)
	{
		return (Items)items.get((Object)name);
	}
	
	public Map takeAllItems()
	{
		Map taken = new HashMap();
		taken.putAll(items);
		items = new HashMap();
		return taken;
	}
	
	public boolean hasItem(String itemName)
	{
		return items.containsKey(itemName);
	}
	
	public boolean hasItem()
	{
		return !items.isEmpty();
	}
	
	public String toString()
	{
		String temp = "";
		if (hasVisited)
			temp += "- " + getRoomName() + " -";
		else
		{
			temp += "- " + getRoomName() + " -" + " \n";
			temp += getRoomDesc();
		}
		if (!items.isEmpty())
			temp += "\n" + showAllItems();
		if (!creaturesIsEmpty())
			temp += "\n" + showAllCreatures();
		return temp; 
	}
	
	public String secondLook()
	{
		String temp = "";
		temp += "- " + getRoomName() + " -" + " \n";
		temp += getRoomDesc();
		if (!items.isEmpty())
		{
			temp += "\n" + showAllItems();
		}
			
		if (!creaturesIsEmpty())
			temp += "\n" + showAllCreatures();
		return temp;
	}
	
	public String getRoomName()
	{
		return name;
	}
	
	public String getRoomDesc()
	{
		return description;
	}
	
	public Creatures removeCreature(Creatures creatureToRemove)
	{
		if(creatures.containsKey(creatureToRemove))
		{
			creatures.remove(creatureToRemove);
			creatures.put(creatureToRemove, null);
			return creatureToRemove;
		}
		else
		 	return null;
	}
	
	public void addCreature(Creatures creatureToAdd)
	{
		creatures.put(creatureToAdd, creatureToAdd);
	}
	
	public String showAllCreatures()
	{
		
		String returnedCrea = "\n";
		Set keys = creatures.keySet();
		Iterator c = keys.iterator();
		int i = 0;
		while (c.hasNext())
		{
			if (i == creatures.size()-1)
			{
				Object next = c.next();
				if ((Creatures)creatures.get((Creatures)creatures.get(next)) != null)
					returnedCrea+=((Creatures)creatures.get((Creatures)creatures.get(next))).getDesc();
			}
				
			else
			{
				Object next = c.next();
				if ((Creatures)creatures.get((Creatures)creatures.get(next)) != null)
					returnedCrea+=((Creatures)creatures.get((Creatures)creatures.get(next))).getDesc() + "\n\n";
			}
			i++;
		}
		return returnedCrea;
		
		/*
		String returnedItems = "\nThere is [";
		for (int i = 0; i < creatures.size(); i++)
		{
			Set keys = creatures.keySet();
    		Iterator c = keys.iterator();
    		while (c.hasNext())
    		{
    			Object next = c.next();
    			if (i == creatures.size()-1)
    			{
    				if ((Creatures)creatures.get(next) != null)
    					returnedItems+=((Creatures)creatures.get(next)).getName();
    			}
					
				else
				{
					if ((Creatures)creatures.get(next) != null)
						returnedItems+=((Creatures)creatures.get(next)).getName() + ", ";
				}
					
    		}
			
		}
		returnedItems+= "] in the room";
		return returnedItems;*/
		
	}
	
	public String showAllItems()
	{
		String returnedItems = "\n";
		Set keys = items.keySet();
		Iterator c = keys.iterator();
		int i = 0;
		while (c.hasNext())
		{
			if (i == items.size()-1)
				returnedItems+=((Items)items.get(c.next())).groundName();
			else
					returnedItems+=((Items)items.get(c.next())).groundName() + "\n\n";
			i++;
		}
		return returnedItems;
		
		//return items.toString();
	}
	
	public boolean creaturesIsEmpty()
	{
		Set keys = creatures.keySet();
		Iterator c = keys.iterator();
		while (c.hasNext())
		{
			Object next = c.next();
			if ((Creatures)creatures.get(next) != null)
				return false;
		}
		return true;
	}
	
	public void addItem(Items itemToAdd)
	{
			items.put(itemToAdd.getName(), itemToAdd);
			
	}
	public boolean addItem(Items itemToAdd, int i)
	{
		if (!items.containsKey(itemToAdd.getName()))
		{
			items.put(itemToAdd.getName(), itemToAdd);
			return true;
		}
		else
			return false;
			
	}
	
	public Items removeItem(Items itemToRemove)
	{
		if(!items.containsKey(itemToRemove))
		{
			System.out.println("This item cannot be found");
			return null;
		}
		else if(!itemToRemove.canRemove())
		{
			System.out.println("This item is unable to be removed");
			return null;
		}
		else
		{
			items.remove(itemToRemove);
			System.out.println("You have taken " + itemToRemove.getName());
			return(itemToRemove);
		}
	}
	
	public ArrayList emptyRooms()
	{
		ArrayList temp = new ArrayList();
		Set key = directions.keySet();
		Iterator iter = key.iterator();
		while(iter.hasNext())
		{
			String str = (String)iter.next();
			if(directions.get(str) != null)
				temp.add(directions.get(str));
		}
		return temp;
	}
	
	public ArrayList neighbors()
	{
		ArrayList temp = new ArrayList();
		Set key = directions.keySet();
		Iterator iter = key.iterator();
		while(iter.hasNext())
		{
			String str = (String)iter.next();
			if(directions.get(str) != null)
				temp.add(str);
		}
		return temp;
	}
	
	public void setNextRoom(String direction, Room room)
	{
		directions.put(direction, room);
		/*if (room!=null)
			room.getDir().put(oppoDir(direction),this);*/
				
	}
	
	public Room getNextRoom(String direction)
	{
		if(!directions.containsKey(direction))
		{
			return null;
		}
		else
		{
			return (Room)directions.get(direction);
		}
	}
	
	public Map getDir()
	{
		return directions;
	}
	
	// UN-NEEDED.. RESULTS IN BAD LINKS EVERYWHERE!
	/*public String oppoDir(String d)
	{
		if (d.equalsIgnoreCase("west"))
		{
			return "east";
		}
		if (d.equalsIgnoreCase("up"))
		{
			return "up";
		}
		if (d.equalsIgnoreCase("down"))
		{
			return "down";
		}
		if (d.equalsIgnoreCase("east"))
		{
			return "west";
		}
		if (d.equalsIgnoreCase("south"))
		{
			return "north";
		}
		else
		{
			return "south";
		}
	}*/
	public int checkCreatures(String [] s)
	{	
		if (s != null)
		{
			for (int n =1; n<s.length; n++)
	    	{	
				Set keys = creatures.keySet();
		    	Iterator c = keys.iterator();
				while (c.hasNext())
				{
					
					Creatures crea = (Creatures)creatures.get(c.next());
					//System.out.println(s[n]);
					if (crea != null && s[n].equalsIgnoreCase(crea.getName()))
		    			return n;
		    		
				}
			}					    	
	    }
		return -1;
	}
	
	public Creatures getCreature(String n)
	{
		Set keys = creatures.keySet();
    	Iterator c = keys.iterator();
    	while (c.hasNext())
    	{		
    		Creatures crea = (Creatures)creatures.get(c.next());
    		if (n.equalsIgnoreCase(crea.getName()))
    			return crea;
    	}
    	return null; 
	}
	public boolean getLight()
	{
		return light;
	}
}