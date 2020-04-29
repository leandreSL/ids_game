package share.action;

import core.node.board.TileLand;
import core.player.ClientData;
import share.Direction;
import share.Player;

@SuppressWarnings("serial")
public class PlayerMoves extends ActionMessage {
	private static final String BASIC_MESSAGE = "Hello !!";
	
	private final TileLand destinationTile;
	private boolean sayHi;
	private String message;
	private int score;

	public PlayerMoves(Player player, TileLand destinationTile) {
		super(player);
		this.destinationTile = destinationTile;
		this.message = BASIC_MESSAGE;
		this.sayHi = false;
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

	public TileLand getDestinationTile() {
		return destinationTile;
	}
	
	public void setSayHi (int score) {
		this.sayHi = true;
		this.score = score;
	}
	
	public boolean sayHi() {
		return sayHi;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getScore() {
		return score;
	}
}
