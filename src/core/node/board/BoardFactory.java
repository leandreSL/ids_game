package core.node.board;

import core.share.board.Board;

public interface BoardFactory {
	public Board createBoard() throws WrongSizeBoardException;
}
