/**
 * ConnectFour.java
 * 
 * @author	Derek Brown <djb3718@rit.edu>
 * 
 * Purpose:	Client program for network connect four
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectFour{

	/**
	 * If improper number of command line arguments, displays the proper
	 * usage of the program and exists.
	 */
	public static void usage() {
		System.err.println(
			"Usage: java ConnectFour <host> <port> <playername>" );
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
		if( args.length != 3 ) {
			usage();
		}//end if
		String host = args[0];
		int port;
		try {
			port = Integer.parseInt( args[1] );
		} catch( NumberFormatException e ) {
			System.err.printf( "<port> must be an int\n" );
			e.printStackTrace( System.err );
		}//end try/catch
		port = Integer.parseInt( args[1] );
		String playerName = args[2];
		
		Socket socket = new Socket();
		socket.connect( new InetSocketAddress( host, port ) );
		
		C4ModelClone model = new C4ModelClone();
		C4UI view = new C4UI( model.getBoard(), playerName );
		ModelProxy proxy = new ModelProxy( socket );
		model.setModelListener( view );
		view.setViewListener( proxy );
		proxy.setModelListener( model );
		proxy.join( null, playerName );
	}//end main
}//end ConnectFour
