package core.player;

import share.Player;
import share.board.Board;

public class ClientData {
	private Player player;
	private String nodeName;
	private Board board;
	
	public ClientData(Player player, String nodeName) {
		this.player = player;
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}
	
	public void setNodeName (String nodeName) {
		this.nodeName = nodeName;
	}

	public Player getPlayer() {
		return player;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void setBoard(Board board) {
		this.board = board;
	}
}
