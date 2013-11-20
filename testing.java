public void use(String command)
{
	
	
	
	
	
	
	
	//[item].use(target, location)
}


















import java.util.ArrayList;

abstract class testing
{
	ArrayList creatures;
	ArrayList items;
	Map directions;
	String name;
	String description;
	
	public Room(String n, String d)
	{
		creatures = new ArrayList();
		items = new ArrayList();
		directions = new HashMap();
		description = d;
		name = n;
	}
	
	
	ArrayList getAllCreatures();
	
	ArrayList getAllItems();
	
	ArrayList takeAllItems();
	
	String toString();
	
	String getRoomName();
	
	Creature removeCreature(Creature creatureToRemove);

	
	void addCreature(Creature creatureToAdd);
	
	String showAllCreatures()
	{
		return creatures.toString();
	}
	
	String showAllItems()
	{
		return items.toString;
	}
	
	void addItem(Item itemToAdd)
	{
		items.add(itemToAdd);
	}
	
	Item removeItem(Item itemToRemove)
	{
		
	}

	Room getNextRoom(String direction)
	{
		
	}
	
	void setNextRoom(Room next, String direction)
	{
		directions.set(direction, next);
	}
	
}