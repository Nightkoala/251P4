/**
 * C4ModelClone.java
 * 
 * @author	Derek Brown
 *
 * Purpose:	The clone of the model used by the server, used for notifying
 *		the C4UI when changes have been made.
 */

import java.io.IOException;

public class C4ModelClone implements ModelListener {
	
	// Hidden Data Members
	
	private C4Board board = new C4Board();
	private ModelListener modelListener;
	
	// Constructor
	
	/**
	 * Constructor for creating a C4ModelClone
	 */
	public C4ModelClone() {}//end C4ModelClone constructor
	
	// Methods
	
	/**
	 * Retrieve the current game board.
	 * 
	 * @return	C4Board object, the current board.
	 */
	public C4Board getBoard() {
		return board;
	}//end getBoard
	
	/**
	 * Set the model listener for this connect four model clone
	 * 
	 * @param modelListener	The model listener to be set
	 */
	public void setModelListener( ModelListener modelListener ) {
		this.modelListener = modelListener;
	}//end setModelListener

	@Override
	public void number( int p ) throws IOException {
		modelListener.number( p );
	}//end number

	@Override
	public void name( int p, String n ) throws IOException {
		modelListener.name( p, n ) ;
	}//end name

	@Override
	public void turn( int p ) throws IOException {
		modelListener.turn( p );
	}//end turn

	@Override
	public void add( int p, int r, int c ) throws IOException {
		board.setSpot( r, c, p );
		modelListener.add( p, r, c );
	}//end add

	@Override
	public void clear() throws IOException {
		board.clear();
		modelListener.clear();
	}//end clear
}//end C4ModelClone
