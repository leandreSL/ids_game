package core.node;

import java.io.Serializable;

import share.Player;

public class PlayerGameData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Player player;

	public PlayerGameData(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public String getId () {
		return this.player.getId();
	}
	

}
