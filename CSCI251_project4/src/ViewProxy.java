/**
 * ViewProxy.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	Provides the network proxy for the view object, it resides in the
 * 			server program and communicates with the client program.
 */

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class ViewProxy implements ModelListener {

	// Hidden data members
	
	private ViewListener viewListener;	
	private DatagramSocket mailbox;
	private SocketAddress clientAddress;
	
	// Constructor
	
	/**
	 * Constructor for creating an instance of a view proxy.
	 * 
	 * @param socket	The socket connection to the server.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public ViewProxy( DatagramSocket mailbox, SocketAddress clientAddress ) throws IOException {
		this.mailbox = mailbox;
		this.clientAddress = clientAddress;
	}//end ViewProxy constructor
	
	// Methods
	
	/**
	 * Set the view listener object for this view proxy.
	 * 
	 * @param viewListener	The view listener to be set.
	 */
	public void setViewListener( ViewListener viewListener ) {
			this.viewListener = viewListener;
	}//end setViewListener
	
	@Override
	public void number( int p ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte('n');
		out.writeByte( p );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, clientAddress ) );
	}//end number

	@Override
	public void name( int p, String n ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'N' );
		out.writeByte( p );
		out.writeUTF( n );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, clientAddress ) );
	}//end name

	@Override
	public void turn( int p ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 't' );
		out.writeByte( p );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, clientAddress ) );
	}//end turn

	@Override
	public void add( int p, int r, int c ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'a' );
		out.writeByte( p );
		out.writeByte( r );
		out.writeByte( c );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, clientAddress ) );
	}//end add

	@Override
	public void clear() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'c' );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, clientAddress ) );
	}//end clear
	
	/**
	 * Helper function to decode incoming messages from the client and pass them
	 * to the model.
	 * 
	 * @param datagram		The message received.
	 * 
	 * @return				true - can remove proxy from game
	 * 						false - otherwise
	 * 
	 * @throws IOException	Thrown if an IO error occurred.
	 */
	public boolean process( DatagramPacket datagram ) throws IOException {
		boolean discard = false;
		DataInputStream in =
			new DataInputStream(
				new ByteArrayInputStream(
					datagram.getData(), 0, datagram.getLength() ) );
		String name;
		int p, c;
		byte b = in.readByte();
		switch( b ) {
			case 'j':
				name = in.readUTF();
				viewListener.join( ViewProxy.this, name );
				break;
			case 'a':
				p = in.readByte();
				c = in.readByte();
				viewListener.add( p, c );
				break;
			case 'c':
				viewListener.clear();
				break;
			case 'l':
				discard = true;
				viewListener.leave();
				break;
			default:
				break;
		}//end switch
		return discard;
	}//end process
}//end ViewProxy
