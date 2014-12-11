/**
 * ConnectFourServer.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	The implementation of the server for playing a game of connect four
 * 			over a network
 */

import java.net.InetSocketAddress;
import java.net.DatagramSocket;
import java.io.IOException;

public class ConnectFourServer {

	// Methods
	
	/**
	 * Helper method called when the improper number of command line arguments
	 * are present.
	 */
	public static void usage() {
		System.err.printf( "Usage: java ConnectFourServer " );
		System.err.printf( "<serverhost> <serverport>\n" );
		System.exit( 1 );
	}//end usage
	
	/**
	 * The main logic for running the server of a network game of connect
	 * four, Sets up the server and mailbox.
	 * 
	 * @param args			Command line arguments: <host> <port>
	 * 
	 * @throws Exception	Throws SocketException if error creating
	 * 						mailbox.
	 */
	public static void main( String[] args ) throws Exception {
		if( args.length != 2 ) {
			usage();
		}//end if
		String host = args[0];
		int port;
		try {
			port = Integer.parseInt( args[1] );
		} catch( NumberFormatException e ) {
			System.err.printf( "<serverport> must be an integer\n" );
			e.printStackTrace( System.err );
			System.exit( 1 );
		}//end try/catch
		port = Integer.parseInt( args[1] );
		
		DatagramSocket mailbox =
			new DatagramSocket(
				new InetSocketAddress( host, port ) );
		
		final MailboxManager manager = new MailboxManager( mailbox );
		
		Runtime.getRuntime().addShutdownHook( new Thread() {
			public void run() {
				try {
					manager.getSesionManager().leave();
				} catch( IOException e ) {}//end try/catch
			}//end run
		});
		
		for( ;; ) {
			manager.receiveMessage();
		}//end for
	}//end main
}//end ConnectFourServer class
