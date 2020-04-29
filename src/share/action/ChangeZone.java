package share.action;

import share.Player;

/**
 * Received when the player changes zone.
 */
@SuppressWarnings("serial")
public class ChangeZone extends ActionMessage {
	protected String nodeName;

	public ChangeZone(Player source, String nodeName) {
		super(source);
		this.nodeName = nodeName;
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

}
