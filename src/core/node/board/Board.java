package core.node.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
					((TileLand) tiles[i][j]).player = player;
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
	
	/**
	 * Remove the player from the board
	 * @param player
	 */
	public void removePlayer (Player player) {
		TileLand source = (TileLand) this.playerPositions.remove(player);
		source.player = null;
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

	/**
	 * Update the position of the player, move it to the destination tile.
	 * @param player
	 * @param destination
	 */
	public void movePlayerToTile(Player player, TileLand destination) {
		TileLand source = (TileLand) this.playerPositions.get(player);
		
		destination.player = source.player;
		source.player = null;
		this.playerPositions.put(player, destination);
	}

	public List<Player> playersNearby(Player player) {
		List<Player> playersNearby = new ArrayList<>();
		Tile source = this.playerPositions.get(player);
		
		/*
		 *  For each tile nearby the player's tile (all the tiles around, distance 1)
		 *  Min/max because the player might be on the border of the board
		 */
		for (int i = Math.max(0, source.x - 1); i < Math.min(source.x + 1, width); i++) {
			for (int j = Math.max(0, source.y - 1); j < Math.min(source.y + 1, height); j++) {
				// Continue when the coordinates point the source player itself
				if (source.x == i && source.y == j) continue;
				// If it's not a land there can't be a player on it
				if (!(tiles[i][j] instanceof TileLand)) continue;
				
				TileLand tile = (TileLand) tiles[i][j];
				if (tile.player == null) continue;
				
				playersNearby.add(tile.player);
			}
		}
		
		return playersNearby;
	}
}
