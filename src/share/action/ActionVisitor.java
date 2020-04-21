package share.action;

public interface ActionVisitor {	
	abstract public void visit(ChangeZone changeZoneMessage);
	abstract public void visit(PlayerJoins playerJoinsMessage);
	abstract public void visit(PlayerLeaves playerLeavesMessage);
	abstract public void visit(SayHello sayHelloMessage);
	abstract public void visit(PlayerMoves playerMovesMessage);
}
