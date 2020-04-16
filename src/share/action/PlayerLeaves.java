package share.action;

import core.player.ClientData;
import core.player.Player;

public class PlayerLeaves extends Action {

	public PlayerLeaves(Player source) {
		super(source);
	}

	@Override
	public void execute (ClientData data) {
		System.out.println(this.getClass().getName());
	}

}
