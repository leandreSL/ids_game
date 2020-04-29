package core.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.node.board.Board;
import core.node.board.Tile;
import core.node.board.TileChangeZone;
import core.node.board.TileLand;
import core.node.board.TileVisitor;
import core.node.board.TileWall;
import share.Direction;
import share.Player;
import share.action.ActionMessage;
import share.action.ChangeZone;
import share.action.PlayerJoins;
import share.action.PlayerLeaves;
import share.action.PlayerMoves;


public class Node implements TileVisitor {
	private static final int[] BOARD_SIZE = {7, 7};

	private enum NodeName {A, B, C, D};
	
	protected RabbitWrapper network;
	
	String[] neighbourNodesName;
	NodeName nodeName;
	String stringNodeName;
	Board board;
	List<Player> players_list;
	Map<Player, PlayerGameData> players_data;
	
	
	public Node (String name, String[] neighbours_node_name) {
		NodeName nodeName = null;
		switch (name) {
			case "A":
				nodeName = NodeName.A;
				break;
	
			case "B":
				nodeName = NodeName.B;
				break;
	
			case "C":
				nodeName = NodeName.C;
				break;
	
			case "D":
				nodeName = NodeName.D;
				break;
		}
		this.nodeName = nodeName;
		this.stringNodeName = name;
		this.neighbourNodesName = neighbours_node_name;
		this.board = new Board(BOARD_SIZE[0], BOARD_SIZE[0]);

        this.players_list = new ArrayList<>();
        this.players_data = new HashMap<>();
        this.network = new RabbitWrapper();


		/*
		 * Allows the player to join the node (log in the node) Allows the player to
		 * move on the board
		 */
        this.initQueues();
	}
	
	private void initQueues () {
		// For when a player joins the game
        network.createQueueAndListen(this.nodeName + "_join", (consumerTag, delivery) -> {
            Player player = (Player) ByteSerializable.fromBytes(delivery.getBody());
        	synchronized (this) {
            	this.initialJoin(player);
        	}
        });
        
        // For when the player wants to move
        network.createQueueAndListen(this.nodeName + "_move", (consumerTag, delivery) -> {
        	Direction direction = (Direction) ByteSerializable.fromBytes(delivery.getBody());
        	synchronized (this) {
            	this.move(direction);
        	}
        });
        
        // When a node receives a player from an other node.
        network.createQueueAndListen(this.nodeName + "_change_node", (consumerTag, delivery) -> {
        	synchronized (this) {
        		PlayerGameData player = (PlayerGameData) ByteSerializable.fromBytes(delivery.getBody());
        		this.receivePlayerChangeNode(player);
        	}
        });
	}

	
	/**
	 * When the player joins the game
	 * @param player
	 */
	protected void initialJoin (Player player) {
		board.addPlayer(player);
		this.players_list.add(player);
		this.players_data.put(player, new PlayerGameData(player));
		this.join(player);
	}


	protected void receivePlayerChangeNode(PlayerGameData playerGameData) {
		Player player = playerGameData.getPlayer();

		// TODO : stratégie de placement différent quand le joueur provient d'un noeud voisin ?
		board.addPlayer(player);
		this.players_list.add(player);
		this.players_data.put(player, playerGameData);
		this.join(player);
	}
	
	private void join (Player player) {
		ActionMessage action = new PlayerJoins(player);
		this.broadcastPlayers(action);
	}
	
	private void move (Direction direction) {
		Player player = direction.getPlayer();
		
		if (player == null || !this.players_list.contains(player) || !direction.isValid()) {
			// TODO : si le player n'est pas dans la zone (hackeur méchant pas gentil)
			// ou si la direction n'est pas valide
			return;
		}
    	
		// check the tile is available, i.e that this is a valid displacement, that the tile is empty
		Tile destinationTile = board.getDestinationTile(player, direction);
		if (destinationTile == null) return;

		if (!board.isTileAvailable(destinationTile)) return;
		
		// Execute the tile action. This class (Node) implements TileVisitor, so it is the visitor for the tiles
		destinationTile.accept(this, player);
	}


	/**
	 * Make the player move on the board.
	 * Check
	 */
	@Override
	public void executeTileAction(TileLand destinationTile, Player player) {
		board.movePlayerToTile(player, destinationTile);
		PlayerMoves action = new PlayerMoves(player, destinationTile);
		
		/*
		 * Get the players nearby the player after he moves
		 * If there are at least 1 :
		 * - add them to the list of encountered players
		 * - add the score to the PlayerMoves message
		 */
		List<Player> playersNearby = board.playersNearby(player);
		int score = playersNearby.size(); 
		if (score > 0) {
			this.players_data.get(player).addEncounteredPlayers(playersNearby);
			
			action.setSayHi(score);
			if (score > 1) {
				action.setMessage("Wow !! Hello ! Hello !!!!");
			}
		}
		
		this.broadcastPlayers(action);
	}

	/**
	 * It's a wall, do nothing
	 */
	@Override
	public void executeTileAction(TileWall tile, Player player) {
		return;
	}
	
	@Override
	public void executeTileAction(TileChangeZone tile, Player player) {
		PlayerGameData playerGameData = this.players_data.get(player);
		
		try {
			this.makePlayerChangeNode(player, playerGameData, tile.getDestinationNode());

			this.players_list.remove(player);
			this.players_data.remove(player);
			this.board.removePlayer(player);

			ActionMessage action = new PlayerLeaves(player);
			this.broadcastPlayers(action, player);
			
			action = new ChangeZone(player, tile.getDestinationNode());
			this.sendActionMessageTo(player, action);
		}
		catch (IOException e) {
			// TODO Si le noeud n'existe pas ? ( = pas d'instance active)
			e.printStackTrace();
		}
	}

	
	/// TODO : Attendre une réponse positive de réception ?
	// Ou seulement vérifier que la queue est up ?
	private void makePlayerChangeNode (Player player, PlayerGameData playerGameData, String destinationNode) throws IOException {
		// Make the player join the new node
		network.publish(destinationNode + "_change_node", ByteSerializable.getBytes(playerGameData));

		// Send the new queue node id to the player
		ActionMessage action = new ChangeZone(player, destinationNode);
		this.sendActionMessageTo(player, action);
	}
	
	/**
	 * Send an action to all the players
	 * @param action
	 */
	private void broadcastPlayers (ActionMessage action) {
		byte[] actionBytes = ByteSerializable.getBytes(action);
		
		for (Player player: this.players_list) {
			try {
				network.publish(player.getId(), actionBytes);
			}
			catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}
	
	private void sendActionMessageTo (Player player, ActionMessage action) throws IOException {
		this.network.publish(player.getId(), ByteSerializable.getBytes(action));
	}
	
	/**
	 * Send an action to all the players
	 * @param action
	 * @param excludedPlayer
	 */
	private void broadcastPlayers (ActionMessage action, Player excludedPlayer) {		
		for (Player player: this.players_list) {
			if (player.equals(excludedPlayer)) continue;
			
			try {
				this.sendActionMessageTo(player, action);
			}
			catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}

}
