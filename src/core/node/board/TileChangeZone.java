package core.node.board;

public class TileChangeZone extends Tile {

	public TileChangeZone(int x, int y, String topic) {
		super(x, y);
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

}
