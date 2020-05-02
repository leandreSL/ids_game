package core.node.board;

import share.board.Board;
import share.board.Tile;
import share.board.TileChangeZone;
import share.board.TileLand;

public class BoardFactoryB implements BoardFactory {

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
		
		x = 0;
		for (y = 2; y < height-2; y++) {
			tiles[y][x] = new TileChangeZone(x, y, "A");
		}
		
		y = height - 1;
		for (x = 2; x < width-2; x++) {
			tiles[y][x] = new TileChangeZone(x, y, "D");
		}
		
		
		return new Board(tiles);
	}

}
