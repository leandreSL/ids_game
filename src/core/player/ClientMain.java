package core.player;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import core.TimeoutSetting;

public class ClientMain {
	public static void main (String[] args) {
		// args[0] : player name
		// args[1] : destination zone
		try {
			Client client = new Client(args[0], args[1]);
		}
		catch (IOException e) {
			// TODO : Erreur de connexion : l'afficher au client, lui permettre de réessayer
			e.printStackTrace();
			System.out.println(">>> Connection error to the node " + args[1] + " <<<");
		}
		
		CompletableFuture.delayedExecutor(TimeoutSetting.TIMEOUT, TimeUnit.SECONDS).execute(() -> {
			System.exit(0);
		});
	}
}
