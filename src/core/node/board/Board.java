package core.node.board;

import java.util.HashMap;
import java.util.Map;

import share.Direction;
import share.Player;

public class Board {
	int height;
	int width;
	Tile[][] tiles;
	
	Map<Player, Tile> playerPositions;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];
		
		this.init();
		
		this.playerPositions = new HashMap<>();
	}
	
	private void init () {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tiles[i][j] = new TileLand(i, i, null);
			}
		}
	}
	
	public void addPlayer (Player player) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				
				// TODO : changer l'implémentation ?
				// TODO : Aussi, vérifier s'il n'y a pas un joueur dans les parages (pour dire "Bonjour" et inc le compteur)
				if (tiles[i][j] instanceof TileLand && tiles[i][j].isAvailable()) {
					((TileLand) tiles[i][j]).topic = player.getId();
					this.playerPositions.put(player, tiles[i][j]);
					
					return;
				}
			}
		}
	}


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
	
	public Tile getDestinationTile (Player player, Direction direction) {
		try {
			// Get the Tile where the player is located on.
			Tile source = this.playerPositions.get(player);
			
			// Get the destination tile
			Tile destination = this.tiles[source.x + direction.getHorizontalDirection()][source.y + direction.getVerticalDirection()];
			return destination;
		}
		catch (NullPointerException | IndexOutOfBoundsException e) {
			/*
			 * If source tile is null
			 * If the destination tile is out of bounds 
			 */
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Checks if a tile is available, i.e. the player can move on that Tile.
	 * @return <strong>true</strong> if the tile is available, <strong>false</strong> otherwise.
	 */
	public boolean isTileAvailable(Tile destination) {
		return destination.isAvailable();
	}
}
