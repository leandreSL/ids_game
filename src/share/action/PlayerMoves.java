package share.action;

import core.player.ClientData;
import share.Direction;

public class PlayerMoves extends ActionMessage {
	private Direction direction;

	public PlayerMoves(Direction direction) {
		super(direction.getPlayer());
		this.direction = direction;
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

}
