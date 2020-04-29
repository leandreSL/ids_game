package core.node.board;

import share.Player;

@SuppressWarnings("serial")
public class TileWall extends Tile {

	public TileWall(int x, int y, String topic) {
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
