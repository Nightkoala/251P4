/**
 * ModelProxy.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	Provides the network proxy for the model object, resides in the 
 * 		client and communicates with server.
 */

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ModelProxy implements ViewListener {

	// Hidden data members
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private ModelListener modelListener;
	
	// Constructor
	
	/**
	 * Constructor for creating an instance of the model proxy.
	 * 
	 * @param socket	The socket connection to the server.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public ModelProxy( Socket socket ) throws IOException {
		this.socket = socket;
		out = new PrintWriter( socket.getOutputStream(), true );
		in = new BufferedReader(
			new InputStreamReader( socket.getInputStream() ) );
	}//end ModelProxy constructor
	
	// Methods
	
	/**
	 * Set the model listener object for this model proxy.
	 * 
	 * @param modelListener	The model listener to be set.
	 */
	public void setModelListener( ModelListener modelListener ) {
		this.modelListener = modelListener;
		new ReaderThread().start();
	}//end setModelListener
	
	@Override
	public void join( ViewProxy proxy, String n ) throws IOException {
		out.printf( "join %s\n", n );
	}//end join

	@Override
	public void add( int p, int c ) throws IOException {
		out.printf( "add %d %d\n", p, c );
	}//end add

	@Override
	public void clear() throws IOException {
		out.printf( "clear\n" );
	}//end clear
	
	// Hidden helper class
	
	/**
	 * ReaderThread
	 * 
	 * @author	Derek Brown
	 *
	 * Purpose:	Receives messages from the network, decodes them, and
	 *		invokes the proper methods to process them.
	 */
	private class ReaderThread extends Thread {
		public void run() {
			try {
				for( ;; ) {
					int p, r, c;
					String n;
					String cmd = in.readLine();
					if( cmd == null ) {
						modelListener.turn( -1 );
						break;
					}//end if
					switch( cmd.charAt( 0 ) ) {
						case 'n':
							if( cmd.charAt( 1 ) == 'u' ) {
								p = Character.getNumericValue(
									cmd.charAt( 7 ) );
								modelListener.number( p );
								break;
							}//end if
							p = Character.getNumericValue( cmd.charAt( 5 ) );
							n = cmd.substring( 7 );
							modelListener.name( p, n );
							break;
						case 't':
							p = Character.getNumericValue( cmd.charAt( 5 ) );
							modelListener.turn( p );
							break;
						case 'a':
							p = Character.getNumericValue( cmd.charAt( 4 ) );
							r = Character.getNumericValue( cmd.charAt( 6 ) );
							c = Character.getNumericValue( cmd.charAt( 8 ) );
							modelListener.add( p, r, c );
							break;
						case 'c':
							modelListener.clear();
							break;
						default:
							System.err.println( "Bad message" );
							break;
					}//end switch
				}//end for
			} catch( IOException e ) {}//end try/catch
			finally {
				try {
					socket.close();
				} catch( IOException e ) {}//end try/catch
			}//end finally
		}//end run
	}//end ReaderThread class
}//end ModelProxy
