package core.node.board;

import core.share.board.Board;
import core.share.board.Tile;
import core.share.board.TileChangeZone;
import core.share.board.TileLand;
import core.share.board.TileWall;

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
			tiles[y][x] = new TileChangeZone(x, y, "C");
		}
		
		x = 3;
		y = 3;
		tiles[y][x] = new TileWall(x, y);
		x = 3;
		y = 2;
		tiles[y][x] = new TileWall(x, y);
		
		x = width-1;
		y = height-1;
		tiles[y][x] = new TileWall(x, y);
		
		
		return new Board(tiles);
	}

}
