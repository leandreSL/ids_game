package core.share.board;

import core.share.Player;

@SuppressWarnings("serial")
public class TileLand extends Tile {
	/**
	 * player is null if there is nothing on the tile
	 */
	protected Player player;

	public TileLand (int x, int y) {
		super(x, y);
		this.player = null;
	}
	
	public TileLand (int x, int y, Player player) {
		super(x, y);
		this.player = player;
	}

	@Override
	public boolean isAvailable () {
		if (player == null) return true;
		return false;
	}

	@Override
	public void accept (TileVisitor tileVisitor, Player player) {
		tileVisitor.executeTileAction(this, player);		
	}
	
	@Override
	public String toString () {
		if (player != null){
			return "[tile:" + x + "," + y + "," + player.getName() + "]";
		}else {
			return "[tile:" + x + "," + y + "]";
		}

	}

	public Player getPlayer() {
		return player;
	}
}
