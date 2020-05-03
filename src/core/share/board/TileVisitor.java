package core.share.board;

import core.share.Player;

public interface TileVisitor {
	public void executeTileAction(TileLand tile, Player player);
	public void executeTileAction(TileChangeZone tile, Player player);
	public void executeTileAction(TileWall tile, Player player);
}
