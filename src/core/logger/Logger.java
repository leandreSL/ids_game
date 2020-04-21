package core.logger;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import core.TimeoutSetting;
import core.node.ByteSerializable;

/**
 * Logs in the standard output the messages received from the NodeLogger instances.
 */
public class Logger {
	private static final String EXCHANGE_NAME = "game_exchange";
	public static final String LOGGER_QUEUE_NAME = "logger";

	private Channel channel;

	public static void main(String[] args) {
		new Logger();
		
		CompletableFuture.delayedExecutor(TimeoutSetting.TIMEOUT, TimeUnit.SECONDS).execute(() -> {
			System.exit(0);
		});
	}

	public Logger() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection;

		try {
			connection = factory.newConnection();
			this.channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

			this.initLoggerQueue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initLoggerQueue() {
		try {
			// Create the queue and bind it to the topic
			String queueNameLogger = channel.queueDeclare().getQueue();
			channel.queueBind(queueNameLogger, EXCHANGE_NAME, LOGGER_QUEUE_NAME);

			/*
			 * Subscribe to the topic "[queueNameLogger]"
			 */
			DeliverCallback deliverCallbackLogger = (consumerTag, delivery) -> {
				String message = (String) ByteSerializable.fromBytes(delivery.getBody());
				System.out.println(message);
			};
			channel.basicConsume(queueNameLogger, true, deliverCallbackLogger, consumerTag -> {
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
