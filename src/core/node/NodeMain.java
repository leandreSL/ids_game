package core.node;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import core.share.RabbitWrapper;

public class NodeMain {	
	public static void main (String[] args) {
		Options options = options();
		CommandLineParser cliParser = new DefaultParser();
		CommandLine line = null;

		try {
			line = cliParser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("Erreur : verifiez la syntaxe des arguments");
			return;
		}
		
		// Display the help and exit if there is the option "h"
		if (line.hasOption("h")) {
			showHelp(options);
			return;
		}
		
		if (args.length == 0) {
			System.err.println("Argument missing : node name");
			return;
		}

		try {
			if (line.hasOption("uri")) {
				
					RabbitWrapper.setURI(line.getOptionValue("uri"));
					if (line.hasOption("l")) {
						new NodeLogger(args[0]);
					}
					else {
						new Node(args[0]);
					}
				
			}
			else {
				if (line.hasOption("l")) {
					new NodeLogger(args[0]);
				}
				else {
					new Node(args[0]);
				}
			}
		}
		catch (Exception e) {
			System.err.println("Echec de la connection a RabbitMQ,");
			System.err.println("verifiez que le serveur associe a l'URI suivante existe bien :");
			System.err.println(line.getOptionValue("uri"));
			return;
		}
		
		System.out.println("Node " + args[0]);
	}
	
	/**
	 * Display the help
	 *
	 * @param options
	 */
	private static void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(" ",
				options);
	}

	private static Options options() {
		Options options = new Options();

		Option logger = new Option("l", "logger", false, "launch in logger mode");	
		Option help = new Option("h", "help", false, "display help");
		Option uri = Option.builder("uri").longOpt("uri").hasArg().argName("URI")
				.desc("URI of the RabbitMQ server to be used instead of localhost, ex: amqp://guest:guest@192.168.99.100:5672").build();
		options.addOption(logger);
		options.addOption(help);
		options.addOption(uri);
		
		return options;
	}


}
