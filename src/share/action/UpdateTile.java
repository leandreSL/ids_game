package share.action;

import share.board.Tile;;

@SuppressWarnings("serial")
public class UpdateTile implements ActionMessage {
	private Tile tile;

	public UpdateTile (Tile tile) {
		this.tile = tile;
	}
	
	public Tile getTile() {
		return tile;
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}
}
