import java.util.ArrayList;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class Game								// This is the secondary driver class.. Game is called from main
{
	private Player player;						// Stores the player object
	private ArrayList creatures;
	private Boss Finny;
	private Boss Schreur;
	private Random rand = new Random();
	private double probOfSpawn = 1.0/5.0;
	private Map finalRooms;
	private String roomsDir = "data/rooms/";
	private String creatsDir = "data/creatures/";
	private String itemsDir = "data/items/generic/";
	private String keysDir = "data/items/keys/";
	private String containerDir = "data/containers/";

	public Map read(String path)						// Reads all the rooms and returns a Map of them
	{
		File f = new File(path);
		String list[] = f.list();
		Map data = new HashMap();
		for (int i = 0; i < list.length; i++)
		{
			Map m = new HashMap();
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(path + list[i]));
				String str;
				while ((str = in.readLine()) != null) 
				{
					String header = str.substring(str.indexOf("<")+1,str.indexOf(">"));
					int start = str.indexOf(">")+1;
					int end = str.indexOf("</");
					//System.out.println(header + " " + str.substring(start, end));
					m.put(header, str.substring(start, end));
				}
				in.close();
			} 
			catch (IOException e) { }
			
			data.put(m.get("ID"), m);
		}
		return data;
	}
	
	public Map linkRooms(Map data)				// Links all the rooms read in the data Map and returns a map of linked rooms
	{
		Map rooms = new HashMap();
		String x = "1";
		for (int i = 1; i <= data.size()+1; i++)
		{
			rooms.put(((HashMap)data.get(x)).get("ID"), new NormRoom((String)((HashMap)data.get(x)).get("Name"), (String)((HashMap)data.get(x)).get("Description"), Integer.parseInt((String)((HashMap)data.get(x)).get("Light"))));
			x = (new Integer(i)).toString();
		}
		
		x = "1";
		for (int i = 1; i <= rooms.size()+1; i++)
		{
				if (!((String)((HashMap)data.get(x)).get("North")).equals(""))
					((NormRoom)rooms.get(x)).setNextRoom("north", (NormRoom)rooms.get(((String)((HashMap)data.get(x)).get("North"))));
					
				if (!((String)((HashMap)data.get(x)).get("South")).equals(""))
					((NormRoom)rooms.get(x)).setNextRoom("south", (NormRoom)rooms.get(((String)((HashMap)data.get(x)).get("South"))));
					
				if (!((String)((HashMap)data.get(x)).get("East")).equals(""))
					((NormRoom)rooms.get(x)).setNextRoom("east", (NormRoom)rooms.get(((String)((HashMap)data.get(x)).get("East"))));
					
				if (!((String)((HashMap)data.get(x)).get("West")).equals(""))
					((NormRoom)rooms.get(x)).setNextRoom("west", (NormRoom)rooms.get(((String)((HashMap)data.get(x)).get("West"))));
				
				if (!((String)((HashMap)data.get(x)).get("Up")).equals(""))
					((NormRoom)rooms.get(x)).setNextRoom("up", (NormRoom)rooms.get(((String)((HashMap)data.get(x)).get("Up"))));
				
				if (!((String)((HashMap)data.get(x)).get("Down")).equals(""))
					((NormRoom)rooms.get(x)).setNextRoom("down", (NormRoom)rooms.get(((String)((HashMap)data.get(x)).get("Down"))));
			x = (new Integer(i)).toString();
		}
		return rooms;
	}
	
	public String readStory() throws IOException
	{
		FileReader i =  new FileReader("data/story.txt");
		BufferedReader in = new BufferedReader(i);
		String str; String story = "";
		while ((str = in.readLine()) != null) 
			story+=str + "\n";
		return story;
	}
	
	public void createCreatures(Map rooms)	// Puts all creatures inside ArrayList
	{
		Map data = read(creatsDir);
		String x = "1";
		for (int i = 1; i < data.size()+1; i++)
		{
			x = (new Integer(i)).toString();
			Map creaNum = (Map)data.get(x);
			String creaName = (String)creaNum.get("Name");
			String desc = (String)creaNum.get("Description");
			String batCry = (String)creaNum.get("BattleCry");
			String atkCry = (String)creaNum.get("AttackCry");
			String creaRoomNum = (String)((HashMap)data.get(x)).get("Room");
			Room creaRoom = (NormRoom)rooms.get(creaRoomNum);
			int creaAtk = Integer.parseInt((String)((Map)data.get(x)).get("Attack"));
			int creaHP = Integer.parseInt((String)((Map)data.get(x)).get("Health"));
			creatures.add(new Teacher(creaName, desc, batCry, atkCry, creaRoom, player, creaAtk, creaHP));
		}
	}
	
	public void placeItems(Map rooms) throws IOException
	{
		Map data = read(itemsDir);
		String x = "1";
		for (int i = 1; i <= data.size()+1; i++)
		{
			Map itemNum = (HashMap)data.get(x);
			String roomNum = (String)itemNum.get("Room");
			if (!roomNum.equals(""))
			{
				NormRoom room = (NormRoom)rooms.get(roomNum);
				if (itemNum.containsKey("HealValue"))
					room.addItem(new Items((String)itemNum.get("ID"), (String)itemNum.get("Name"), (String)itemNum.get("Description"), (String)itemNum.get("Ground"), (String)itemNum.get("Removable"), (Integer.parseInt((String)itemNum.get("HealValue"))), (Integer.parseInt((String)itemNum.get("Stock")))));
				else
					room.addItem(new MagicItems((String)itemNum.get("ID"), (String)itemNum.get("Name"), (String)itemNum.get("Description"), (String)itemNum.get("Ground"), (String)itemNum.get("Removable"), (Integer.parseInt((String)itemNum.get("Attack")))));
			}
			x = (new Integer(i)).toString();
		}
	}

	public Map loadWords() throws IOException
	{
		String path = "data/phrases.txt";
		File f = new File(path);
		String list[] = f.list();
		Map m = new HashMap();
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(path));
			String str;
			while ((str = in.readLine()) != null) 
			{
				int start = str.indexOf(",")+2;
				int end = str.length();
				m.put(str.substring(0, start-2), str.substring(start, end));
			}
			in.close();
		} 
		catch (IOException e) { }
		return m;
	}
	
	public Items randItem(int difficulty)
	{
		int r = rand.nextInt(2)+1;
		if (r == 1)
		{
			switch (difficulty)
			{
				case 1:
					return new MagicItems("0", "Ruler", "A substitute dropped this..", "A Ruler is on the ground", "1", rand.nextInt(15)+10);
				case 2:
					return new MagicItems("0", "Pencil", "A TA dropped this..", "A Pencil is on the ground", "1", rand.nextInt(20)+15);
				case 3:
					return new MagicItems("0", "Book", "A Teacher dropped this..", "A Book is on the ground", "1", rand.nextInt(25)+20);
			}
			//System.out.println("Item");
		}
		else
		{
			String p = "Potion";
				p += new String(new Integer(rand.nextInt(1000)+1).toString());
			switch (difficulty)
			{
				case 1:
					return new Items("0", p, "Heals you", "A " + p + " is on the ground", "1", rand.nextInt(3)+1, 8);
				case 2:
					return new Items("0", p, "Heals you", "A " + p + " is on the ground", "1", rand.nextInt(4)+2, 5);
				case 3:
					return new Items("0", p, "Heals you", "A " + p + " is on the ground", "1", rand.nextInt(5)+3, 3);
			}
			//System.out.println("Item");
		}
		return null;
	}
	
	public void spawn()
	{
		if (rand.nextDouble() < probOfSpawn)
		{
			String roomNum = (new Integer(rand.nextInt(finalRooms.size())+1)).toString();
			Room randRoom = (Room)finalRooms.get(roomNum);
			int decide = rand.nextInt(2);
			if (decide == 0)
			{
				if (randRoom.creaturesIsEmpty())
				{
					int randNum = rand.nextInt(3)+1;
					switch (randNum) // Difficulty. 1 is easy, 3 is hard..
					{
						case 1:
							if (rand.nextDouble() < (1.0/2.0))
							{
								Teacher t = new Teacher("Substitute", " teacher, weak but armed with a ruler, marches back and fourth here", "Hey, what are you doing here!?", " slaps you silly with his ruler for ", randRoom, player, 1, rand.nextInt(50)+10);
								if (rand.nextDouble() < (1.0/3.0))
								{
									Items i = randItem(1);
									if (i != null)
										t.addItems(i);
								}
								//System.out.println("Sub");
								creatures.add(t);
							}
							break;
						case 2:
							if (rand.nextDouble() < (1.0/3.0))
							{
								Teacher t = new Teacher("TA", ", the teachers assistant, sits here with a pencil in hand", "Hey, what are you doing here!?", " pokes you with his pencil for ", randRoom, player, 2, rand.nextInt(70)+40);
								if (rand.nextDouble() < (1.0/3.0))
								{
									Items i = randItem(2);
									if (i != null)
										t.addItems(i);
								}
								//System.out.println("TA");
								creatures.add(t);
							}
							break;
						case 3:
							if (rand.nextDouble() < (1.0/4.0))
							{
								Teacher t = new Teacher("Teacher", " of TIS is here", "OH MY GOODNESS WHAT ARE YOU DOING OUT OF CLASS", " hurts your ears for ", randRoom, player, 3, rand.nextInt(90)+50);
								if (rand.nextDouble() < (1.0/3.0))
								{
									Items i = randItem(3);
									if (i != null)
										t.addItems(i);
								}
								//System.out.println("TEA");
								creatures.add(t);
							}
							break;
					}
					//System.out.println("A vicious beast has spawned in room " + roomNum);
				}
			}
			else
			{
				int randNum = rand.nextInt(3)+1;
				String p = "Potion";
				p += new String(new Integer(rand.nextInt(1000)+1).toString());
				switch (randNum) // Stock..
				{
					case 1:
						if (rand.nextDouble() < (1.0/70.0))
							randRoom.addItem(new Items("0", p, "Heals you", "A " + p + " is on the ground", "1", rand.nextInt(3)+1, 4));
						break;
					case 2:
						if (rand.nextDouble() < (1.0/100.0))
							randRoom.addItem(new Items("0", p, "Heals you", "A " + p + " is on the ground", "1", rand.nextInt(4)+2, 2));
						break;
					case 3:
						if (rand.nextDouble() < (1.0/250.0))
							randRoom.addItem(new Items("0", p, "Heals you", "A " + p + " is on the ground", "1", rand.nextInt(5)+3, 1));
						break;
				}
			}
			
		}
	}
	
	public Map placeKeys(Map rooms) throws IOException
	{
		Map data = read(keysDir);
		Map keys = new HashMap();
		String x = "1";
		for (int i = 1; i <= data.size()+1; i++)
		{
			Map itemNum = (HashMap)data.get(x);
			String roomNum = (String)itemNum.get("Room");
			if (!roomNum.equals(""))
			{
				NormRoom room = (NormRoom)rooms.get(roomNum);
				Keys ky= new Keys((String)itemNum.get("ID"), (String)itemNum.get("Name"), (String)itemNum.get("Description"), (String)itemNum.get("Ground"), (String)itemNum.get("Removable"));
				room.addItem(ky);
				keys.put((String)itemNum.get("ID"), ky);
			}
			x = (new Integer(i)).toString();
		}
		return keys;
	}
	
	public void placeContainers(Map rooms, Map keys) throws IOException
	{
		Map data = read(containerDir);
		String x = "1";
		for (int i = 1; i <= data.size()+1; i++)
		{
			Map itemNum = (HashMap)data.get(x);
			String roomNum = (String)itemNum.get("Room");
			if (!roomNum.equals(""))
			{
				NormRoom room = (NormRoom)rooms.get(roomNum);
				if (itemNum.containsKey("Direction"))
					new Chest(Integer.parseInt((String)itemNum.get("ID")), room, (String)itemNum.get("Name"), (Keys)(keys.get((String)itemNum.get("Key"))), (String)itemNum.get("Direction"));
				else
					new Chest(Integer.parseInt((String)itemNum.get("ID")), room, (String)itemNum.get("Name"), (Keys)(keys.get((String)itemNum.get("Key"))));
			}
			x = (new Integer(i)).toString();
		}
	}
	
	public Game(String name) throws IOException // Constructor
	{
		creatures = new ArrayList();			// ArrayList of Creatures (Ex: teachers)
		finalRooms = linkRooms(read(roomsDir));	// Map of all the rooms
		Map keys = (HashMap)placeKeys(finalRooms);
		System.out.println();
		player = new Player (name, ((NormRoom)finalRooms.get("1")));
		placeItems(finalRooms);
		placeContainers(finalRooms, keys);
		player.placeWords(loadWords());
		player.addItems(((NormRoom)finalRooms.get("1")).getItem("Flashlight")); // Add mandatory flashlight
		
		Finny = new Boss("Principal",((NormRoom)finalRooms.get("56")),player,3,150,"The Prinpal smiles gleefully at the sight of you",5, "You want a detention kid?", "Finny beams laser out of his eyes for ");
		Schreur = new Boss("BibleTeacher",((NormRoom)finalRooms.get("59")),player,5,200,"A BibleTeacher stands here, flames surround him, burning everything that touches him",10, "God wills it!!! Veni, Vidi, Vici", "The Bible teacher attacks you with a loaf of bread for ");
		createCreatures(finalRooms);
	}
	
	public void play() throws IOException		// Gameplay
	{
		System.out.println(player.getLocation());
		while(player.getHealth() > 0)			// Supposedly every loop is a turn
		{
			player.prompt();
			for (int i = 0; i < creatures.size(); i++)
				((Teacher)creatures.get(i)).move(player.getLocation());
			spawn();
			Finny.move(player.getLocation());
			Schreur.move(player.getLocation());
			if (player.getLocation()==((NormRoom)finalRooms.get("65")))
			{
				System.out.println("\nThe outside air is nice. \nYou're free now, congratulations!\nYou have completed the adventure!");
				return;
			}
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("*CRASH*");
		System.out.println("Things go black for a while...");
		in.readLine();
		System.out.println("You awake naturally, on a gigantic cloud. ");
		System.out.println("You see only white all around you, and the ");
		System.out.println("limitless sky surrounding your every angle.");
		in.readLine();
		System.out.println("It seems as though you have died..");	
	}
	
}