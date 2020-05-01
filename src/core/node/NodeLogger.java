package core.node;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import core.logger.Logger;
import share.Player;
import share.board.TileChangeZone;
import share.board.TileLand;
import share.board.TileWall;

public class NodeLogger extends Node {	
	DateTimeFormatter timeFormatter;

	public NodeLogger (String name, String[] neighbours_node_name) {
		super(name, neighbours_node_name);
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	}

	private void log (String message) {
		try {
			network.publish(Logger.LOGGER_QUEUE_NAME, ByteSerializable.getBytes(LocalTime.now().format(timeFormatter) + " - " + nodeName + ": " + message));
		}
		catch (IOException e) {
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
	public void executeTileAction(TileLand tile, Player player) {
		super.executeTileAction(tile, player);

		String message = player + " moves to tile " + tile;
		
		System.out.println(message);
		this.log(message);
	}

	@Override
	public void executeTileAction(TileChangeZone tile, Player player) {
		super.executeTileAction(tile, player);

		String message = player + " quits zone " + nodeName + " for zone " + tile.getDestinationNode();
		
		System.out.println(message);
		this.log(message);	
	}

	@Override
	public void executeTileAction(TileWall tile, Player player) {
		super.executeTileAction(tile, player);

		String message = player + " tried to hit the wall " + tile + " !";
		
		System.out.println(message);
		this.log(message);
	}

}
