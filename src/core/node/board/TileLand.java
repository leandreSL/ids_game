package core.node.board;

public class TileLand extends Tile {
	/**
	 * The topic is equal to the player ID or null if there is nothing on the tile
	 */
	protected String topic;

	public TileLand(int x, int y, String topic) {
		super(x, y);
		this.topic = topic;
	}

	@Override
	public boolean isAvailable() {
		if (topic == null) return true;
		return false;
	}

}
