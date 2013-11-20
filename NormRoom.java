import java.util.ArrayList;
import java.util.*;

public class NormRoom extends Room
{
	
	public NormRoom(String name, String description, int l)
	{
		super(name,description,l);
		setNextRoom("south",null);
		setNextRoom("north",null);
		setNextRoom("west",null);
		setNextRoom("east",null);
		setNextRoom("up",null);
		setNextRoom("down",null);
	}
	
	public NormRoom(String name, String description, int l, Room north, Room west, Room east, Room south, Room up, Room down)
	{
		super(name,description,l);
		setNextRoom("south", south);
		setNextRoom("north", north);
		setNextRoom("west", west);
		setNextRoom("east", east);
		setNextRoom("up", up);
		setNextRoom("down", down);
	}
}