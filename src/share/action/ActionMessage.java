package share.action;

import java.io.Serializable;

import share.Player;

@SuppressWarnings("serial")
public abstract class ActionMessage implements Serializable {
	
	private Player source;
	
	public ActionMessage(Player source) {
		this.source = source;
	}

	public Player getPlayer () {
		return source;
	}
	
	abstract public void accept(ActionVisitor visitor);
}
