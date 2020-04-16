package share;

import java.io.Serializable;

import core.player.Player;
import share.action.Action;

public class Direction implements Serializable {
	Player player;
	
	int horizontalDirection;
	int verticalDirection;
	
	
	public Direction(Player player, int horizontalDirection, int verticalDirection) {
		this.player = player;
		this.horizontalDirection = horizontalDirection;
		this.verticalDirection = verticalDirection;
	}


	public Player getPlayer() {
		return player;
	}


	public int getHorizontalDirection() {
		return horizontalDirection;
	}


	public int getVerticalDirection() {
		return verticalDirection;
	}
}
