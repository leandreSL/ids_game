package core.node;

public class NodeMain {	
	public static void main (String[] args) {
		if (args[0] == null) {
			System.out.println("Argument missing : node name");
		}

		System.out.println("Node " + args[0]);
		
		if (args.length > 1 && "--logger".equals(args[1]))
			new NodeLogger(args[0]);
		else
			new Node(args[0]);
	}
}
