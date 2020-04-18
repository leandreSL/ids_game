package share.action;

import core.player.Player;

public class PlayerJoins extends ActionMessage {
	private static final long serialVersionUID = 1L;

	public PlayerJoins(Player source) {
		super(source);
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}
}
