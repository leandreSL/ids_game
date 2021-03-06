package core.share.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.node.board.WrongSizeBoardException;
import core.share.Direction;
import core.share.Player;

@SuppressWarnings("serial")
public class Board implements Serializable {
	final int height;
	final int width;


	final Tile[][] tiles;
	
	final Map<Player, Tile> playerPositions;
	
	public Board (Tile[][] tiles) throws WrongSizeBoardException {
		height = tiles.length;
		if (height < 1) throw new WrongSizeBoardException("Height but me greater than zero.");
		
		width = tiles[0].length;
		if (width < 1) throw new WrongSizeBoardException("Width but me greater than zero.");
		
		this.tiles = tiles;
		this.playerPositions = new HashMap<>();
	}
	
	public void addPlayer (Player player) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (tiles[i][j] instanceof TileLand && tiles[i][j].isAvailable()) {
					((TileLand) tiles[i][j]).player = player;
					this.playerPositions.put(player, tiles[i][j]);
					
					return;
				}
			}
		}
	}
	
	/**
	 * Remove the player from the board
	 * @param player
	 */
	public void removePlayer (Player player) {
		TileLand source = (TileLand) this.playerPositions.remove(player);
		source.player = null;
	}

	public TileLand getPlayerTile(Player player) {
		return (TileLand) this.playerPositions.get(player);
	}
	
	public Tile getDestinationTile (Player player, Direction direction) {
		try {
			// Get the Tile where the player is located on.
			Tile source = getPlayerTile(player);
			
			// Get the destination tile
			Tile destination = this.tiles[source.y + direction.getVerticalDirection()][source.x + direction.getHorizontalDirection()];
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
		TileLand source = (TileLand) getPlayerTile(player);
		
		destination.player = source.player;
		source.player = null;
		this.playerPositions.put(player, destination);
	}

	public List<Player> playersNearby(Player player) {
		List<Player> playersNearby = new ArrayList<>();
		Tile source = getPlayerTile(player);
		
		/*
		 *  For each tile nearby the player's tile (all the tiles around, distance 1)
		 *  Min/max because the player might be on the border of the board
		 */
		for (int i = Math.max(0, source.y - 1); i < Math.min(source.y + 1, height); i++) {
			for (int j = Math.max(0, source.x - 1); j < Math.min(source.x + 1, width); j++) {
				// Continue when the coordinates point the source player itself
				if (source.x == j && source.y == i) continue;
				// If it's not a land there can't be a player on it
				if (!(tiles[i][j] instanceof TileLand)) continue;
				
				TileLand tile = (TileLand) tiles[i][j];
				if (tile.player == null) continue;
				
				playersNearby.add(tile.player);
			}
		}
		
		return playersNearby;
	}
	
	public void updateTile (Tile tile) {
		try {
			this.tiles[tile.y][tile.x] = tile;
		}
		catch (NullPointerException | IndexOutOfBoundsException e) {
			// If for any reason tile.x or tile.y is out of bounds or if the tile is null
			return;
		}
	}

	public Tile[][] getTiles() {
		return tiles;
	}

}
