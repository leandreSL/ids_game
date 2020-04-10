package core.player;

import java.io.Serializable;

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

}
