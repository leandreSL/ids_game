package core.node;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import core.logger.Logger;
import core.player.Player;

public class NodeLogger extends Node {	
	DateTimeFormatter timeFormatter;

	public NodeLogger (String name, String[] neighbours_node_name) {
		super(name, neighbours_node_name);
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	}

	private void log (String message) {
		try {
			channel.basicPublish(EXCHANGE_NAME, Logger.LOGGER_QUEUE_NAME, null, ByteSerializable.getBytes(LocalTime.now().format(timeFormatter) + " - " + nodeName + ": " + message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void join (Player player) {
		super.join(player);
		
		String message = player + " joins zone " + nodeName;
		
    	System.out.println(message);
		this.log(message);
	}

	protected void changeNode (Player player, String destinationNode) {
		super.changeNode(player, destinationNode);
		
		String message = player + " quits zone " + nodeName + " for zone " + destinationNode;
		
		System.out.println(message);
		this.log(message);
	}

}