package share.action;

import java.io.Serializable;

import share.Player;

public abstract class ActionMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Player source;
	
	public ActionMessage(Player source) {
		this.source = source;
	}

	public Player getPlayer () {
		return source;
	}
	
	abstract public void accept(ActionVisitor visitor);
}
