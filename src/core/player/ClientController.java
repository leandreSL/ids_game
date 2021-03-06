package core.player;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import ui.PlateauController;
import ui.UIActionVisitor;

public class ClientController {
	private Client client;
	
	public ClientController () {
		
	}
	
	public void init (String playerName, String nodeName, PlateauController ui) throws IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		client = new Client(playerName, nodeName);
		
		client.addActionVisitorObservable(new UIActionVisitor(ui,client.getClientData()));
	}

	public void move(int horizontalD, int verticalD)  {
		try {
			this.client.move(horizontalD,verticalD);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void disconnect(){
		try {
			this.client.disconnect();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
}
