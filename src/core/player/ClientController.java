package core.player;

import java.io.IOException;

import share.Direction;
import ui.UIActionVisitor;

public class ClientController {
	private Client client;
	
	public ClientController () {
		
	}
	
	public void init (String playerName, String nodeName) {
		try {
			client = new Client(playerName, nodeName);
		}
		catch (IOException e) {
			// Queue du client : �chec de cr�ation
		}
		
		client.addActionVisitorObservable(new UIActionVisitor());
	}

	public void move(int horizontalD, int verticalD)  {
		try {
			this.client.move(horizontalD,verticalD);
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
	}
}
