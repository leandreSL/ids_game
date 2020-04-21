package core.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import core.node.ByteSerializable;
import share.Direction;
import share.action.ActionMessage;
import share.action.ActionVisitor;
import share.action.BasicActionVisitor;

public class Client {
	private static final String EXCHANGE_NAME = "game_exchange";

	private ClientData data;
	private ActionVisitor coreActionVisitor;
	/**
	 * Synchronized observable set: set of ActionVisitor instances that will be
	 * notified when ActionMessages are received - "Synchronized" because multiple
	 * thread will access the set - "Set" because we want to ensure there are no
	 * duplicate observers
	 */
	private Set<ActionVisitor> actionVisitorsObservers;

	Channel channel;

	public Client(String playerName, String nodeName) {
		this.data = new ClientData(new Player(), nodeName);
		this.data.player.setName(playerName);

		this.coreActionVisitor = new BasicActionVisitor(data);
		this.actionVisitorsObservers = Collections.synchronizedSet(new HashSet<ActionVisitor>());
	}

	public void start() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection;

		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

			this.initPlayerQueue();
			System.out.println("Process start");
			System.out.println("---------- Send join to Node A");
			this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_join", null,
					ByteSerializable.getBytes(this.data.player));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println();
			System.out.println("---------- Send move (to Node B)");
			this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_move", null,
					ByteSerializable.getBytes(new Direction(this.data.player, 1, 0)));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println();
			System.out.println("---------- Send move (to Node A)");
			this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_move", null,
					ByteSerializable.getBytes(new Direction(this.data.player, 1, 0)));

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println();
			System.out.println("---------- Send move (to Node B)");
			this.channel.basicPublish(EXCHANGE_NAME, this.data.nodeName + "_move", null,
					ByteSerializable.getBytes(new Direction(this.data.player, 1, 0)));
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO
			e.printStackTrace();
		}
	}

	private void initPlayerQueue() {
		try {
			// Create the queue and bind it to the topic
			String queueNamePlayer = channel.queueDeclare().getQueue();
			channel.queueBind(queueNamePlayer, EXCHANGE_NAME, queueNamePlayer);

			this.data.player.setId(queueNamePlayer);

			/*
			 * Subscribe to the topic "[queueNamePlayer]"
			 */
			DeliverCallback deliverCallbackPlayer = (consumerTag, delivery) -> {
				ActionMessage actionMessage = (ActionMessage) ByteSerializable.fromBytes(delivery.getBody());
				this.notifyActionVisitors(actionMessage);
			};
			channel.basicConsume(queueNamePlayer, true, deliverCallbackPlayer, consumerTag -> {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		// TODO : add Board to the synchronization ?
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

}
