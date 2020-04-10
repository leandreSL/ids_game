package core.node;

public class Tile {
	int x;
	int y;
	String topic;
	
	public Tile (int x, int y, String topic) {
		this.x = x;
		this.x = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
}
