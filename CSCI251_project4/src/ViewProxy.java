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
		System.out.printf("sending -- number %d\n", p);
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
		System.out.printf("sending -- name %d %s\n", p, n);
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
		System.out.printf("sending -- turn %d\n", p);
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
		System.out.printf("sending -- add %d %d %d\n", p, r, c);
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
		System.out.printf("sending -- clear\n");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'c' );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, clientAddress ) );
	}//end clear
	
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
				System.out.printf("receive -- join %s\n", name);
				viewListener.join( ViewProxy.this, name );
				break;
			case 'a':
				p = in.readByte();
				c = in.readByte();
				System.out.printf("receive --add %d %d\n", p, c);
				viewListener.add( p, c );
				break;
			case 'c':
				System.out.println("receive --clear");
				viewListener.clear();
				break;
			case 'l':
				discard = true;
				break;
			default:
				System.err.println( "Bad message." );
				break;
		}//end switch
		return discard;
	}//end process
}//end ViewProxy
