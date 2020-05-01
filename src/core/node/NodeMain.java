package core.node;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import core.TimeoutSetting;

public class NodeMain {	
	public static void main (String[] args) {
		/*
		 * Separate the node's name from its neighbours' name
		 */
		String[] neighbourNodes = new String[args.length-1];
		for (int i = 0; i < neighbourNodes.length; i++) {
			neighbourNodes[i] = args[i+1];
		}
		
		System.out.println("Node " + args[0]);
		new NodeLogger(args[0], args);
		
		CompletableFuture.delayedExecutor(TimeoutSetting.TIMEOUT, TimeUnit.SECONDS).execute(() -> {
			System.exit(0);
		});
	}
}
