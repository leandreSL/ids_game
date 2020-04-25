package core.player;

import share.Player;

public class ClientData {
	Player player;
	String nodeName;
	
	public ClientData(Player player, String nodeName) {
		this.player = player;
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Player getPlayer() {
		return player;
	}
}
