package core.player;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import core.share.ByteSerializable;
import core.share.Direction;
import core.share.Player;
import core.share.action.ActionMessage;
import core.share.action.ActionVisitor;

import core.share.RabbitWrapper;

/**
 * @author Florian
 *
 */
public class Client {

	private ClientData data;
	private ActionVisitor coreActionVisitor;
	/**
	 * Synchronized observable set: set of ActionVisitor instances that will be
	 * notified when ActionMessages are received - "Synchronized" because multiple
	 * thread will access the set - "Set" because we want to ensure there are no
	 * duplicate observers
	 */
	private Set<ActionVisitor> actionVisitorsObservers;
	private RabbitWrapper network;

	public Client(String playerName, String nodeName) throws IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		this.network = new RabbitWrapper();
		this.init(playerName, nodeName);
	}
	
	private void init (String playerName, String nodeName) throws IOException {
		String playerId = this.initPlayerQueue();
		this.data = new ClientData(new Player(playerId, playerName), nodeName);
		
		this.coreActionVisitor = new CoreActionVisitor(data);
		this.actionVisitorsObservers = Collections.synchronizedSet(new HashSet<ActionVisitor>());
		this.network.publish(this.data.getNodeName() + "_join", ByteSerializable.getBytes(this.data.getPlayer()));
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
	
	/**
	 * Send a move message to the node.
	 * @param horizontalDirection
	 * @param verticalDirection
	 * @throws IOException
	 */
	public void move (int horizontalDirection, int verticalDirection) throws IOException {
		String topic = this.data.getNodeName() + "_move";
		this.network.publish(topic, ByteSerializable.getBytes(new Direction(this.data.getPlayer(), horizontalDirection, verticalDirection)));
	}

	
	/**
	 * Send a disconnection message to the node.
	 * @throws IOException
	 */
	public void disconnect () throws IOException {
		String topic = this.data.getNodeName() + "_disconnect";
		this.network.publish(topic, ByteSerializable.getBytes(this.data.getPlayer()));
	}

}
