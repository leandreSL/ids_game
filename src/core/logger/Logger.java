package core.logger;

import core.node.ByteSerializable;
import core.share.RabbitWrapper;

import java.io.IOException;

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

		try {
			this.network = new RabbitWrapper();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.network.createQueueAndListen(LOGGER_QUEUE_NAME, (consumerTag, delivery) -> {
				String message = (String) ByteSerializable.fromBytes(delivery.getBody());
				System.out.println(message);
			});
	}
}
