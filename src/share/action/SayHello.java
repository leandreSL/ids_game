package share.action;

import share.Player;

public class SayHello extends ActionMessage {
	private static final long serialVersionUID = 1L;

	public SayHello(Player source) {
		super(source);
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

}
