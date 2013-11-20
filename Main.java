import java.io.*;

public class Main
{
	public Main()
	{
		
	}
	
	public static void main (String args[]) throws IOException
	{
		Game game = null;
		
		System.out.println("************  Welcome to Adventure Redux! (v1.5)  ***********");
		System.out.println("*                                                           *");
		System.out.println("* This game is located at a school-based setting, so if you *");
		System.out.println("* are new, the characters and some of the rooms may not be  *");
		System.out.println("* familiar to you, but nevertheless dont forget to have fun!*");
		System.out.println("*                                                           *");
		System.out.println("*                                                           *");
		System.out.println("*                                                           *");
		System.out.println("* Changes:                                                  *");
		System.out.println("*      - Random droppings of both teachers and potions      *");
		System.out.println("*        around the school                                  *");
		System.out.println("*                                                           *");
		System.out.println("*************************************************************");
		System.out.println();
		String name = "";
		boolean read = false;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
		int quest = 0;
        
        
        String str = "";
            if (str != null) {
            	if (quest == 0)
            	{
            		System.out.print("What is your name? ");
            		quest++;
            		str = in.readLine();
					name = str;
					game = new Game(name);
            	}
                if (quest == 1)
                {
                	System.out.print("Do you want to know the story, " + name + "? ");
                	str = in.readLine();
                	if (str.substring(0,1).equalsIgnoreCase("y"))
                	{
                		System.out.println("\n" + game.readStory() + "\n");
                		read = true;
                	}
                	else
                		System.out.println();
                }
            }
        } catch (IOException e) { }
		if (read)
		{
			System.out.println("Press ENTER to continue...");
			in.readLine();
		}
		game.play();
	}	
}