package share.action;

import core.player.ClientData;

public class BasicActionVisitor implements ActionVisitor {
	ClientData data;

	public BasicActionVisitor(ClientData data) {
		this.data = data;
	}

	@Override
	public void visit (ChangeZone changeZoneMessage) {
		System.out.println("ChangeZone from zone \"" + this.data.getNodeName() + "\" to zone \"" + changeZoneMessage.nodeName + "\"");
		this.data.setNodeName(changeZoneMessage.nodeName);
	}

	@Override
	public void visit (PlayerJoins playerJoinsMessage) {
		System.out.println("PlayerJoins");
	}

	@Override
	public void visit (PlayerLeaves playerLeavesMessage) {
		System.out.println("PlayerLeaves");
	}

	@Override
	public void visit (PlayerMoves playerMovesMessage) {
		System.out.println("PlayerMoves");
		System.out.println("Coords : " + playerMovesMessage.getDestinationTile());
	}

}
