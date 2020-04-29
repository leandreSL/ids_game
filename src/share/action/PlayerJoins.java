package share.action;

import share.Player;

@SuppressWarnings("serial")
public class PlayerJoins extends ActionMessage {

	public PlayerJoins(Player source) {
		super(source);
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}
}
