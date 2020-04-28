package core.network;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import core.node.ByteSerializable;
import share.Player;

public class RabbitWrapper {
	protected static final String EXCHANGE_NAME = "game_exchange";

	Channel channel;

	public RabbitWrapper() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection;

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
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

	public void createQueueAndListen (String topic, DeliverCallback deliverCallback) {
		try {
			// Create the queue and bind it to the topic
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, topic);
			
	        // Subscribe to the topic
	        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void publish (String topic, byte[] message) throws IOException {
		channel.basicPublish(EXCHANGE_NAME, topic, null, message);
	}

}
