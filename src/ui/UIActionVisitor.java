package ui;

import share.action.ActionVisitor;
import share.action.ChangeZone;
import share.action.PlayerMoves;
import share.action.UpdateBoard;
import share.action.UpdateTile;

public class UIActionVisitor implements ActionVisitor {

	public UIActionVisitor () {
		//this.javafx = javafx;
	}
	
	@Override
	public void visit(ChangeZone changeZoneMessage) {
		
		
	}

	@Override
	public void visit(PlayerMoves playerMovesMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(UpdateBoard updateBoardMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(UpdateTile updateTileMessage) {
		// TODO Auto-generated method stub
		
	}

}
