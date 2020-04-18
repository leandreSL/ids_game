package core.player;

public class ClientMain {
	public static void main (String[] args) {
		// args[0] : player name
		// args[1] : destination zone
		Client client = new Client(args[0], args[1], "share.action.BasicActionVisitor");
	}
}
