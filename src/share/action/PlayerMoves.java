package share.action;

import core.player.ClientData;
import share.Direction;

public class PlayerMoves extends Action {
	private Direction direction;

	public PlayerMoves(Direction direction) {
		super(direction.getPlayer());
		this.direction = direction;
	}

	@Override
	public void execute (ClientData data) {

		System.out.println(this.getClass().getName());
	}

}
