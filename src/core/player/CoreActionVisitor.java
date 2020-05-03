package core.player;

import core.share.action.ActionVisitor;
import core.share.action.ChangeZone;
import core.share.action.PlayerMoves;
import core.share.action.UpdateBoard;
import core.share.action.UpdateTile;

public class CoreActionVisitor implements ActionVisitor {
	ClientData data;

	public CoreActionVisitor(ClientData data) {
		this.data = data;
	}

	@Override
	public void visit (ChangeZone changeZoneMessage) {
		System.out.println("ChangeZone from zone \"" + this.data.getNodeName() + "\" to zone \"" + changeZoneMessage.getNodeName() + "\"");
		this.data.setNodeName(changeZoneMessage.getNodeName());
	}

	@Override
	public void visit (UpdateBoard updateBoardMessage) {
		System.out.println("UpdateBoard");
		this.data.setBoard(updateBoardMessage.getBoard());
	}

	@Override
	public void visit (UpdateTile updateTileMessage) {
		System.out.println("UpdateTile");
		this.data.getBoard().updateTile(updateTileMessage.getTile());
	}

	@Override
	public void visit (PlayerMoves playerMovesMessage) {
		System.out.println("PlayerMoves");
		System.out.println("Coords : " + playerMovesMessage.getDestinationTile());
		this.data.getBoard().updateTile(playerMovesMessage.getSourceTile());
		this.data.getBoard().updateTile(playerMovesMessage.getDestinationTile());
	}

}
