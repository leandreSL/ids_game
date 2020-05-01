package share.action;

import share.board.TileLand;

@SuppressWarnings("serial")
public class PlayerMoves implements ActionMessage {
	private static final String BASIC_MESSAGE = "Hello !!";

	private final TileLand sourceTile;
	private final TileLand destinationTile;
	private boolean sayHi;
	private String message;
	private int score;

	public PlayerMoves (TileLand sourceTile, TileLand destinationTile) {
		this.sourceTile = sourceTile;
		this.destinationTile = destinationTile;
		this.message = BASIC_MESSAGE;
		this.sayHi = false;
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}
	
	public TileLand getSourceTile() {
		return sourceTile;
	}

	public TileLand getDestinationTile() {
		return destinationTile;
	}
	
	public void setSayHi (int score) {
		this.sayHi = true;
		this.score = score;
	}
	
	/**
	 * True if there are players nearby the player after he moves.
	 * @return
	 */
	public boolean sayHi() {
		return sayHi;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	/**
	 * Score : how many players are nearby the player.
	 * @return
	 */
	public int getScore () {
		return score;
	}
}
