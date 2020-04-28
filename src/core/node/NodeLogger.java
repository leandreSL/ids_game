package core.node;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import core.logger.Logger;
import share.Player;

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

	@Override
	protected void initialJoin (Player player) {
		super.initialJoin(player);
		
		String message = player + " joins zone " + nodeName;
		
    	System.out.println(message);
		this.log(message);
	}

	@Override
	protected void receivePlayerChangeNode (PlayerGameData playerGameData) {
		super.receivePlayerChangeNode(playerGameData);
		
		String message = playerGameData.getPlayer() + " joins zone " + nodeName;
		
		System.out.println(message);
		this.log(message);
	}

	@Override
	protected void makePlayerChangeNode (Player player, String destinationNode) {
		super.makePlayerChangeNode(player, destinationNode);
		
		String message = player + " quits zone " + nodeName + " for zone " + destinationNode;
		
		System.out.println(message);
		this.log(message);
	}

}
