import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Class C4UI provides the user interface for the network game of Connect Four.
 *
 * @author  Alan Kaminsky, Derek Brown
 * @version 13-Oct-2014
 */
public class C4UI implements ModelListener
	{

// Hidden data members.

	private C4BoardIntf c4board;

	private JFrame frame;
	private C4Panel boardPanel;
	private JTextField message;
	private JButton newGameButton;
	
	private ViewListener viewListener;
	
	private int p;
	private String theirName;
	private boolean yourTurn;
	private boolean gameOver = false;
	private boolean isFull = false;
	private int numMoves = 0;

// Exported constructors.

	/**
	 * Construct a new Connect Four UI.
	 *
	 * @param  board  Connect Four board.
	 * @param  name   Player's name.
	 */
	public C4UI
		(C4BoardIntf board,
		 String name)
		{
		c4board = board;

		// Set up window.
		frame = new JFrame ("Connect Four -- " + name);
		Container pane = frame.getContentPane();
		JPanel p1 = new JPanel();
		pane.add (p1);
		p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
		p1.setBorder (BorderFactory.createEmptyBorder (10, 10, 10, 10));

		// Create and add widgets.
		boardPanel = new C4Panel (c4board);
		boardPanel.setAlignmentX (0.5f);
		p1.add (boardPanel);
		p1.add (Box.createVerticalStrut (10));
		JPanel p2 = new JPanel();
		p2.setLayout (new BoxLayout (p2, BoxLayout.X_AXIS));
		p2.setAlignmentX (0.5f);
		p1.add (p2);
		message = new JTextField (20);
		message.setAlignmentY (0.5f);
		message.setEditable (false);
		message.setText ("Waiting for partner");
		p2.add (message);
		p2.add (Box.createHorizontalStrut (10));
		newGameButton = new JButton ("New Game");
		newGameButton.setAlignmentY (0.5f);
		newGameButton.setEnabled (false);
		p2.add (newGameButton);

		// Clicking the Connect Four panel informs the view listener.
		boardPanel.addMouseListener (new MouseAdapter()
			{
			public void mouseClicked (MouseEvent e)
				{
				int c = boardPanel.clickToColumn (e);
				try {
					if( yourTurn && !gameOver && !isFull ) {
						viewListener.add( p, c );
					}//end if
				} catch( IOException exc ) {}//end try/catch
				}
			});

		// Clicking the New Game button informs the view listener.
		newGameButton.addActionListener (new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				try {
					viewListener.clear();
					gameOver = false;
				} catch( IOException exc ) {}//end try/catch
				}
			});

		// Closing the window exits the client.
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		// Display window.
		frame.pack();
		frame.setVisible (true);
		}
	
		/**
		 * Sets the view listener for this C4 UI.
		 * 
		 * @param viewListener	The view listener to be set.
		 */
		public void setViewListener( ViewListener viewListener ) {
			this.viewListener = viewListener;
		}//end setViewListener

		@Override
		public void number( int p ) throws IOException {
			this.p = p;
			boardPanel.repaint();
		}//end number

		@Override
		public void name( int p, String n ) throws IOException {
			if( p != this.p ) {
				this.theirName = n;
				newGameButton.setEnabled( true );
				boardPanel.updateUI();
			}//end if
			boardPanel.repaint();
		}//end name

		@Override
		public void turn(int p) throws IOException {
			if( p == -1 ) {
				frame.dispatchEvent(
						new WindowEvent( frame,
						WindowEvent.WINDOW_CLOSING ) );
			}//end if
			if( p == this.p ) {
				message.setText( "Your turn" );
				yourTurn = true;
				boardPanel.updateUI();
			}//end if
			else if( p == 0 ) {
				message.setText( "Game over" );
				boardPanel.updateUI();
				gameOver = true;
			}//end else if
			else {
				message.setText( theirName + "'s turn" );
				yourTurn = false;
				boardPanel.updateUI();
			}//end else
			if( numMoves == 42 ) {
				isFull = true;
			}//end if
			numMoves++;
			boardPanel.repaint();
		}//end turn

		@Override
		public void add( int p, int r, int c ) throws IOException {
			boardPanel.repaint();
		}//end add

		@Override
		public void clear() throws IOException {
			gameOver = false;
			isFull = false;
			numMoves = 0;
			boardPanel.repaint();
		}//end clear
	}//end C4UI
