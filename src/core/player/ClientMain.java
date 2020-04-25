package core.player;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import core.TimeoutSetting;
import share.action.ActionVisitor;
import share.action.BasicActionVisitor;

public class ClientMain {
	public static void main (String[] args) {
		// args[0] : player name
		// args[1] : destination zone
		Client client = new Client(args[0], args[1]);
		
		CompletableFuture.delayedExecutor(TimeoutSetting.TIMEOUT, TimeUnit.SECONDS).execute(() -> {
			System.exit(0);
		});
	}
}
