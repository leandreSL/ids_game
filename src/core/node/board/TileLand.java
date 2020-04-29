package core.node.board;

import share.Player;

@SuppressWarnings("serial")
public class TileLand extends Tile {
	/**
	 * The topic is equal to the player ID or null if there is nothing on the tile
	 */
	protected Player player;

	public TileLand(int x, int y, Player topic) {
		super(x, y);
		this.player = topic;
	}

	@Override
	public boolean isAvailable() {
		if (player == null) return true;
		return false;
	}

	@Override
	public void accept (TileVisitor tileVisitor, Player player) {
		tileVisitor.executeTileAction(this, player);		
	}
}
