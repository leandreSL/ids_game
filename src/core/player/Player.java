package core.player;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
	private String id;
	private String name;

	public String getId () {
		return this.id;
	}

	protected void setId(String queueNameId) {
		this.id = queueNameId;
	}
	
	public String getName () {
		return this.name;
	}

	protected void setName (String name) {
		this.name = name;
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
