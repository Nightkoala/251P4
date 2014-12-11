/**
 * MailboxManager.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	Manage all of the view proxy objects.  Read all incoming data and
 * 			forward to appropriate view proxy
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.HashMap;

public class MailboxManager {

	// Hidden Data Members
	
	private DatagramSocket mailbox;
	private HashMap<SocketAddress, ViewProxy> proxyMap =
		new HashMap<SocketAddress, ViewProxy>();
	private byte[] payload = new byte[128];
	private SessionManager sessionManager = new SessionManager();
	
	// Constructor
	
	/**
	 * Constructor for creating an instance of a Mailbox Manager.
	 * 
	 * @param mailbox	The DatagramSocket that messages will be delivered to.
	 */
	public MailboxManager( DatagramSocket mailbox ) {
		this.mailbox = mailbox;
	}//end MailboxManager constructor
	
	// Methods
	
	/**
	 * Retrieves the session manager stored within this object, helper method
	 * used for exiting clients when server unexpectedly closes.
	 * 
	 * @return	The session manager.
	 */
	public SessionManager getSesionManager() {
		return this.sessionManager;
	}//end getSessionManager
	
	/**
	 * Method responsible for retriving messages to be sent back to the clients,
	 * Determines the correct client to send to and sends the message.
	 * 
	 * @throws IOException	Thrown if an IO error occurred.
	 */
	public void receiveMessage() throws IOException {
		DatagramPacket packet = new DatagramPacket( payload, payload.length );
		mailbox.receive( packet );
		SocketAddress clientAddress = packet.getSocketAddress();
		ViewProxy proxy = proxyMap.get( clientAddress );
		if( proxy == null ) {
			proxy = new ViewProxy( mailbox, clientAddress );
			proxy.setViewListener( sessionManager );
			proxyMap.put( clientAddress, proxy );
		}//end if
		if( proxy.process( packet ) ) {
			proxyMap.remove( clientAddress );
		}//end if
	}//end receiveMessage
}//end mailboxManager class
