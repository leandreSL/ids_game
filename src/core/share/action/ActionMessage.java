package core.share.action;

import java.io.Serializable;

public interface ActionMessage extends Serializable {
	abstract public void accept(ActionVisitor visitor);
}
