package share.action;

import core.player.ClientData;

public class BasicActionVisitor extends ActionVisitor {

	public BasicActionVisitor(ClientData data) {
		super(data);
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
	public void visit (SayHello sayHelloMessage) {
		System.out.println("SayHello");
	}

	@Override
	public void visit (PlayerMoves playerMovesMessage) {
		System.out.println("PlayerMoves");
	}

}
