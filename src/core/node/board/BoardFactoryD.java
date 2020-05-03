package core.node.board;

import core.share.board.Board;
import core.share.board.Tile;
import core.share.board.TileChangeZone;
import core.share.board.TileLand;

public class BoardFactoryD implements BoardFactory {

	@Override
	public Board createBoard () throws WrongSizeBoardException {
		int width = 7,
			height = 7;
		Tile[][] tiles = new Tile[width][height];
		int x, y;
		

		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {
				tiles[y][x] = new TileLand(x, y);
			}
		}
		

		x = 3;
		y = 0;
		tiles[y][x] = new TileChangeZone(x, y, "B");
		
		x = 0;
		y = 3;
		tiles[y][x] = new TileChangeZone(x, y, "C");
		
		return new Board(tiles);
	}

}
