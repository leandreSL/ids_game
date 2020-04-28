package core.node.board;

public abstract class Tile {
	protected int x;
	protected int y;
	
	public Tile (int x, int y) {
		this.x = x;
		this.x = y;
	}


	/**
	 * Returns true if the tile is available, i.e. the player can move on that Tile.
	 * @param player
	 * @return <strong>true</strong> if the tile is available, <strong>false</strong> otherwise.
	 */
	public abstract boolean isAvailable();
}
