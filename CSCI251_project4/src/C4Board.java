/**
 * C4Board.java
 * 
 * @author	Derek Brown
 *
 * Purpose:	Implementation of C4BoardIntf methods
 */

public class C4Board implements C4BoardIntf {
	
	private int[][] spot = new int[ROWS][COLS];
	
	// Constructor
	
	/**
	 * Constructor for C4Board, The board initially is empty.
	 */
	public C4Board() {}
	
	// Methods
	
	public int getSpot( int r, int c ) {
		return this.spot[r][c];
	}//end getSpot
	
	/**
	 * Places a players 'marker' on the specified tile in the board.
	 * 
	 * @param r	The row position
	 * @param c	The column position
	 * @param p	The player id
	 */
	public synchronized void setSpot( int r, int c, int p ) {
		spot[r][c] = p;
	}//end setSpot
	
	
	/**
	 * Clears the current game board, sets all of the tiles to 0.
	 */
	public synchronized void clear() {
		for( int r = 0 ; r < ROWS ; r++ ) {
			for( int c = 0 ; c < COLS ; c++ ) {
				spot[r][c] = 0;
			}//end for c
		}//end for r
	}//end clear

	@Override
	public synchronized boolean hasPlayer1Marker( int r, int c ) {
		if( spot[r][c] == 1 ) {
			return true;
		}//end if
		return false;
	}//end hasPlayer1Marker

	@Override
	public synchronized boolean hasPlayer2Marker( int r, int c ) {
		if( spot[r][c] == 2 ) {
			return true;
		}//end if
		return false;
	}//end hasPlayer2Marker

	@Override
	public synchronized int[] hasWon() {
		int[] winner = new int[4];
		int current = 0;
		int length = 0;
		// Check rows
		for( int r = 0 ; r < ROWS ; r++ ) {
			for( int c = 0 ; c < COLS ; c++ ) {
				if( current != spot[r][c] ) {
					current = spot[r][c];
					winner[0] = r;
					winner[1] = c;
					length = 1;
				}//end if
				if( current == spot[r][c] ) {
					if( current != 0 ) {
						if( length == 4) {
							winner[2] = r;
							winner[3] = c;
							return winner;
						}//end else if
						else {
							length++;
						}//end else
					}//end if
				}//end if
			}//end for c
			current = 0;
			length = 0;
		}//end for r
		// Check Columns
		current = 0;
		length = 0;
		for( int c = 0 ; c < COLS ; c++ ) {
			for( int r = 0 ; r < ROWS ; r++ ) {
				if( current != spot[r][c] ) {
					current = spot[r][c];
					winner[0] = r;
					winner[1] = c;
					length = 1;
				}//end if
				if( current == spot[r][c] ) {
					if( current != 0 ) {
						if( length == 4) {
							winner[2] = r;
							winner[3] = c;
							return winner;
						}//end else if
						else {
							length++;
						}//end else
					}//end if
				}//end if
			}//end for r
			current = 0;
			length = 0;
		}//end for c
		// Check 'increasing' diagonals
		current = 0;
		length = 0;
		int c;
		for( int d = 3 ; d <= 5 ; d++ ) {
			for( int r = d ; r >= 0 ; r-- ) {
				c = d-r;
				if( current != spot[r][c] ) {
					current = spot[r][c];
					winner[0] = r;
					winner[1] = c;
					length = 1;
				}//end if
				if( current == spot[r][c] && current != 0 ) {
					if( length == 4 ) {
						winner[2] = r;
						winner[3] = c;
						return winner;
					}//end if
					else {
						length++;
					}//end else
				}//end if
			}//end for r
			current = 0;
			length = 0;
		}//end for d
		for( int d = 1 ; d <= 3 ; d++ ) {
			for( int r = 5 ; r >= d-1 ; r-- ) {
				c = 5-r+d;
				if( current != spot[r][c] ) {
					current = spot[r][c];
					winner[0] = r;
					winner[1] = c;
					length = 1;
				}//end if
				if( current == spot[r][c] && current != 0 ) {
					if( length == 4 ) {
						winner[2] = r;
						winner[3] = c;
						return winner;
					}//end if
					else {
						length++;
					}//end else
				}//end if
			}//end for r
			current = 0;
			length = 0;
		}//end for d
		// Check 'decreasing' diagonals
		current = 0;
		length = 0;
		for( int d = 2 ; d >= 0 ; d-- ) {
			for( int r = d ; r <= 5 ; r++ ) {
				c = r-d;
				if( current != spot[r][c] ) {
					current = spot[r][c];
					winner[0] = r;
					winner[1] = c;
					length = 1;
				}//end if
				if( current == spot[r][c] && current != 0 ) {
					if( length == 4 ) {
						winner[2] = r;
						winner[3] = c;
						return winner;
					}//end if
					else {
						length++;
					}//end else
				}//end if
			}//end for r
			current = 0;
			length = 0;
		}//end for d
		for( int d = 0 ; d <= 3 ; d++ ) {
			for( int r = 0 ; r <= 5-d ; r++ ) {
				c = d+r+1;
				if( current != spot[r][c] ) {
					current = spot[r][c];
					winner[0] = r;
					winner[1] = c;
					length = 1;
				}//end if
				if( current == spot[r][c] && current != 0 ) {
					if( length == 4 ) {
						winner[2] = r;
						winner[3] = c;
						return winner;
					}//end if
					else {
						length++;
					}//end else
				}//end if
			}//end for r
			current = 0;
			length = 0;
		}//end for d
		return null;
	}//end hasWon
}//end C4Board
