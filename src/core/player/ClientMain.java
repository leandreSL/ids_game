package core.player;

import java.io.IOException;

public class ClientMain {
	public static void main (String[] args) {
		// args[0] : player name
		// args[1] : destination zone
		
		try {
			Client client = new Client(args[0], args[1]);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println(">>> Connection error to the node " + args[1] + " <<<");
		}
	}
}
