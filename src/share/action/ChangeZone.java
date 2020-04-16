package share.action;

import core.player.ClientData;
import core.player.Player;

public class ChangeZone extends Action {
	String nodeName;

	public ChangeZone(Player source, String nodeName) {
		super(source);
		this.nodeName = nodeName;
	}

	@Override
	public void execute (ClientData data) {
		System.out.println(this.getClass().getName());
		data.setNodeName(nodeName);
	}

}
