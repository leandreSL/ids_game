package share.action;

import share.Player;

/**
 * Received when an other player on the board left the zone.
 */
@SuppressWarnings("serial")
public class PlayerLeaves extends ActionMessage {

	public PlayerLeaves(Player source) {
		super(source);
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

}
