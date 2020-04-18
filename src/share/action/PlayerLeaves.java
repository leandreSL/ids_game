package share.action;

import core.player.ClientData;
import core.player.Player;

public class PlayerLeaves extends ActionMessage {
	private static final long serialVersionUID = 1L;

	public PlayerLeaves(Player source) {
		super(source);
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

}
