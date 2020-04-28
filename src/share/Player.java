package share;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final String name;

	public Player(String playerId, String playerName) {
		this.id = playerId;
		this.name = playerName;
	}

	public String getId () {
		return this.id;
	}

	public String getName () {
		return this.name;
	}
	
	@Override
	public String toString () {
		return "[" + this.id + " - " + this.name + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this.getClass().equals(obj.getClass())) {
			return this.hashCode() == obj.hashCode();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
