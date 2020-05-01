package core.node.board;

import share.board.Board;
import share.board.Tile;
import share.board.TileChangeZone;
import share.board.TileLand;
import share.board.TileWall;

public class BoardFactoryA implements BoardFactory {

	@Override
	public Board createBoard () throws WrongSizeBoardException {
		int width = 7,
			height = 7;
		Tile[][] tiles = new Tile[width][height];
		int x, y;
		

		for (y = 0; y < height-1; y++) {
			for (x = 0; x < width-1; x++) {
				tiles[y][x] = new TileLand(x, y);
			}
		}
		
		x = width - 1;
		for (y = 0; y < height-1; y++) {
			tiles[y][x] = new TileChangeZone(x, y, "B");
		}
		
		y = height - 1;
		for (x = 0; x < width-1; x++) {
			tiles[y][x] = new TileChangeZone(x, y, "D");
		}
		
		x = width-1;
		y = height-1;
		tiles[y][x] = new TileWall(x, y);
		
		
		return new Board(tiles);
	}

}
