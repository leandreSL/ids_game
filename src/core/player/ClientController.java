package core.player;

import java.io.IOException;

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
}
