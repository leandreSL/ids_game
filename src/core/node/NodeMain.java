package core.node;

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
		Node node = new NodeLogger(args[0], args);
	}
}
