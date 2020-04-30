package share.board;

import share.Player;

@SuppressWarnings("serial")
public class TileChangeZone extends Tile {
	String destinationNode;

	public TileChangeZone (int x, int y, String destinationNode) {
		super(x, y);
		this.destinationNode = destinationNode;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public void accept (TileVisitor tileVisitor, Player player) {
		tileVisitor.executeTileAction(this, player);		
	}
	
	public String getDestinationNode() {
		return destinationNode;
	}
}
