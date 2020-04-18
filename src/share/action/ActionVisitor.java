package share.action;

import core.player.ClientData;

public abstract class ActionVisitor {
	ClientData data;
	
	public ActionVisitor (ClientData data) {
		this.data = data;
	}
	
	abstract public void visit(ChangeZone changeZoneMessage);
	abstract public void visit(PlayerJoins playerJoinsMessage);
	abstract public void visit(PlayerLeaves playerLeavesMessage);
	abstract public void visit(SayHello sayHelloMessage);
	abstract public void visit(PlayerMoves playerMovesMessage);
}
