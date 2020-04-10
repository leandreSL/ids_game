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
		
		Node node = new Node(args[0], args);
	}
}
