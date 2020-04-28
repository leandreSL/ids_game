package core.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import core.network.RabbitWrapper;
import core.node.board.Board;
import core.node.board.Tile;
import share.Direction;
import share.Player;
import share.action.ActionMessage;
import share.action.ChangeZone;
import share.action.PlayerJoins;
import share.action.PlayerLeaves;
import share.action.PlayerMoves;


public class Node {
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
        	this.initialJoin(player);
        });
        
        // For when the player wants to move
        network.createQueueAndListen(this.nodeName + "_move", (consumerTag, delivery) -> {
        	Direction direction = (Direction) ByteSerializable.fromBytes(delivery.getBody());
        	System.out.println(direction.getPlayer().getName() + " move");
        	this.move(direction);
        });
        
        // When a node receives a player from an other node.
        network.createQueueAndListen(this.nodeName + "_change_node", (consumerTag, delivery) -> {
        	PlayerGameData player = (PlayerGameData) ByteSerializable.fromBytes(delivery.getBody());
        	this.receivePlayerChangeNode(player);
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
		Tile destination = board.getDestinationTile(player, direction);
		if (destination == null) return;

		if (!board.isTileAvailable(destination)) return;

		//ActionMessage action = board.moveToTileAndGetActionMessage();
		
		/*
		 * TODO : faire bouger le player sur le board (selon règles / contraintes)
		 * - Si déplacement normal et valide : envoyer sa nouvelle position a tous les joueurs
		 * - Si collision : afficher le "Bonjour" à tous les joueurs et incrémenter le compteur de bonjour de l'initiateur
		 */
		
		/* Si déplacement valide (pas de sortie de plateau etc) && déplacement simple (pas de changement de noeud) && pas de collision
		 * 
			// Broadcast the move action to the other players
			Action action = new PlayerMoves(direction);
			this.broadcastPlayers(action);
		 */
		

		// TODO : clean ce code dégueulasse à base de ifs
		// Si déplacement = changement de noeud
		if (true) {
			ActionMessage action = new PlayerLeaves(player);
			this.broadcastPlayers(action, player);
			
			// Remove the player from the board
			this.board.removePlayer(player.getId());
			
			if (this.nodeName == NodeName.A) {
				if (direction.getHorizontalDirection() != 0) {
					this.makePlayerChangeNode(player, "B");
				}
				else {
					this.makePlayerChangeNode(player, "D");
				}
			}
			else if (this.nodeName == NodeName.B) {
				if (direction.getHorizontalDirection() != 0) {
					this.makePlayerChangeNode(player, "A");
				}
				else {
					this.makePlayerChangeNode(player, "C");
				}
			}
			else if (this.nodeName == NodeName.C) {
				if (direction.getHorizontalDirection() != 0) {
					this.makePlayerChangeNode(player, "D");
				}
				else {
					this.makePlayerChangeNode(player, "B");
				}
			}
			else if (this.nodeName == NodeName.D) {
				if (direction.getHorizontalDirection() != 0) {
					this.makePlayerChangeNode(player, "C");
				}
				else {
					this.makePlayerChangeNode(player, "A");
				}
			}
		}
	}
	
	protected void makePlayerChangeNode (Player player, String destinationNode) {
		this.players_list.remove(player);	
		PlayerGameData playerGameData = this.players_data.get(player);
		
		try {
			// Make the player join the new node
			network.publish(destinationNode + "_change_node", ByteSerializable.getBytes(playerGameData));

			// Send the new queue node id to the player
			ActionMessage action = new ChangeZone(player, destinationNode);
			network.publish(player.getId(), ByteSerializable.getBytes(action));
		} catch (IOException e) {
			// TODOs
			e.printStackTrace();
		}
	}
	
	/**
	 * Send an action to all the players
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
	
	/**
	 * Send an action to all the players
	 */
	private void broadcastPlayers (ActionMessage action, Player excludedPlayer) {
		byte[] actionBytes = ByteSerializable.getBytes(action);
		
		for (Player player: this.players_list) {
			if (player.equals(excludedPlayer)) continue;
			
			try {
				network.publish(player.getId(), actionBytes);
			}
			catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}
}
