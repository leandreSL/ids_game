package core.node;


public class Board {
	int height;
	int width;
	Tile[][] tiles;
	
	
	public Board (int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];
	}
	
	/**
	 * @param source Source tile where the player is located
	 * @param horizontalDirection -1, 0 or 1
	 * @param verticalDirection -1, 0 or 1
	 * @return
	 */
	public boolean move (Tile source, int horizontalDirection, int verticalDirection) {
		int new_coord;

		// move in vertical direction
		if (horizontalDirection == 0) {
			new_coord = source.getY() + verticalDirection;
			
			if (new_coord < 0 || new_coord >= this.height) return false;
			
			this.tiles[source.getX()][new_coord].setTopic(source.getTopic());
		}
		// move in horizontal direction
		else {
			new_coord = source.getX() + horizontalDirection;
			
			if (new_coord < 0 || new_coord >= this.width) return false;
			
			this.tiles[new_coord][source.getY()].setTopic(source.getTopic());
		}

		//reset the previous tile
		source.setTopic(null);
		return true;
	}
}
