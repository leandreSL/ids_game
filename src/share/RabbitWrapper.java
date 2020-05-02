package share;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * RabbitMQ Wrapper for network messaging. Allows to subscribe to topics and publish messages on topics.
 */
public class RabbitWrapper {
	protected static final String EXCHANGE_NAME = "game_exchange";

	Channel channel;

	public RabbitWrapper () throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection;

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
		}
		catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a queue, bind it to the topic and add the callback to listen on it.
	 * @param topic
	 * @param deliverCallback
	 */
	public void createQueueAndListen (String topic, DeliverCallback deliverCallback) {
		try {
			// Create the queue and bind it to the topic
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, topic);
			
	        // Subscribe to the topic
	        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a queue, bind it to the topic that is actually the name of the queue itself and add the callback to listen to it.
	 * @param deliverCallback
	 * @return the queue name, i.e. the player id
	 * @throws IOException
	 */
	public String createClientQueueAndListen (DeliverCallback deliverCallback) throws IOException {
			// Create the queue and bind it to the topic
			// queueName will be the client id
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, queueName);
			
	        // Subscribe to the topic
	        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
	        
	        return queueName;
	}
	
	/**
	 * Publish a message (byte array) with a topic.
	 * @param topic
	 * @param message
	 * @throws IOException
	 */
	public void publish (String topic, byte[] message) throws IOException {
		channel.basicPublish(EXCHANGE_NAME, topic, null, message);
	}

}
