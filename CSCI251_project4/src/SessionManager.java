/**
 * SessionManager.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	Maintains the model object for a session of network connect four.
 */

import java.io.IOException;
import java.util.ArrayList;

public class SessionManager implements ViewListener {
	
	// Hidden Data Members
	
	private ArrayList<ConnectFourModel> sessions = new ArrayList<ConnectFourModel>();
	private int numSessions = 0;
	
	// Constructor
	
	public SessionManager() {
		this.sessions.add(0, null);
	}//end SessionManager constructor
	
	// Methods

	@Override
	public synchronized void join( ViewProxy proxy, String session) throws IOException {
		ConnectFourModel model = sessions.get( numSessions );
		if( model == null ) {
			model = new ConnectFourModel();
			model.setPlayer1Name( session );
			sessions.add( numSessions, model );
		}//end if
		else {
			model.setPlayer2Name( session );
			numSessions++;
		}//end else
		model.addModelListener( proxy );
		proxy.setViewListener( model );
	}//end join

	@Override
	public void add(int p, int c) throws IOException {}//end add

	@Override
	public void clear() throws IOException {}//end clear
}//end SessionManager class
