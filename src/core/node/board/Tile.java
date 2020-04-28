package core.node;

public class Tile {
	protected int x;
	protected int y;
	/**
	 * The topic is equal to the player ID or null if there is nothing on the tile
	 */
	protected String topic;
	
	public Tile (int x, int y, String topic) {
		this.x = x;
		this.x = y;
	}
}
