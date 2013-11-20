import java.util.Random;

public class MagicItems extends Items// implements Comparable
{
	private int attackValue;
	private boolean light;
	private int randAtk;
	private String ID;
	
	public MagicItems(String ID, String name, String desc, String grname, String rem, int atck)
	{
		super (ID,name,desc,grname,rem);
		this.ID = ID;
		attackValue = atck;
		light = false;
	}
	public MagicItems(String ID, String n, String d, String x, String r, int a, boolean s)
	{
		super (ID,n,d,x,r);
		this.ID = ID;
		attackValue = a;		
		light = s;
	}
	public int attack(Creatures c)
	{
		randAtk = ((attackValue/10)*(rand.nextInt(8) + 2));
		c.changeHealth(-randAtk);
		return randAtk;
	}
	public boolean isItems()
	{
		return false;
	}
	public boolean getLight()
	{
		return light;
	}
	public void turn(boolean s)
	{
		light = s;
	}
	
	public String toString()
	{
		return "Name:\t\t" + name + "\n"+ "Description:\t" + descrip + "\n" + "Attack:\t\t" + attackValue;
	}
	
	public int getStock()
	{
		return 0;
	}
	
}