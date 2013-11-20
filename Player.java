import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.lang.Integer;

class Player implements Creatures
{
	protected String n;
	protected int intel;
	protected ArrayList items;
	protected Room location;
	protected int health;
	protected boolean see;
	protected Map words;
	protected int maxHealth;
	protected boolean firstBlindMove;
	protected Object wielded;
	
	public Player(String name, Room loc)
	{
		n = name;
		// conditions for intelligence 
		////////////////////////////////////////////////////////
		/*
		if (!name.equals("Jan"))
		{
		intel = 100;
		if (name.equals("Smith")||name.equals("Mr. Smith")|| name.equals("Patrick Smith"))
		{
			intel = -100;
			System.out.println("Jan is a genius!!!");
		}
		}
		else
		{
		intel = 200;
		System.out.println("Jan is a genius!!!");
		}
		*/
		////////////////////////////////////////////////////////
		
		location = loc;
		health = 100;
		items = new ArrayList();
		see = false;
		maxHealth = 100;
		firstBlindMove = false;
	}
	
	public void die()
	{
		health = -1;
	}
	
	public void darkness(String dir)	// When its too dark for them to see, and have no flashligh on
	{
		if (firstBlindMove)
		{
			System.out.println("While walking blindly to the " + dir + ", you fell into a gigantic hole");
			die();
		}
		else
		{
			System.out.println("You are in a dark room right now. You can't see anything without light. You should turn on a light source before attempting to move any further");
			firstBlindMove = true;
		}
	}
	
	public void darkness()	// When its too dark for them to see, and have no flashligh on
	{
		if (firstBlindMove)
		{
			System.out.println("While walking blindly, you fell into a gigantic hole");
			die();
		}
		else
		{
			System.out.println("You are in a dark room right now. You can't see anything without light. You should turn on a light source before attempting to move any further");
			firstBlindMove = true;
		}
	}
	
	public void move(String dir)
	{
		location = location.getNextRoom(dir);
		if (canSee())
		{
				System.out.println(location.visit());
		}
		else
			darkness(dir);
	}
	
	public void placeWords(Map words)
	{
		this.words = (HashMap)words;
	}
	
	public void prompt() throws IOException {
        
        try {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        boolean x = false;
            if (str != null) {
                while (!x) {
                	
                    System.out.print("\n" + "<HP: " + health + "> ");
                    str = in.readLine().trim();
                    x = process(str);
                }
            }
        } catch (IOException e) { }
    }
    
    public boolean process(String str)throws IOException {
    	String split[] = str.split(" ");
    	if (split[0].equalsIgnoreCase("turn") && split.length > 1&&(((checkCommand(split, "light")!= -1) || (checkCommand (split, "Flashlight")!= -1))|| split.length == 2))
        {
        	if (split[1].equalsIgnoreCase("on"))
        	{
        		if (checkInventory("Flashlight")!= -1)
        		{
        			((MagicItems)items.get(checkInventory("Flashlight"))).turn(true);
        			see= true;
        			System.out.println("You turn on your Flashlight.");
        			return true;
         		}        		
        		else
        		{
        			System.out.println("You don't have a flashlight");
        		}
        	}
        	else if (split[1].equalsIgnoreCase("off"))
        	{
        		if (checkInventory("Flashlight")!= -1)
        		{
        			((MagicItems)items.get(checkInventory("Flashlight"))).turn(false);
        			see = false;
        			System.out.println("You turn your Flashlight off.");
        			return true;
        		}
        		else
        		{
        			System.out.println("You don't have a flashlight");

        		}
        	}
        	else
        		System.out.println(split[0] + " " + split[1] + " what?");
        	
        }
        else if (str.equalsIgnoreCase("mouse on the moon"))
        {
        	health += 200;
        	intel = 100;
        }
        else if (saveGame(str))
        {
        	BufferedWriter outStream = new BufferedWriter(new FileWriter("save.txt"));	
			outStream.write(String.valueOf(n));
			outStream.newLine();
			outStream.write(String.valueOf(health));
			outStream.newLine();
			outStream.write(String.valueOf(intel));
			outStream.newLine();
			outStream.write("Items");
			outStream.newLine();
			for(int k = 0; k < items.size(); k++)
			{
				Items t = (Items) items.get(k);
				outStream.write(t.getName());
				outStream.newLine();
				outStream.write(t.getDesc());
				outStream.newLine();
				outStream.write(Boolean.toString(t.canRemove()));
				outStream.newLine();
			}
			outStream.close();
			System.out.println("Game saved");
			System.out.println();
        }
        else if (loadGame(str))
        {
        	FileReader inFile = new FileReader("save.txt");		// #1		
			BufferedReader inStream = new BufferedReader(inFile);	// #2		
			String inString;				// #3
			int k = 0;
			while((inString = inStream.readLine()) != null)			// #5	
			{
				if(k==0)
				{
					n = inString;
					k++;
				}
				else if(k==1)
				{
					health = Integer.parseInt(inString);
					k++;
				}
				else if(k == 2)
				{
					intel = Integer.parseInt(inString);
					k++;
				}
				else if(inString.equalsIgnoreCase("Items"))
				{
					int l = 0;
					while((inString = inStream.readLine()) != null)
					{
						if(l == 0)
						{
							
						}
						if(l == 1)
						{
							
						}
						if(l == 2)
						{
							
						}
					}
				}
			} 
			inStream.close();  
        }
        else if (str.equalsIgnoreCase("inventory") || str.equalsIgnoreCase("inv") || str.equalsIgnoreCase("i"))
        {
        	System.out.println(getItems());
        	return false;
        }
        else if(str.equalsIgnoreCase("help"))
		{
			if (!(health - 10 > 0))
				System.out.println("Sorry, you do not have enough health to receive help");
			else
			{
				health-= 10;
				intel = 0;
				System.out.println("\n" + "By receiving help, your health decreases by ten" + "\n");
				String path = "data/help.txt";
				File f = new File(path);
				String list[] = f.list();
				try
				{
					BufferedReader in = new BufferedReader(new FileReader(path));
					String str1;
					while ((str1 = in.readLine()) != null) 
						System.out.println(str1);
					in.close();
				} 
				catch (IOException e) { }
			}
		}
		else if(str.equalsIgnoreCase("credits"))
		{
			System.out.println();
			String path = "data/credits.txt";
			File f = new File(path);
			String list[] = f.list();
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(path));
				String str1;
				while ((str1 = in.readLine()) != null) 
					System.out.println(str1);
				in.close();
			} 
			catch (IOException e) { }
			
		}
        
        else if (str.equalsIgnoreCase("status") || str.equalsIgnoreCase("stats"))
        {
        	System.out.println(this);
        	return true;
        }
        
 
  		
  		else if (!see && !location.getLight())
  		{
  			darkness();
  			return true;
  		}
  		else if (West(str)) 			// If he wants to go west
            return goLoc("west");
            
        else if (East(str))		// If he wants to go east
            return goLoc("east");
            
        else if (South(str))	// If he wants to go south
            return goLoc("south");
            
        else if (North(str))	// If he wants to go north
            return goLoc("north");
            
        else if (Up(str))	// If he wants to go north
            return goLoc("up");
        
        else if (Down(str))	// If he wants to go north
            return goLoc("down");
            

        else if(split[0].equalsIgnoreCase("use"))
        {
        	if(split.length==1)
        		System.out.println("Use what?");
        	else if (split.length>1)
        	{
        		int k = 0;
        		boolean found = false;
        		while(!found&&k < items.size())
        		{
        			Items item = (Items)items.get(k);
        			if (checkInventory(item.getName()) != -1)
        			{
	        			if(item.getName().equals(split[1]))
	        			{
	        				if (item.isItems())
	        				{
		        				if (checkStock(split[1]) != 0)
		        				{
		        					item.use(this, location);
		        					return true;
		        				}
		        					
		        				else
		        				{
		        					System.out.println("Its out of Stock!");
		        					destroyItems(item);
		        				}
		        					
		        				found=true;
	        				}
	        				else if (!item.isItems())
		        			{
		        				System.out.println("This item is not yet usable");
		        				return false;
		        			}
	        			}		
	        			
	        			k++;
        			}
        		}
        		System.out.println("Sorry, nothing named " + split[1] + " is in your inventory");
        	}
        	else
        		System.out.println("I only understood you as far as wanting to use");
        }
        else if (split[0].equalsIgnoreCase("examine") || split[0].equalsIgnoreCase("identify"))
        {
        	String temp = "";
        	for (int i = 1; i < split.length; i++)
        	{
        		if (i == split.length-1)
        			temp+= split[i];
        		else
        			temp+= split[i] + " ";
        	}
        	boolean exists = false;
        	for (int i = 0; i < items.size(); i++)
        		if (((Items)items.get(i)).getName().equals(temp))
        			exists = true;
        			
        	if (exists)
        	{
        		Items x = null;
        		for (int i = 0; i < items.size(); i++)
        		{
        			if (((Items)items.get(i)).getName().equals(temp))
        				x = (Items)items.get(i);
        		}
        		System.out.println("You examine the " + x.getName() + " carefully, and these are your results:\n\n" + x);
        	}
        	else if (temp.equals(""))
        	{
        		System.out.println("Examine what?");
        	}
        	else
        		System.out.println("Sorry, there is nothing named " + temp + " in your inventory to examine");
        }
        else if (split[0].equalsIgnoreCase("peek"))
        {
        	if (location.getCreature(split[1]) != null)
        	{
        		if (location.getCreature(split[1]) instanceof Chest)
        			System.out.println((Chest)location.getCreature(split[1]));
        		else
        			System.out.println("Sorry, you cannot peek into that");
        	}
        	else
        		System.out.println("There is no such thing called '" + split[1] + "' to peek inside of");
        }
        else if (split[0].equalsIgnoreCase("take") || split[0].equalsIgnoreCase("get"))
        {
        	if (split.length == 1)
        	{
        		System.out.println(split[0] + " what?");
        		return false;
        	}
        	String temp = "";
        	for (int i = 1; i < split.length; i++)
        	{
        		if (i == split.length-1)
        			temp+= split[i];
        		else
        			temp+= split[i] + " ";
        	}
        	////////////////////////////////////////////////////////////////////
        	////////////////////////////////////////////////////////////////////
        	////////////////////////////////////////////////////////////////////	
        	if (checkInventory(temp) == -1 && split[1].equalsIgnoreCase("all"))
        	{
        		if (location.hasItem())
        		{
        			addItems(location.takeAllItems());	// UGLY CODE!!!
        			System.out.println("You take everything portable from this room");
        			//location.takeAllItems();			// same here!
        		}
        		else
        			System.out.println("Nothing is in this room!");
        	}		
        		
        	else if (checkInventory(temp) == -1 && location.hasItem(temp) && location.checkItem(temp).canRemove())
        	{
        		addItems(location.checkItem(temp));
        		System.out.println("You have taken " + temp + " and put it in your inventory");
        	}
        	
        	else if (checkInventory(temp) == -1 && location.hasItem(temp) && !location.checkItem(temp).canRemove())
        	{
        		System.out.println(temp + " is hardly portable");
        	}
        	else if (checkInventory(temp) != -1)
        	{
        		boolean a = addItems(location.checkItem(temp));
        		if (a)
        			System.out.println("You have increased your " + temp + " stock");
        		else
        			System.out.println("Sorry, that item currently can not be stacked");
        	}
        		
        	else
        		System.out.println("Sorry, nothing named " + temp + " exists");
        }
       	/*else if (split[0].equalsIgnoreCase("attack"))
        {
        	//System.out.println("CheckItem:" + checkItems(split));
        	//System.out.println("CheckCreatures:" + location.checkCreatures(split));
        	if (checkItems(split) != -1 &&location.checkCreatures(split)!= -1)
        	{
        		MagicItems item = null;
        		item = (MagicItems)ReadItems(split[checkItems(split)]);
        		Creatures hit = location.getCreature(split [location.checkCreatures(split)]);
        		int hia = ((Boss)hit).getArmor();
        		int attacked = item.attack(hit)-hia;
        		if (hit.getHealth() <= 0)
        		{
        			System.out.println("You have killed " + hit.getName() + "!");
        		}
        		else
        			System.out.println("You did " + attacked+ " damage to " + hit.getName());
        		return true;
        	}
        	else if (split.length == 2)
        		System.out.println("With what?");
        	else if (checkItems(split) == -1 &&location.checkCreatures(split)!= -1)
        		System.out.println("You have nothing named '" + split[3] + " to attack with");
	       	else
        		System.out.println("Sorry, nothing with such name exists");
        }*/
        
        else if (split[0].equalsIgnoreCase("wield") || split[0].equalsIgnoreCase("equip"))
        {
        	if (checkItems(split) != -1)
        	{
        		MagicItems item = null;
        		if (!(ReadItems(split[checkItems(split)]) instanceof MagicItems))
        		{
        			System.out.println("Sorry, you cannot wield that");
        			return false;
        		}
        		else
        			item = (MagicItems)ReadItems(split[checkItems(split)]);
        		if (item.canAttack())
        		{
	        		wielded = item;
	        		System.out.println("You have wielded '" + split[1] + "'");
	        		return true;
        		}
        		else
        			System.out.println("Sorry, that item can not be used to attack");
        	}
        	// wielded
        	else if (split.length == 2)
        		System.out.println(split[0] + " what?");
        	else if (checkItems(split) == -1 &&location.checkCreatures(split)!= -1)
        		System.out.println("You have nothing named '" + split[1] + "' to " + split[0]);
        }
        
        else if (split[0].equalsIgnoreCase("unwield") || split[0].equalsIgnoreCase("unequip") || split[0].equalsIgnoreCase("sheath"))
        {
        	if (wielded == null)
        		System.out.println("Why " + split[0] + " when you dont have anything on in the first place?");
        	else
        	{
        		String weapName = ((Items)wielded).getName();
        		wielded = null;
        		System.out.println("Unwielded '" + weapName + "'");
        	}
        	return true;
        }
        
        else if (split[0].equalsIgnoreCase("attack") || split[0].equalsIgnoreCase("hit") || split[0].equalsIgnoreCase("jab"))
        {
        	if (wielded != null && location.checkCreatures(split)!= -1)
        	{
        		Creatures hit = location.getCreature(split [location.checkCreatures(split)]);
        		int attacked = ((MagicItems)wielded).attack(hit);
        		if (hit.getHealth() <= 0)
        		{
        			System.out.print("You have killed " + hit.getName() + "!");
        			if (hit.hasItems())
        				System.out.println(" Items fall crashing onto the floor");
        			else
        				System.out.println();
        		}
        		else
        			System.out.println("You did " + attacked+ " damage to " + hit.getName());
        		return true;
        	}
        	else if (wielded == null && location.checkCreatures(split)== -1)
        		System.out.println("You have nothing equipped to attack with");
	       	else
        		System.out.println("You cannot attack that which does not exist");
        	//System.out.println("CheckItem:" + checkItems(split));
        	//System.out.println("CheckCreatures:" + location.checkCreatures(split));
        	/*if (checkItems(split) != -1 &&location.checkCreatures(split)!= -1)
        	{
        		MagicItems item = null;
        		if (!(ReadItems(split[checkItems(split)]) instanceof MagicItems))
        		{
        			System.out.println("Sorry, you cannot attack with that");
        			return false;
        		}
        		else
        			item = (MagicItems)ReadItems(split[checkItems(split)]);
        		if (item.canAttack())
        		{
	        		Creatures hit = location.getCreature(split [location.checkCreatures(split)]);
	        		int attacked = item.attack(hit);
	        		if (hit.getHealth() <= 0)
	        		{
	        			System.out.print("You have killed " + hit.getName() + "!");
	        			if (hit.hasItems())
	        				System.out.println(" Items fall crashing onto the floor");
	        			else
	        				System.out.println();
	        		}
	        		else
	        			System.out.println("You did " + attacked+ " damage to " + hit.getName());
	        		return true;
        		}
        		else
        			System.out.println("Sorry, that item can not be used to attack");
        	}
        	else if (split.length == 2)
        		System.out.println("With what?");
        	else if (checkItems(split) == -1 &&location.checkCreatures(split)!= -1)
        		System.out.println("You have nothing named '" + split[3] + "' to attack with");
	       	else
        		System.out.println("You cannot attack that which does not exist"); */
        }
        
        else if (split[0].equalsIgnoreCase("open") || split[0].equalsIgnoreCase("unlock"))
        {
        	if (split.length == 1)
        		System.out.println(split[0] + " what?");
        	else if (location.getCreature(split[1]) != null)
        	{
        		((Chest)location.getCreature(split[1])).open(items, this);
        	}
        	else
        		System.out.println("There is no such thing to open");
        }
        
        else if (words.containsKey(split[0].toLowerCase()))
        {
        	System.out.println(words.get(split[0].toLowerCase()));
        }
        
        else if (split[0].equalsIgnoreCase("directions") || split[0].equalsIgnoreCase("dir"))
        {
        	ArrayList list = location.neighbors();
        	String temp = "";
        	for (int i = 0; i < list.size(); i++)
        	{
        		if (i==list.size()-1)
        			temp += (String)list.get(i);
        		else
        			temp += (String)list.get(i) + ", ";
        	}
        		
        	System.out.println("There are routes " + temp + " from here");
        }
        
        else if (str.equalsIgnoreCase("look") || str.equalsIgnoreCase("l"))
        	if (canSee())
        		System.out.println(location.secondLook());
			else
				System.out.println("You are in a dark room right now. You can't see anything without light. Perhaps you should try and find a Flashlight to use?");
        /*else if (split[0].equalsIgnoreCase("Push"))
        {
        	int d = 0;
        	if (split.length <2)
        	{
        		System.out.println("Unlock what?");
        		return true;
        	}
        	else if (checkDoor(split)!= -1)
        	{
        		d = checkDoor(split);
        		String s = "Door" + d;
        		String k = "Key" + d;
        		if (checkInventory(k)!= -1)
        		{	
					((Teacher)location.getCreature(s)).die();
        			return true;
        		}
        		else
        		{
        			System.out.println("You don't have the appropriate key.");
        		}
        	}
        	
        	
        }  */  
        else if (split[0].equalsIgnoreCase("drop") || split[0].equalsIgnoreCase("trash"))
        {
        	if (split.length == 1)
        		System.out.println(split[0] + " what?");
        	else
        	{
	        	Items h = null;
	        	boolean exists = false;
	        	//System.out.println(split[1]);
	        	for (int i = 0; i < items.size(); i++)
	        		if (((Items)items.get(i)).getName().equals(split[1]))
	        		{
	        			h = ((Items)items.get(i));
	        			exists = true;
	        		}
	        			
	        	
	        	if (exists)
	        	{
	        		dropItems(h);
	        		System.out.println("You dropped " + split[1]);
	        		return true;
	        	}
	        	else
	        		System.out.println("You dont have anything named " + split[1] + " to drop");
        	}
        	
        	return false;
        }  
        else
            System.out.println("Sorry, I dont understand what you're saying");
        return false;   
    }
    
    public boolean goLoc(String dir)
    {
    	if (location.getNextRoom(dir) != null) {
                move(dir);
                return true;
        }
        else
        {
        	System.out.println("There is no exit in that direction");
        	return false;
        }
    }
	
	public boolean canSee()
	{
		return checkInventory("Flashlight") != -1 && (location.getLight() || see);
	}
	public String getName(){return n;}
	public int getIntel(){return intel;}
	public Room getLocation(){return location;}
	public String toString()
	{
		String temp = "";
		temp += "Name:\t\t";
		temp += n+"\n";
		temp += "Intelligence:\t" + intel + "\n";
		temp += "HP:\t\t" + health;
		if (wielded != null)
			temp += "\nWielded:\t" + ((Items)wielded).getName();
		return temp;
	}
	
	public int getMaxHealth()
	{
		return maxHealth;
	}

	public String getItems()
	{
		String returnedItems = "\nYou have: [";
		for (int i = 0; i < items.size(); i++)
		{
			if (i == items.size()-1)
				returnedItems+=((Items)items.get(i)).getName();
			else
				returnedItems+=((Items)items.get(i)).getName() + ", ";
		}
		returnedItems+= "] in your inventory";
		return returnedItems;
	}
	
	public boolean isDead()
	{
		return false;
	}
	
	public String getDesc()
	{
		return "Player";
	}
	
	public boolean addItems(Object it) throws IOException
	{
		if (it instanceof Map)
		{
			Set keys =  ((Map)it).keySet();
			Iterator a = keys.iterator();
			while (a.hasNext())
				addItems(((Map)it).get((String)a.next()));
		}
		else if (it instanceof Items)
		{
			if (((Items)it).stockable() && checkInventory(((Items)it).getName()) != -1)
			{
				String itName = ((Items)it).getName();
				Items i = location.getItem(itName); // the ROOM item
				int locStock = i.getStock();
				ReadItems(((Items)it).getName()).addStock(locStock);
				return true;
				//System.out.println("You have increased your " + ((Items)it).getName() + " stock by " + locStock);
			}
			else
			{
				items.add((Items)it);
				location.getItem(((Items)it).getName()); // Again.. Ugly, UGLY code!
			}
				
		}
		//System.out.println("Added Object");
		return false;
	}
	
	public void dropItems(Items it)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if(((Items)items.get(i)).getName().equals(it.getName()))
			{
				location.addItem((Items)items.get(i), 1);
				items.remove(i);
			}
				
		}
	}
	
	public void destroyItems(Items it)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if(((Items)items.get(i)).getName().equals(it.getName()))
			{
				items.remove(i);
			}
				
		}
	}
	public int getHealth()
	{
		return health;
	}
	public void changeHealth(int hp)
	{
		health += hp;
	}
	
	public void potionHealth(int hp)
	{
		if (health+hp < maxHealth)
			health += hp;
		else
			health = maxHealth;
	}
	
	public Items ReadItems(String n) throws IOException
	{
		Items it = null;
		for (int x = 0; x < items.size();x++)
		{
			if (n.equalsIgnoreCase(((Items)items.get(x)).getName()))
			{
				if (items.get(x) instanceof Items)
					it = (Items)items.get(x);
				else
					it = (MagicItems)items.get(x);
			}
		}
		return it;
		
	}
	private boolean West(String s)
	{
		if (s.equalsIgnoreCase("west") || s.equalsIgnoreCase("w")) 			// If he wants to go west
			return true;
		else
			return false;        
	}
	private boolean East(String s)
	{
		if (s.equalsIgnoreCase("east") || s.equalsIgnoreCase("e")) 			// If he wants to go west
           	return true;
        else
        	return false;            
	}  
	private boolean North(String s)
	{
		if (s.equalsIgnoreCase("north") || s.equalsIgnoreCase("n")) 			// If he wants to go west
           	return true;
        else
			return false;              
    }  
	private boolean South(String s)
	{
		if (s.equalsIgnoreCase("south") || s.equalsIgnoreCase("s")) 			// If he wants to go west
           	return true;
		else
			return false;             	            
    }
    	private boolean Up(String s)
	{
		if (s.equalsIgnoreCase("Up") || s.equalsIgnoreCase("u")) 			// If he wants to go west
			return true;
		else
			return false;        
	}
	private boolean Down(String s)
	{
		if (s.equalsIgnoreCase("Down") || s.equalsIgnoreCase("d")) 			// If he wants to go west
           	return true;
        else
        	return false;            
	} 
    
    private boolean saveGame(String s)
    {
    	if (s.equalsIgnoreCase("save"))
    		return true;
    	else
    		return false;
    }
    private boolean loadGame(String s)
    {
    	if (s.equalsIgnoreCase("load"))
    		return true;
    	else
    		return false;
    }
    private int checkItems(String[] s)
    {
    	for (int n =1; n<s.length; n++)
    	{
    		//System.out.println(n);
    		for (int x =0; x < items.size();x++)
    		{
    			//System.out.println(((Items)items.get(x)).getName());
    			//System.out.println(s[n]);
    			//System.out.println(s[n].equalsIgnoreCase(((Items)items.get(x)).getName()));    			
    			if (s[n].equalsIgnoreCase(((Items)items.get(x)).getName()))
    				return n;
    		}
    	}	
    	return -1;
    }
    public int checkInventory(String i) // Returns -1 if dont have item, returns items index if there is
    {
		for (int x = 0; x < items.size(); x++)
		{
			
    		String it = ((Items)items.get(x)).getName();
    		if (i.equalsIgnoreCase(it))
    		{
    			return x;
    		}
    	}
		return -1;
    }
    public int checkStock(String i)
    {
		for (int x = 0; x < items.size(); x++)
		{
			
    		String it = ((Items)items.get(x)).getName();
    		if (i.equalsIgnoreCase(it))
    		{
    			return ((Items)items.get(x)).getStock();
    		}
    	}
		return -1;
    }
    public int checkCommand(String[] s, String i)
    {
    	for (int x = 0; x < s.length; x++)
    	{
    		if (s[x].equalsIgnoreCase(i))
    		{
    			return x;
    		}
    	}
    	return -1;
    }
    public int checkDoor(String []s)
    {
    	String i;
    	for (int x = 1; x < 99; x++)
    	{
    		i = "Door" + x;
    		if (checkCommand(s, i)!= -1)
    		{
    			return checkCommand(s, i);
    		}
    	}
    	return -1;
    }
    public boolean hasItems()
    {
    	return !items.isEmpty();
    }
}
