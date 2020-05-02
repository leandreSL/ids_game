package core.node;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import share.Player;

public class PlayerGameData implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Player player;
	private final Set<Player> playersEncountered;
	private final String consumerTag; // Unique ID provided by RabbitMQ

	public PlayerGameData(Player player, String consumerTag) {
		this.player = player;
		this.playersEncountered = new HashSet<>();
		this.consumerTag = consumerTag;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public String getId () {
		return this.player.getId();
	}
	
	public String getConsumerTag() {
		return consumerTag;
	}

	/**
	 * Add all the players nearby to the list of all the players encountered.
	 * Duplicates are not added.
	 * @param playersNearby
	 */
	public void addEncounteredPlayers(List<Player> playersNearby) {
		this.playersEncountered.addAll(playersNearby);
	}	

	public int getEncounteredPlayersNumber () {
		return this.playersEncountered.size();
	}
}
