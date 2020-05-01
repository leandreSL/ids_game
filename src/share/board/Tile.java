package share.board;

import java.io.Serializable;

import share.Player;

@SuppressWarnings("serial")
public abstract class Tile implements Serializable {
	protected int x;
	protected int y;
	
	public Tile (int x, int y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * Returns true if the tile is available, i.e. the player can move on that Tile.
	 * @param player
	 * @return <strong>true</strong> if the tile is available, <strong>false</strong> otherwise.
	 */
	public abstract boolean isAvailable();
	public abstract void accept(TileVisitor tileVisitor, Player player);
	
	public int getX () {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String toString () {
		return "[tile:" + x + "," + y + "]";
	}
}
