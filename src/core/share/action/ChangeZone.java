package core.share.action;

/**
 * Received when the player changes zone.
 */
@SuppressWarnings("serial")
public class ChangeZone implements ActionMessage {
	private String nodeName;

	public ChangeZone(String nodeName) {
		this.nodeName = nodeName;
	}

	@Override
	public void accept(ActionVisitor visitor) {
		visitor.visit(this);
	}

	public String getNodeName() {
		return nodeName;
	}
}
