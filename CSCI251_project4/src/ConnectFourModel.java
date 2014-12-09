/**
 * ConnectFourModel.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	The server-side model for a network game of connect four
 */

import java.io.IOException;
import java.util.LinkedList;
import java.util.Iterator;

public class ConnectFourModel implements ViewListener {
	
	// Hidden Data Members
	
	private C4Board board = new C4Board();
	private LinkedList<ModelListener> listeners =
		new LinkedList<ModelListener>();
	
	// Constructor
	
	public ConnectFourModel() {}//end ConnectFourModel constructor
	
	// Methods
	
	public synchronized void addModelListener( ModelListener modelListener ) {
		try {
			for( int r = 0 ; r < C4Board.ROWS ; ++r ) {
				for( int c = 0 ; c < C4Board.COLS ; ++c ) {
					if( board.hasPlayer1Marker( r, c ) ) {
						modelListener.add( 1, r, c );
					}//end if
					else if( board.hasPlayer2Marker( r, c ) ) {
						modelListener.add( 2, r, c );
					}//end else if
				}//end for c
			}//end for r
			listeners.add( modelListener );
		} catch( IOException e ) {}//end try/catch
	}//end addModelListener

	@Override
	public void join(ViewProxy proxy, String n) throws IOException {}//end join

	@Override
	public void add(int p, int c) throws IOException {
		// update board state
		// find r
		int rr = -1;
		for( int r = 5 ; r >= 0 ; r-- ) {
			if( !( board.hasPlayer1Marker(r, c) && board.hasPlayer2Marker(r, c) ) ) {
				board.setSpot(r, c, p);
				rr = r;	// Assumes that row is not off edge, will always be set
				break;
			}//end if
		}//end for r
		Iterator<ModelListener> iter = listeners.iterator();
		while( iter.hasNext() ) {
			ModelListener listener = iter.next();
			try {
				if( rr != -1 ) {
					listener.add(p, rr, c);
				}//end if
			} catch( IOException e ) {
				iter.remove();
			}//end try/catch
		}//end while
	}//end add

	@Override
	public void clear() throws IOException {
		board.clear();
		Iterator<ModelListener> iter = listeners.iterator();
		while( iter.hasNext() ) {
			ModelListener listener = iter.next();
			try {
				listener.clear();
			} catch( IOException e ) {
				iter.remove();
			}//end try/catch
		}//end while
	}//end clear
}//end ConnectFourModel class
