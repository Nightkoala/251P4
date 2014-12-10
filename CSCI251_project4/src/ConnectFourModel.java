/**
 * ConnectFourModel.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	The server-side model for a network game of connect four
 */

import java.io.IOException;

public class ConnectFourModel implements ViewListener {
	
	// Hidden Data Members
	
	private C4Board board = new C4Board();
	private ModelListener[] listeners =
		new ModelListener[2];
	private String player1Name = null;
	private String player2Name = null;
	private int numPlayers = 0;
	
	// Constructor
	
	public ConnectFourModel() {}//end ConnectFourModel constructor
	
	// Methods
	
	public String getPlayer1Name() {
		return this.player1Name;
	}//end getPlayer1Name
	
	public String getPlayer2Name() {
		return this.player2Name;
	}//end getPlayer2Name
	
	public void setPlayer1Name( String n ) {
		this.player1Name = n;
	}//end setPlayer1Name
	
	public void setPlayer2Name( String n ) {
		this.player2Name = n;
	}//end setPlayer2Name
	public void incNumPlayers() {
		this.numPlayers++;
	}//end incNumPlayers
	
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
			listeners[numPlayers] = modelListener;
		} catch( IOException e ) {}//end try/catch
	}//end addModelListener
	
	public static void printBoard( C4Board b) {
		for( int r = 0 ; r < C4Board.ROWS ; r++ ) {
			for( int c = 0 ; c < C4Board.COLS ; c++ ) {
				System.out.printf("%d ", b.getSpot(r, c));
			}//end for c
			System.out.println();
		}//end for r
	}//end printBoard

	@Override
	public void join(ViewProxy proxy, String n) throws IOException {
		if( n == player1Name ) {
			listeners[0].number( 1 );
			listeners[0].name( 1, n );
		}//end if
		else if( n == player2Name ) {
			ModelListener ml1 = listeners[0];
			ModelListener ml2 = listeners[1];
			ml1.name( 2, player2Name );
			ml1.turn( 1 );
			ml2.number( 2 );
			ml2.name( 1, player1Name );
			ml2.name( 2, player2Name );
			ml2.turn( 1 );
		}//end else if
	}//end join

	@Override
	public void add(int p, int c) throws IOException {
		// update board state
		// find r
		int rr = -1;
		for( int r = 5 ; r >= 0 ; r-- ) {
			if( !( board.hasPlayer1Marker(r, c) || board.hasPlayer2Marker(r, c) ) ) {
				board.setSpot(r, c, p);
				rr = r;	// Assumes that row is not off edge, will always be set
				break;
			}//end if
		}//end for r
		// notify players of board change
		for( int i = 0 ; i < 2 ; i++ ) {
			ModelListener listener = listeners[i];
			try {
				if( rr != -1 ) {
					listener.add(p, rr, c);
				}//end if
			} catch( IOException e ) {}//end try/catch
		}//end for i
		
		// notify players of player turn
		int[] winner = board.hasWon();
		// if game over
		if( winner != null ) {
			for( int i = 0 ; i < 2 ; i++ ) {
				ModelListener listener = listeners[i];
				try {
					listener.turn( 0 );
				} catch ( IOException e ) {}//end try/catch
			}//end for i
		}//end if
		// not game over
		else {
			for( int i = 0 ; i < 2 ; i++ ) {
				ModelListener listener = listeners[i];
				try {
					if( p == 1 ) {
						listener.turn( 2 );
					}//end if
					else if( p == 2 ) {
						listener.turn( 1 );
					}//end else if
				} catch ( IOException e ) {}//end try catch
			}//end for i
		}//end else
	}//end add

	@Override
	public void clear() throws IOException {
		board.clear();
		for( int i = 0 ; i < numPlayers ; i++ ) {
			ModelListener listener = listeners[i];
			try {
				listener.clear();
			} catch( IOException e ) {}//end try/catch
		}//end for i
	}//end clear
}//end ConnectFourModel class
