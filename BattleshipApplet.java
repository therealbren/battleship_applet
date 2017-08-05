// Battleship
// CS 201
// John Bowllan and Brendan Murphy
// BattleshipApplet class

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")

public class BattleshipApplet extends Applet implements ActionListener {
	/*
	setting up the Battleship Applet
	overall BorderLayout
	contains two canvases: 'left' of type fieldBattleshipCanvas
						   'right' of type BattleshipCanvas

	'left' is where the user fires and where the CPU has its ships placed
	'right' is where the user places his/her ships and where the CPU fires

	The following are also included:
		- 3 buttons: "Set My Ships", "New Game", and "Strike!"
		- 2 drop down menus: one that specifies the type of ship to place
							 one that specifies the orientation of that ship
		- title image
		- instructions
	*/

	// instance variables
	protected TitleCanvas c;
	protected BattleshipCanvas right;
	protected fieldBattleshipCanvas left;
	static volatile Choice shipChoice, directionChoice;
	protected Button setButton, newGameButton, strikeButton;
	static final Color lightBlue= new Color(200, 220, 255);
	static final Color darkRed = new Color(160, 0, 100);

	public void init() {
		// initializing the Applet
		setFont(new Font("TimesRoman", Font.BOLD, 18));
		setLayout(new BorderLayout());

		setButton = new Button("Set My Ships");
		setButton.setBackground(lightBlue);
		setButton.addActionListener(this);

		newGameButton = new Button("New Game");
		newGameButton.setBackground(Color.white);
		newGameButton.addActionListener(this);

		strikeButton = new Button("Strike!");
		strikeButton.setBackground(darkRed);
		strikeButton.addActionListener(this);

		// image that will be placed in the north portion of the Border Layout
		// generates the BattleshipApplet$TitleCanvas.class upon compile
		Image card1 = getImage(getDocumentBase(), "battleship-logo.jpg");
        c = new TitleCanvas(card1);
        c.setPreferredSize(new Dimension(100, 100));
        c.setBackground(Color.black);

		add("North", c);
		add("South", south());
		add("East", east());
		// set up field on left
		left = new fieldBattleshipCanvas();
		left.setBackground(Color.BLACK);
		left.addMouseListener(left);
		left.newGame();
		// set up board on right
		right = new BattleshipCanvas();
		right.setBackground(Color.BLACK);
		right.addMouseListener(right);
		right.newGame();
		// adding 'left' and 'right' to the center of the overall BorderLayout
		Panel center1 = new Panel();
		center1.setLayout(new GridLayout(1,2));
		center1.add(left);
		center1.add(right);
		add("Center", center1);

	}
	// drop down menus
	public Panel east(){
		Font choice = new Font("TimesRoman", Font.BOLD, 18);
		Panel east = new Panel();
		east.setBackground(Color.white);
		east.setLayout(new GridLayout(2,1));

		shipChoice = new Choice();
		shipChoice.setFont(choice);
		shipChoice.setBackground(Color.white);
		shipChoice.setForeground(Color.black);
		shipChoice.add("Carrier(5)");
		shipChoice.add("Battleship(4)");
		shipChoice.add("Submarine(3)");
		shipChoice.add("Patrol Boat(2)");
		shipChoice.select(0);

		Panel Northeast = new Panel();
		Northeast.setLayout(new GridLayout(2,1));
		Northeast.add(new Label("Ships: "));
		Northeast.setBackground(Color.white);
		Northeast.setForeground(Color.black);
		Northeast.add(shipChoice);

		directionChoice = new Choice();
		directionChoice.setFont(choice);
		directionChoice.setBackground(Color.white);
		directionChoice.setForeground(Color.black);
		directionChoice.add("Up");
		directionChoice.add("Down");
		directionChoice.add("Left");
		directionChoice.add("Right");
		directionChoice.select(1);

		Panel Southeast = new Panel();
		Southeast.setLayout(new GridLayout(2,1));
		Southeast.setBackground(Color.white);
		Southeast.setForeground(Color.black);
		Southeast.add(new Label("Direction: "));
		Southeast.add(directionChoice);

		east.add(Northeast);
		east.add(Southeast);
		return east;
	}
	// directions and various buttons
	public Panel south(){
		Panel south = new Panel();
		south.setLayout(new FlowLayout());
		south.setBackground(Color.BLACK);
		south.add(setButton);
		south.add(strikeButton);
		south.add(newGameButton);

		return south;
	}

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == setButton) {
            left.set();
            right.set();
            if (left.sunkCount == 14 ||
            	    right.sunkCount == 14) left.canClick = false;
            // this conditional prevents the user from affecting the left
            // canvas after the game has finished

        } else if (e.getSource() == newGameButton) {
        	left.newGame();
            right.newGame();

        } else if (e.getSource() == strikeButton) {
        	if (right.setup == false && left.strikeApplicable == true){
        		// This conditional allows the user to press the strike button
        		// if and only if the user is done setting up his or her ships
        		// and the user highlighted a square to potentially strike.
        		// It makes sure the user and the CPU take turns.

        	left.strike();
        	right.strike();
        	}
        	if (left.sunkCount == 14 ||
        	    right.sunkCount == 14) {
        		left.canClick = false;
        		right.canClick = false;
        	}
        	// this conditional prevents the user from affecting both the left
            // canvas and the right canvas after the game has finished
    	}
}

class TitleCanvas extends Canvas {
	// canvas class that we use to paint the image
	Dimension d;
	int startPt;
    Image card1;

    public TitleCanvas(Image c1) {
    	d = getSize();
    	startPt = (int) d.getWidth() + 300;
        card1 = c1;
    }

    public void paintMe(){
    	repaint();
    }
    // draw the boxes
    public void paint(Graphics g) {
    	//System.out.println(d);
        g.drawImage(card1, startPt, 0, this);
    }
}
}
