package share.action;

import core.player.Player;

public class ChangeZone extends ActionMessage {
	private static final long serialVersionUID = 1L;
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
