package core.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import core.node.ByteSerializable;
import share.Direction;
import share.Player;
import share.action.ActionMessage;
import share.action.ActionVisitor;

import share.RabbitWrapper;

public class Client {

	final private ClientData data;
	private ActionVisitor coreActionVisitor;
	/**
	 * Synchronized observable set: set of ActionVisitor instances that will be
	 * notified when ActionMessages are received - "Synchronized" because multiple
	 * thread will access the set - "Set" because we want to ensure there are no
	 * duplicate observers
	 */
	private Set<ActionVisitor> actionVisitorsObservers;
	private RabbitWrapper network;

	public Client(String playerName, String nodeName) throws IOException {
		this.network = new RabbitWrapper();
		
		String playerId = this.initPlayerQueue();
		this.data = new ClientData(new Player(playerId, playerName), nodeName);
		
		this.coreActionVisitor = new CoreActionVisitor(data);
		this.actionVisitorsObservers = Collections.synchronizedSet(new HashSet<ActionVisitor>());
		this.network.publish(this.data.getNodeName() + "_join", ByteSerializable.getBytes(this.data.getPlayer()));
		//tempScenario();
	}
	
	private void tempScenario () {
		try {
			System.out.println("Process start");
			System.out.println("---------- Send join to Node A");
			this.network.publish(this.data.getNodeName() + "_join", ByteSerializable.getBytes(this.data.getPlayer()));
			Thread.sleep(5000);
			
			System.out.println();
			System.out.println("---------- Send move");
			this.network.publish(this.data.getNodeName() + "_move", ByteSerializable.getBytes(new Direction(this.data.getPlayer(), 1, 0)));
			this.network.publish(this.data.getNodeName() + "_move", ByteSerializable.getBytes(new Direction(this.data.getPlayer(), 1, 0)));
			this.network.publish(this.data.getNodeName() + "_move", ByteSerializable.getBytes(new Direction(this.data.getPlayer(), 1, 0)));
			this.network.publish(this.data.getNodeName() + "_move", ByteSerializable.getBytes(new Direction(this.data.getPlayer(), 1, 0)));
			this.network.publish(this.data.getNodeName() + "_move", ByteSerializable.getBytes(new Direction(this.data.getPlayer(), 1, 0)));
			Thread.sleep(5000);
			
			System.out.println();
			System.out.println("---------- Send move");
			this.network.publish(this.data.getNodeName() + "_move", ByteSerializable.getBytes(new Direction(this.data.getPlayer(), 1, 0)));
			Thread.sleep(5000);
			
			System.out.println();
			System.out.println("---------- Send move");
			this.network.publish(this.data.getNodeName() + "_move", ByteSerializable.getBytes(new Direction(this.data.getPlayer(), 1, 0)));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String initPlayerQueue () throws IOException {
		return this.network.createClientQueueAndListen((consumerTag, delivery) -> {
			ActionMessage actionMessage = (ActionMessage) ByteSerializable.fromBytes(delivery.getBody());
			this.notifyActionVisitors(actionMessage);
		});
	}

	/**
	 * Add an observer.
	 * 
	 * @param actionVisitor
	 */
	public void addActionVisitorObservable(ActionVisitor actionVisitor) {
		this.actionVisitorsObservers.add(actionVisitor);
	}

	/**
	 * Remove the observer "actionVisitor".
	 * 
	 * @param actionVisitor
	 */
	public void removeActionVisitorObservable(ActionVisitor actionVisitor) {
		this.actionVisitorsObservers.remove(actionVisitor);
	}

	/**
	 * Following the Observable pattern, "notify" all the ActionVisitor observers.
	 * In other words, call the "accept" method on actionMessage for all the
	 * visitors (= observers).
	 * 
	 * @param actionMessage
	 */
	private void notifyActionVisitors(ActionMessage actionMessage) {
		synchronized (this.data) {
			actionMessage.accept(this.coreActionVisitor);

			for (ActionVisitor actionVisitor : this.actionVisitorsObservers) {
				actionMessage.accept(actionVisitor);

			}
		}
	}

	public ClientData getClientData() {
		return this.data;
	}
	
	public void move (int horizontalDirection, int verticalDirection) throws IOException {
		String topic = this.data.getNodeName() + "_move";
		this.network.publish(topic, ByteSerializable.getBytes(new Direction(this.data.getPlayer(), horizontalDirection, verticalDirection)));
	}

}
