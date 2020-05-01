package core.player;

import share.action.ActionVisitor;
import share.action.ChangeZone;
import share.action.PlayerMoves;
import share.action.UpdateBoard;
import share.action.UpdateTile;

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
		// TODO : Est-ce que set le board et le save dans ClientDate est utile ? le but est juste de le donner aux observers ?
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
