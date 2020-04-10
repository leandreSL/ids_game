package core.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import core.player.Player;


public class Node {
	private static final int[] BOARD_SIZE = {7, 7};
    private static final String EXCHANGE_NAME = "game_exchange";
	
	String[] neighbourNodesName;
	String nodeName;
	Board board;
	ArrayList<Player> players_list;
	
	Channel channel;
	
	public Node (String name, String[] neighbours_node_name) {
		this.nodeName = name;
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
	        
	        
	        /*
	         * Allows to send messages to neighbour nodes
	         */
	        String[] queueNameNeighbourNodes = new String[neighbourNodesName.length];
	        for (int i = 0; i < neighbourNodesName.length; i++) {
	        	if (neighbourNodesName[i] != null) {
	        		queueNameNeighbourNodes[i] = channel.queueDeclare().getQueue(); // One queue for each neighbour topic
	        		channel.queueBind(queueNameNeighbourNodes[i], EXCHANGE_NAME, neighbourNodesName[i] + "_change_zone");
	        	}
	        }
	        
	       
	        
	        /*String queueNameDisconnect = channel.queueDeclare().getQueue(); // Disconnect clients
	        String queueNameAskHistory = channel.queueDeclare().getQueue(); // For when the client asks for the history
	         
	        channel.queueBind(queueNameSendMessage, EXCHANGE_NAME, "sendMessage");
	        channel.queueBind(queueNameDisconnect, EXCHANGE_NAME, "disconnect");
	        channel.queueBind(queueNameAskHistory, EXCHANGE_NAME, "askHistory");*/
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For when a player wants to log in the node
	 */
	public void initJoinQueue () {
        try {
            String queueNameJoin = channel.queueDeclare().getQueue();
			channel.queueBind(queueNameJoin, EXCHANGE_NAME, this.nodeName + "_initial_join");
			
			
	        /*
	         * Subscribe to the topic "[nodeName]_join"
	         */
	        DeliverCallback deliverCallbackJoin = (consumerTag, delivery) -> {
	        	Player player = (Player) ByteSerializable.fromBytes(delivery.getBody());
	        	this.initialJoin(player);
	        };
	        channel.basicConsume(queueNameJoin, true, deliverCallbackJoin, consumerTag -> {});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	/**
	 * For when the player wants to move
	 */
	private void initMoveQueue() {
		String queueNameMove;
		try {
			queueNameMove = channel.queueDeclare().getQueue();
	        channel.queueBind(queueNameMove, EXCHANGE_NAME, this.nodeName + "_move");
	        
	        DeliverCallback deliverCallbackMove = (consumerTag, delivery) -> {
	        	Direction direction = (Direction) ByteSerializable.fromBytes(delivery.getBody());
	        	this.move(direction);
	        };
	        channel.basicConsume(queueNameMove, true, deliverCallbackMove, consumerTag -> {});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	/**
	 * When the player joins the game for the first time <=> When the player logs in
	 * @param client
	 */
	public void initialJoin (Player player) {
		this.players_list.add(player);
	}
	
	private void move (Direction direction) {
		// TODO Auto-generated method stub
		
	}
	
	private void changeNode (Player player, Node destinationNode) {
		this.players_list.remove(player);
		
		// TODO LA SUITE
	}
}
