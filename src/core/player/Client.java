package core.player;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import core.node.ByteSerializable;
import share.Direction;
import share.action.Action;
import share.action.PlayerMoves;

public class Client {
    private static final String EXCHANGE_NAME = "game_exchange";
    
    private ClientData data;

	Channel channel;
	
	public Client(String playerName, String nodeName) {
		this.data = new ClientData(new Player(), nodeName);
		this.data.player.setName(playerName);
		
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection;
        
		try {
			connection = factory.newConnection();
	        channel = connection.createChannel();
	        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        
	        this.initPlayerQueue();
	        this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_join", null, ByteSerializable.getBytes(this.data.player));
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println();
	        this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_move", null, ByteSerializable.getBytes(new Direction(this.data.player, 1, 0)));
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println();
	        this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_move", null, ByteSerializable.getBytes(new Direction(this.data.player, 1, 0)));
	        
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println();
	        this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_move", null, ByteSerializable.getBytes(new Direction(this.data.player, 1, 0)));
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

	private void initPlayerQueue() {
		try {
        	// Create the queue and bind it to the topic 
            String queueNamePlayer = channel.queueDeclare().getQueue();
			channel.queueBind(queueNamePlayer, EXCHANGE_NAME, queueNamePlayer);
			
			this.data.player.setId(queueNamePlayer);
			
	        /*
	         * Subscribe to the topic "[queueNamePlayer]"
	         */
	        DeliverCallback deliverCallbackPlayer = (consumerTag, delivery) -> {
	        	Action action = (Action) ByteSerializable.fromBytes(delivery.getBody());
	        	action.execute(this.data);
	        };
	        channel.basicConsume(queueNamePlayer, true, deliverCallbackPlayer, consumerTag -> {});
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
