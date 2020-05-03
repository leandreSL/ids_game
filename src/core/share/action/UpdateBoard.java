package core.share.action;

import core.share.board.Board;

/**
 * Received when the player changes zone.
 */
@SuppressWarnings("serial")
public class UpdateBoard implements ActionMessage {
	private Board board;

	public UpdateBoard (Board board) {
		this.board = board;
	}
	
	public Board getBoard() {
		return board;
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}
}
