package core.logger;

import core.share.ByteSerializable;
import core.share.RabbitWrapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Logs in the standard output the messages received from the NodeLogger instances.
 */
public class Logger {
	public static final String LOGGER_QUEUE_NAME = "logger";

	private RabbitWrapper network;

	public static void main(String[] args) {
		new Logger(args);
	}

	public Logger(String[] args) {
    	if (args.length == 1) RabbitWrapper.setURI(args[0]);
		try {
			this.network = new RabbitWrapper();
		}
		catch (Exception e) {
			System.err.println("Echec de la connection a RabbitMQ,");
			return;
		}	
				
		this.network.createQueueAndListen(LOGGER_QUEUE_NAME, (consumerTag, delivery) -> {
			String message = (String) ByteSerializable.fromBytes(delivery.getBody());
			System.out.println(message);
		});
		
		System.out.println("Logger");
	}
}
