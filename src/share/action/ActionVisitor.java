package share.action;

public interface ActionVisitor {	
	public void visit(ChangeZone changeZoneMessage);
	public void visit(PlayerMoves playerMovesMessage);
	public void visit(UpdateBoard updateBoardMessage);
	public void visit(UpdateTile updateTileMessage);
}
