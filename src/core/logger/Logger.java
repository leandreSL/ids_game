package core.logger;

import core.node.ByteSerializable;
import share.RabbitWrapper;

/**
 * Logs in the standard output the messages received from the NodeLogger instances.
 */
public class Logger {
	public static final String LOGGER_QUEUE_NAME = "logger";

	private RabbitWrapper network;

	public static void main(String[] args) {
		new Logger();
	}

	public Logger() {

		this.network = new RabbitWrapper();
		this.network.createQueueAndListen(LOGGER_QUEUE_NAME, (consumerTag, delivery) -> {
				String message = (String) ByteSerializable.fromBytes(delivery.getBody());
				System.out.println(message);
			});
	}
}
