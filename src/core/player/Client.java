package core.player;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import core.node.ByteSerializable;
import share.Direction;
import share.action.ActionMessage;
import share.action.ActionVisitor;
import share.action.BasicActionVisitor;
import share.action.PlayerMoves;

public class Client {
    private static final String EXCHANGE_NAME = "game_exchange";
    
    private ClientData data;
    private ActionVisitor actionVisitor;

	Channel channel;
	
	public Client(String playerName, String nodeName, String actionVisitorClassName) {
		this.data = new ClientData(new Player(), nodeName);
		this.data.player.setName(playerName);
		
		
		try {
			Class<?> actionVisitorClass = Class.forName(actionVisitorClassName);
			Constructor<?> actionVisitorConstructor = actionVisitorClass.getConstructor(ClientData.class);
			this.actionVisitor = (ActionVisitor) actionVisitorConstructor.newInstance(this.data);
		}
		catch (Exception e) {
			e.printStackTrace();
			
			// If the class invocation fails, instantiate the basic visitor
			this.actionVisitor = new BasicActionVisitor(this.data);
		}
		
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
	        	ActionMessage actionMessage = (ActionMessage) ByteSerializable.fromBytes(delivery.getBody());
	        	actionMessage.accept(this.actionVisitor);
	        };
	        channel.basicConsume(queueNamePlayer, true, deliverCallbackPlayer, consumerTag -> {});
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
