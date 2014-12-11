/**
 * ModelProxy.java
 * 
 * @author	Derek Brown
 * 
 * Purpose:	Provides the network proxy for the model object, resides in the 
 * 		client and communicates with server.
 */

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class ModelProxy implements ViewListener {

	// Hidden data members
	
	private DatagramSocket mailbox;
	private SocketAddress destination;
	private ModelListener modelListener;
	
	// Constructor
	
	/**
	 * Constructor for creating an instance of the model proxy.
	 * 
	 * @param socket	The socket connection to the server.
	 * 
	 * @throws IOException	Thrown if an I/O error occurred.
	 */
	public ModelProxy( DatagramSocket mailbox, SocketAddress destination ) throws IOException {
		this.mailbox = mailbox;
		this.destination = destination;
	}//end ModelProxy constructor
	
	// Methods
	
	/**
	 * Set the model listener object for this model proxy.
	 * 
	 * @param modelListener	The model listener to be set.
	 */
	public void setModelListener( ModelListener modelListener ) {
		this.modelListener = modelListener;
		new ReaderThread().start();
	}//end setModelListener
	
	@Override
	public void join( ViewProxy proxy, String n ) throws IOException {
		System.out.printf("Sending -- join %s\n", n);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'j' );
		out.writeUTF( n );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, destination ) );
	}//end join

	@Override
	public void add( int p, int c ) throws IOException {
		System.out.printf("Sending -- add %d %d\n", p, c);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'a' );
		out.writeByte( p );
		out.writeByte( c );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, destination ) );
	}//end add

	@Override
	public void clear() throws IOException {
		System.out.printf("Sending -- clear\n");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'c' );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, destination ) );
	}//end clear
	
	@Override
	public void leave() throws IOException {
		System.out.printf("Sending -- leave\n");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream( baos );
		out.writeByte( 'l' );
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(
			new DatagramPacket(
				payload, payload.length, destination ) );
	}//end leave
	
	// Hidden helper class
	
	/**
	 * ReaderThread
	 * 
	 * @author	Derek Brown
	 *
	 * Purpose:	Receives messages from the network, decodes them, and
	 *		invokes the proper methods to process them.
	 */
	private class ReaderThread extends Thread {
		public void run() {
			byte[] payload = new byte[128];
			try {
				for( ;; ) {
					DatagramPacket packet = 
						new DatagramPacket( payload, payload.length );
					mailbox.receive( packet );
					DataInputStream in =
						new DataInputStream(
							new ByteArrayInputStream( payload, 0, packet.getLength() ) );
					int p, r, c;
					String n;
					byte b = in.readByte();
					switch( b ) {
						case 'n':	//number
							p = in.readByte();
							System.out.printf("Receive -- number %d\n", p);
							modelListener.number( p );
							break;
						case 'N':	//name
							p = in.readByte();
							n = in.readUTF();
							System.out.printf("Receive -- name %d %s\n", p, n);
							modelListener.name( p, n );
							break;
						case 't':	//turn
							p = in.readByte();
							System.out.printf("Receive -- turn %d\n", p);
							modelListener.turn( p );
							break;
						case 'a':	//add
							p = in.readByte();
							r = in.readByte();
							c = in.readByte();
							System.out.printf("Receive -- add %d %d %d\n", p, r, c);
							modelListener.add( p, r, c );
							break;
						case 'c':	//clear
							System.out.printf("Receive -- clear\n");
							modelListener.clear();
							break;
						default:
							System.err.println("Bad message.");
							break;
					}//end switch
				}//end for
			} catch( IOException e ) {}//end try/catch
			finally {
				mailbox.close();
			}//end finally
		}//end run
	}//end ReaderThread class
}//end ModelProxy
