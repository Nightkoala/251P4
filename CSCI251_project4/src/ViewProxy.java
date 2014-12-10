/**
 * ViewProxy.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	Provides the network proxy for the view object, it resides in the
 * 			server program and communicates with the client program.
 */

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ViewProxy implements ModelListener {

	// Hidden data members
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private ViewListener viewListener;
	
	// Constructor
	
	/**
	 * Constructor for creating an instance of a view proxy.
	 * 
	 * @param socket	The socket connection to the server.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public ViewProxy( Socket socket ) throws IOException {
		this.socket = socket;
		out = new PrintWriter( socket.getOutputStream(), true );
		in = new BufferedReader(
				new InputStreamReader( socket.getInputStream() ) );
	}//end ViewProxy constructor
	
	// Methods
	
	/**
	 * Set the view listener object for this view proxy.
	 * 
	 * @param viewListener	The view listener to be set.
	 */
	public void setViewListener( ViewListener viewListener ) {
		if( this.viewListener == null ) {
			this.viewListener = viewListener;
			new ReaderThread().start();
		}//end if
	}//end setViewListener
	
	@Override
	public void number( int p ) throws IOException {
		out.printf( "number " );
		out.printf( "%d\n", p );
	}//end number

	@Override
	public void name( int p, String n ) throws IOException {
		out.printf( "name " );
		out.printf( "%d %s\n", p, n );
	}//end name

	@Override
	public void turn( int p ) throws IOException {
		out.printf( "turn " );
		out.printf( "%d\n", p );
	}//end turn

	@Override
	public void add( int p, int r, int c ) throws IOException {
		out.printf( "add " );
		out.printf( "%d %d %d\n", p, r, c );
	}//end add

	@Override
	public void clear() throws IOException {
		out.printf( "clear\n" );
	}//end clear
	
	// Hidden helper classes
	
	/**
	 * ReaderThread
	 * 
	 * @author	Derek Brown
	 *
	 * Purpose:	Receives messages from the network, decodes them, and invokes
	 * 			the proper methods to process them.
	 */
	private class ReaderThread extends Thread {
		public void run() {
			try {
				for( ;; ) {
					String name;
					int p, c;
					String cmd = in.readLine();
					switch( cmd.charAt( 0 ) ) {
						case 'j':
							name = cmd.substring( 5 );
							viewListener.join( ViewProxy.this, name );
							break;
						case 'a':
							System.out.println(cmd);
							p = Character.getNumericValue( cmd.charAt( 4 ) );
							c = Character.getNumericValue( cmd.charAt( 6 ) );
							viewListener.add( p, c );
							break;
						case 'c':
							viewListener.clear();
							break;
						default:
							System.err.println( "Bad Message" );
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
}//end ViewProxy
