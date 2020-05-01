package share.board;

import share.Player;

@SuppressWarnings("serial")
public class TileWall extends Tile {

	public TileWall (int x, int y) {
		super(x, y);
	}

	@Override
	public boolean isAvailable() {
		return false;
	}

	@Override
	public void accept (TileVisitor tileVisitor, Player player) {
		tileVisitor.executeTileAction(this, player);		
	}
}
