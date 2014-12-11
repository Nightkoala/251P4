/**
 * ViewLIstener.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	The interface for an object that is triggered by events from the
 * 			view object in the network connect four game.
 */

import java.io.IOException;

public interface ViewListener {

	/**
	 * Send notification to server notifying it that the client has joined the
	 * game session.
	 * 
	 * @param n	The name of the client.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void join( ViewProxy proxy, String n ) throws IOException;
	
	/**
	 * Send notification to server notifying that a player has made a move.
	 * 
	 * @param p	The player's number who made the move.
	 * @param c	The column number that the move was made.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void add( int p, int c ) throws IOException;
	
	/**
	 * Send notification to the server notifying it that a player pressed
	 * New Game.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void clear() throws IOException;
	
	public void leave() throws IOException;
	
}//end ViewListener
