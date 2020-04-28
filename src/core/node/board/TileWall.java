package core.node.board;

public class TileWall extends Tile {

	public TileWall(int x, int y, String topic) {
		super(x, y);
	}

	@Override
	public boolean isAvailable() {
		return false;
	}
	
}
