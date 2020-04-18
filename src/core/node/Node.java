package core.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import core.node.board.Board;
import core.player.Player;
import share.Direction;
import share.action.ActionMessage;
import share.action.ChangeZone;
import share.action.PlayerJoins;
import share.action.PlayerLeaves;
import share.action.PlayerMoves;


public class Node {
	private static final int[] BOARD_SIZE = {7, 7};
    private static final String EXCHANGE_NAME = "game_exchange";
    

	private enum NodeName {A, B, C, D};
	
	String[] neighbourNodesName;
	NodeName nodeName;
	String stringNodeName;
	Board board;
	ArrayList<Player> players_list;
	
	Channel channel;
	
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

        this.players_list = new ArrayList<Player>();

		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
        
		try {
			connection = factory.newConnection();
	        channel = connection.createChannel();
	        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        
	        /*
	         * Allows the player to join the node (log in the node)
	         * Allows the player to move on the board
	         */
	        this.initJoinQueue();
	        this.initMoveQueue();
		}
		catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
		catch (TimeoutException e) {
			// TODO
			e.printStackTrace();
		}
	}

	/**
	 * For when a player joins the zone
	 */
	public void initJoinQueue () {
        try {
        	// Create the queue and bind it to the topic 
            String queueNameJoin = channel.queueDeclare().getQueue();
			channel.queueBind(queueNameJoin, EXCHANGE_NAME, this.nodeName + "_join");
			
	        /*
	         * Subscribe to the topic "[nodeName]_join"
	         */
	        DeliverCallback deliverCallbackJoin = (consumerTag, delivery) -> {
	        	Player player = (Player) ByteSerializable.fromBytes(delivery.getBody());
	        	System.out.println(player.getName() + " join");
	        	this.join(player);
	        };
	        channel.basicConsume(queueNameJoin, true, deliverCallbackJoin, consumerTag -> {});
		}
        catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	/**
	 * For when the player wants to move
	 */
	private void initMoveQueue() {
		try {
        	// Create the queue and bind it to the topic
			String queueNameMove = channel.queueDeclare().getQueue();
	        channel.queueBind(queueNameMove, EXCHANGE_NAME, this.nodeName + "_move");
	        
	        /*
	         * Subscribe to the topic "[nodeName]_move"
	         */
	        DeliverCallback deliverCallbackMove = (consumerTag, delivery) -> {
	        	Direction direction = (Direction) ByteSerializable.fromBytes(delivery.getBody());
	        	System.out.println(direction.getPlayer().getName() + " move");
	        	this.move(direction);
	        };
	        channel.basicConsume(queueNameMove, true, deliverCallbackMove, consumerTag -> {});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	/**
	 * When the player joins the zone
	 * @param client
	 */
	public void join (Player player) {
		this.players_list.add(player);
		
		/*
		 * TODO :
		 * placer le joueur sur le board
		*/
		ActionMessage action = new PlayerJoins(player);
		this.broadcastPlayers(action);
	}
	
	private void move (Direction direction) {
		Player sourcePlayer = direction.getPlayer();
		
		if (!this.players_list.contains(sourcePlayer)) {
			// TODO : si le player n'est pas dans la zone (hackeur méchant pas gentil)
		}
		
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
			ActionMessage action = new PlayerLeaves(sourcePlayer);
			this.broadcastPlayers(action, sourcePlayer);
			
			// Remove the player from the board
			this.board.removePlayer(sourcePlayer.getId());
			
			if (this.nodeName == NodeName.A) {
				if (direction.getHorizontalDirection() != 0) {
					this.changeNode(sourcePlayer, "B");
				}
				else {
					this.changeNode(sourcePlayer, "D");
				}
			}
			else if (this.nodeName == NodeName.B) {
				if (direction.getHorizontalDirection() != 0) {
					this.changeNode(sourcePlayer, "A");
				}
				else {
					this.changeNode(sourcePlayer, "C");
				}
			}
			else if (this.nodeName == NodeName.C) {
				if (direction.getHorizontalDirection() != 0) {
					this.changeNode(sourcePlayer, "D");
				}
				else {
					this.changeNode(sourcePlayer, "B");
				}
			}
			else if (this.nodeName == NodeName.D) {
				if (direction.getHorizontalDirection() != 0) {
					this.changeNode(sourcePlayer, "C");
				}
				else {
					this.changeNode(sourcePlayer, "A");
				}
			}
		}
	}
	
	private void changeNode (Player player, String destinationNode) {
		this.players_list.remove(player);
		
		try {
			System.out.println("ChangeNode player \"[" + player.getId() + "] " + player.getName() + "\" from zone \"" + this.nodeName + "\" to zone \"" + destinationNode + "\"");
			
			// Make the player join the new node
			channel.basicPublish(EXCHANGE_NAME, destinationNode + "_join", null, ByteSerializable.getBytes(player));

			// Send the new queue node id to the player
			ActionMessage action = new ChangeZone(player, destinationNode);
			channel.basicPublish(EXCHANGE_NAME, player.getId(), null, ByteSerializable.getBytes(action));
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
				channel.basicPublish(EXCHANGE_NAME, player.getId(), null, actionBytes);
			} catch (IOException e) {
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
				channel.basicPublish(EXCHANGE_NAME, player.getId(), null, actionBytes);
			} catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
		}
	}
}
