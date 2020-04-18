package core.node;

import java.util.HashMap;
import java.util.Map;

public class Board {
	int height;
	int width;
	Tile[][] tiles;
	
	Map<String, Tile> playerPositions;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];
		
		this.playerPositions = new HashMap<String, Tile>();
	}

	/**
	 * @param source              Source tile where the player is located
	 * @param horizontalDirection -1, 0 or 1
	 * @param verticalDirection   -1, 0 or 1
	 * @return
	 */
	public boolean move(Tile source, int horizontalDirection, int verticalDirection) {
		/*int new_coord;
		Tile playerPosition = this.playerPositions.get(source.topic);
		
		// move in vertical direction
		if (horizontalDirection == 0) {
			new_coord = source.y + verticalDirection;

			if (new_coord < 0 || new_coord >= this.height)
				return false;

			this.tiles[source.x][new_coord].topic = source.topic;
			playerPosition.y = new_coord;
		}
		// move in horizontal direction
		else {
			new_coord = source.x + horizontalDirection;

			if (new_coord < 0 || new_coord >= this.width)
				return false;

			this.tiles[new_coord][source.y].topic = source.topic;
			playerPosition.x = new_coord;
		}

		// reset the previous tile
		source.topic = null;*/
		return true;
	}
	
	public void removePlayer (String playerId) {
		/*Tile playerPosition = this.playerPositions.get(playerId);
		this.tiles[playerPosition.x][playerPosition.y].topic = null;
		
		this.playerPositions.remove(playerId);*/
	}
}
