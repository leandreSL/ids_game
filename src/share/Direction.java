package share;

import java.io.Serializable;

public class Direction implements Serializable {
	private static final long serialVersionUID = 1L;

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
