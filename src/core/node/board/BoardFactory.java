package core.node.board;

import share.board.Board;

public interface BoardFactory {
	public Board createBoard() throws WrongSizeBoardException;
}
