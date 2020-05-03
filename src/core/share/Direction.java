package core.share;

import java.io.Serializable;

@SuppressWarnings("serial")
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
	
	/**
	 * Checks the direction is valid, i.e only one of horizontal/vertical is set to 0 and the other one is set to -1 or 1.
	 * @return <strong>true</strong> if the direction is valid, <strong>false</strong> otherwise.
	 */
	public boolean isValid () {
		if (horizontalDirection == 0 && (verticalDirection == -1 || verticalDirection == 1)) return true;
		if (verticalDirection == 0 && (horizontalDirection == -1 || horizontalDirection == 1)) return true;
		return false;
	}
}
