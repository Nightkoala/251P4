/**
 * ConnectFour.java
 * 
 * @author	Derek Brown <djb3718@rit.edu>
 * 
 * Purpose:	Client program for network connect four
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.DatagramSocket;

public class ConnectFour{

	/**
	 * If improper number of command line arguments, displays the proper
	 * usage of the program and exists.
	 */
	public static void usage() {
		System.err.printf("Usage: java ConnectFour <serverhost> <serverport> ");
		System.err.printf("<clienthost> <clientport> <playername>\n");
		System.exit(1);
	}//end usage
	
	/**
	 * The main logic for the program, Checks to make sure the usage is
	 * correct and then initializes all necessary objects to run a
	 * successful game of connect four over a network.
	 * 
	 * @param args	Command line arguments: <host> <port> <playername>
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public static void main( String[] args ) throws IOException {
		if( args.length != 5 ) {
			usage();
		}//end if
		String serverHost = args[0];
		String clientHost = args[2];
		int serverPort, clientPort;
		try {
			serverPort = Integer.parseInt( args[1] );
		} catch( NumberFormatException e ) {
			System.err.printf( "<serverport> must be an int\n" );
			e.printStackTrace( System.err );
		}//end try/catch
		serverPort = Integer.parseInt( args[1] );
		try {
			clientPort = Integer.parseInt( args[3] );
		} catch( NumberFormatException e ) {
			System.err.printf( "<clientport> must be an int\n" );
			e.printStackTrace( System.err );
		}//end try/catch
		clientPort = Integer.parseInt( args[3] );
		String playerName = args[4];
		
		DatagramSocket mailbox = new DatagramSocket( new InetSocketAddress( clientHost, clientPort ) );
		
		C4ModelClone model = new C4ModelClone();
		C4UI view = new C4UI( model.getBoard(), playerName );
		ModelProxy proxy = new ModelProxy( mailbox, new InetSocketAddress( serverHost, serverPort ) );
		model.setModelListener( view );
		view.setViewListener( proxy );
		proxy.setModelListener( model );
		
		proxy.join( null, playerName );
	}//end main
}//end ConnectFour
