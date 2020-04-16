package share.action;

import core.player.ClientData;
import core.player.Player;

public class SayHello extends Action {

	public SayHello(Player source) {
		super(source);
	}

	@Override
	public void execute (ClientData data) {
		System.out.println(this.getClass().getName());

	}

}
