public class FlashLight extends MagicItems// implements Comparable
{
	private boolean light;
	public FlashLight(String n, String d, String x, String r, int a, boolean s)
	{
		super (n,d,x,r,a);
		light = s;
	}
	public boolean getLight()
	{
		return light;
	}
	public void turn(boolean s)
	{
		light = s;
	}

}