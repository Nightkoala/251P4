/**
 * SessionManager.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	Maintains the model object for a session of network connect four.
 */

import java.io.IOException;
import java.util.HashMap;

public class SessionManager implements ViewListener {
	
	// Hidden Data Members
	
	private HashMap<String, ConnectFourModel> sessions =
		new HashMap<String, ConnectFourModel>();
	
	// Constructor
	
	public SessionManager() {}//end SessionManager constructor
	
	// Methods

	@Override
	public synchronized void join( ViewProxy proxy, String session) throws IOException {
		ConnectFourModel model = sessions.get( session );
		if( model == null ) {
			model = new ConnectFourModel();
			sessions.put( session, model );
		}//end if
		model.addModelListener( proxy );
		proxy.setViewListener( model );
	}//end join

	@Override
	public void add(int p, int c) throws IOException {}//end add

	@Override
	public void clear() throws IOException {}//end clear
}//end SessionManager class
