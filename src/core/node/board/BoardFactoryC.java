package core.node.board;

import share.board.Board;
import share.board.Tile;
import share.board.TileChangeZone;
import share.board.TileLand;

public class BoardFactoryC implements BoardFactory {

	@Override
	public Board createBoard () throws WrongSizeBoardException {
		int width = 7,
			height = 7;
		Tile[][] tiles = new Tile[width][height];
		int x, y;
		

		for (y = 0; y < height; y++) {
			for (x = 1; x < width; x++) {
				tiles[y][x] = new TileLand(x, y);
			}
		}
		

		x = 3;
		y = 0;
		tiles[y][x] = new TileChangeZone(x, y, "B");
		
		x = 0;
		y = 3;
		tiles[y][x] = new TileChangeZone(x, y, "D");
		
		return new Board(tiles);
	}

}
