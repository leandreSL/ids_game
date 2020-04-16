package share.action;

import java.io.Serializable;

import core.player.ClientData;
import core.player.Player;

public abstract class Action implements Serializable {
	protected Player source;
	
	public Action(Player source) {
		this.source = source;
	}

	abstract public void execute(ClientData data);

	public Player getPlayer () {
		return source;
	}
}
