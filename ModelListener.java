/**
 * ModelListener.java
 * 
 * @author	Derek Brown
 *
 * Purpose:	The interface for an object that is triggered by events from the
 * 		model object.
 */

import java.io.IOException;

public interface ModelListener {
	
	/**
	 * Report the player's number in the game session.
	 * 
	 * @param p	The player's number
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void number( int p ) throws IOException;
	
	/**
	 * Report the player name with their player number.
	 * 
	 * @param p	The player's number
	 * @param n	The player's name
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void name( int p, String n ) throws IOException;
	
	/**
	 * Sent to each client, reports whose turn it is.
	 * 
	 * @param p	The player's number whose turn it is.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void turn( int p ) throws IOException;
	
	/**
	 * Sent to each client, reports where a marker was added and by which
	 * player.
	 * 
	 * @param p	The player's number who made the move.
	 * @param r	The row position.
	 * @param c	The column position.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void add( int p, int r, int c ) throws IOException;
	
	/**
	 * Report that a new game has been started.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public void clear() throws IOException;
}//end ModelListener
